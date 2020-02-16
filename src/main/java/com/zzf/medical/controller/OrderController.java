package com.zzf.medical.controller;

import com.zzf.medical.common.CommonRes;
import com.zzf.medical.common.Const;
import com.zzf.medical.enums.EmCommonError;
import com.zzf.medical.exception.BusinessException;
import com.zzf.medical.pojo.User;
import com.zzf.medical.request.OrderCreateForm;
import com.zzf.medical.service.IOrderService;
import com.zzf.medical.vo.OrderVo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/order")
@Api(tags = "订单控制类")
@Slf4j
public class OrderController {
    @Autowired
    private IOrderService orderService;

    @PostMapping("/create")
    public CommonRes create(@Valid @RequestBody OrderCreateForm form,
            HttpServletRequest request) throws BusinessException {
        User user = (User) request.getSession().getAttribute(Const.CURRENT_USER_SESSION);
        if(user == null){
            throw new BusinessException(EmCommonError.USER_NOT_LOGIN);
        }
        OrderVo orderVo = orderService.create(user.getId(), form.getShippingId());
        return CommonRes.create(orderVo);
    }

    @GetMapping("/orders")
    public CommonRes list(@RequestParam Integer pageNum,
                          @RequestParam Integer pageSize,
                          HttpServletRequest request) throws BusinessException {
        User user = (User) request.getSession().getAttribute(Const.CURRENT_USER_SESSION);
        if(user == null){
            throw new BusinessException(EmCommonError.USER_NOT_LOGIN);
        }
        return CommonRes.create(orderService.list(user.getId(),pageNum,pageSize));
    }

    @GetMapping("/orders/{orderNo}")
    public CommonRes detail(@PathVariable Long orderNo,
                          HttpServletRequest request) throws BusinessException {
        User user = (User) request.getSession().getAttribute(Const.CURRENT_USER_SESSION);
        if(user == null){
            throw new BusinessException(EmCommonError.USER_NOT_LOGIN);
        }
        return CommonRes.create(orderService.detail(user.getId(),orderNo));
    }

    @PutMapping("/orders/{orderNo}")
    public CommonRes cancel(@PathVariable Long orderNo,
            HttpServletRequest request) throws BusinessException {
        User user = (User) request.getSession().getAttribute(Const.CURRENT_USER_SESSION);
        if(user == null){
            throw new BusinessException(EmCommonError.USER_NOT_LOGIN);
        }
        orderService.cancel(user.getId(),orderNo);
        return CommonRes.create(null);
    }
}
