package com.zzf.medical.controller;

import com.zzf.medical.common.CommonRes;
import com.zzf.medical.common.Const;
import com.zzf.medical.enums.EmCommonError;
import com.zzf.medical.exception.BusinessException;
import com.zzf.medical.pojo.User;
import com.zzf.medical.request.LoginReq;
import com.zzf.medical.request.RegisterReq;
import com.zzf.medical.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@Api(tags = "用户控制类")
@Slf4j
public class UserController {
    @Autowired
    private IUserService userService;
    /**
     * 登录
     * */
    @PostMapping("/login")
    public CommonRes login(@RequestBody @Valid LoginReq loginReq,
                           HttpServletRequest request,
                           HttpServletResponse response) throws BusinessException {
        log.info("UserController login loginReq={}",loginReq);
        User user = userService.login(loginReq.getUsername(),loginReq.getPassword());
        if(user != null){
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            userService.updateByPrimaryKey(user);
            user.setPassword("");
            request.getSession().setAttribute(Const.CURRENT_USER_SESSION,user);
            response.addCookie(new Cookie("token",token));
        }
        return CommonRes.create(user);
    }
    /**
     * 注册
     * **/
    @PostMapping("/register")
    public CommonRes register(@RequestBody @Valid RegisterReq registerReq) throws BusinessException {
        log.info("UserController register registerReq={}",registerReq);
        User user = new User();
        String token = UUID.randomUUID().toString();
        BeanUtils.copyProperties(registerReq,user);
        user.setToken(token);
        User resUser = userService.register(user);
        resUser.setPassword("");
        return CommonRes.create(resUser);
    }
    /**
     * 用户登出,清空session和cookie
     * ***/
    @GetMapping("/logout")
    @ApiOperation("登出")
    public CommonRes logout(HttpServletRequest request,
                            HttpServletResponse response) throws BusinessException {
        log.info("UserController logout logout");
        request.getSession().invalidate();
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            try{
                for(Cookie cookie:cookies){
                    Cookie newCookie = new Cookie("token",null);
                    cookie.setMaxAge(0);
                    response.addCookie(newCookie);
                }
            }catch(Exception ex){
                throw new BusinessException(EmCommonError.CLEAR_COOKIES_ERROR);
            }
        }
        return CommonRes.create(null);
    }
    /***
     * 获取当前User登录状态获取用户信息
     * **/
    @ApiOperation("登录状态查询 使用session   CURRENT_USER_SESSION ")
    @GetMapping("/getCurrentUser")
    public CommonRes getCurrentUser(HttpServletRequest request) throws BusinessException {
        log.info("UserController getCurrentUser");
        User user = (User) request.getSession().getAttribute(Const.CURRENT_USER_SESSION);
        if(user == null){
            throw new BusinessException(EmCommonError.USER_NOT_LOGIN);
        }
        return CommonRes.create(user);
    }
}
