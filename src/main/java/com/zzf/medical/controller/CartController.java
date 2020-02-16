package com.zzf.medical.controller;

import com.zzf.medical.common.CommonRes;
import com.zzf.medical.common.Const;
import com.zzf.medical.enums.EmCommonError;
import com.zzf.medical.exception.BusinessException;
import com.zzf.medical.pojo.User;
import com.zzf.medical.request.CartAddReq;
import com.zzf.medical.request.CartUpdateReq;
import com.zzf.medical.service.ICartService;
import com.zzf.medical.vo.CartVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/carts")
@Api(tags = "购物车模块")
public class CartController {
    @Autowired
    private ICartService cartService;
    @GetMapping("/list")
    public CommonRes list(HttpServletRequest request) throws BusinessException {
        User user = (User) request.getSession().getAttribute(Const.CURRENT_USER_SESSION);
        if(user == null){
            throw new BusinessException(EmCommonError.USER_NOT_LOGIN);
        }
        CartVo cartVo = cartService.list(user.getId());
        return CommonRes.create(cartVo);
    }
    @PostMapping("/add")
    public CommonRes add(@Valid @RequestBody CartAddReq cartAddReq,
                         HttpServletRequest request) throws BusinessException {
        User user = (User) request.getSession().getAttribute(Const.CURRENT_USER_SESSION);
        if(user == null){
            throw new BusinessException(EmCommonError.USER_NOT_LOGIN);
        }
        return CommonRes.create(cartService.add(user.getId(),cartAddReq));
    }
    @PutMapping("/{productId}")
    public CommonRes put(@Valid @RequestBody CartUpdateReq cartUpdateReq,
                         @PathVariable("productId") Integer productId,
                         HttpServletRequest request) throws BusinessException {
        User user = (User) request.getSession().getAttribute(Const.CURRENT_USER_SESSION);
        if(user == null){
            throw new BusinessException(EmCommonError.USER_NOT_LOGIN);
        }
        return CommonRes.create(cartService.update(user.getId(),productId,cartUpdateReq));
    }
    @DeleteMapping("/{productId}")
    public CommonRes delete(@PathVariable("productId") Integer productId,
                            HttpServletRequest request) throws BusinessException {
        User user = (User) request.getSession().getAttribute(Const.CURRENT_USER_SESSION);
        if(user == null){
            throw new BusinessException(EmCommonError.USER_NOT_LOGIN);
        }
        return CommonRes.create(cartService.delete(user.getId(),productId));
    }
    @PutMapping("/selectAll")
    public CommonRes selectAll(HttpServletRequest request) throws BusinessException {
        User user = (User) request.getSession().getAttribute(Const.CURRENT_USER_SESSION);
        if(user == null){
            throw new BusinessException(EmCommonError.USER_NOT_LOGIN);
        }
        return CommonRes.create(cartService.selectAll(user.getId()));
    }
    @PutMapping("/unSelectAll")
    public CommonRes unSelectAll(HttpServletRequest request) throws BusinessException {
        User user = (User) request.getSession().getAttribute(Const.CURRENT_USER_SESSION);
        if(user == null){
            throw new BusinessException(EmCommonError.USER_NOT_LOGIN);
        }
        return CommonRes.create(cartService.unSelectAll(user.getId()));
    }
    @GetMapping("/quantity")
    public CommonRes sum(HttpServletRequest request) throws BusinessException {
        User user = (User) request.getSession().getAttribute(Const.CURRENT_USER_SESSION);
        if(user == null){
            throw new BusinessException(EmCommonError.USER_NOT_LOGIN);
        }
        Map<String,Integer> map = new HashMap<>();
        map.put("sum",cartService.sum(user.getId()));
        return CommonRes.create(map);
    }
}
