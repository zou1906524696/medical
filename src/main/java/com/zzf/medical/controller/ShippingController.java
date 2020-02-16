package com.zzf.medical.controller;

import com.zzf.medical.common.CommonRes;
import com.zzf.medical.common.Const;
import com.zzf.medical.exception.BusinessException;
import com.zzf.medical.pojo.User;
import com.zzf.medical.request.ShippingReq;
import com.zzf.medical.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class ShippingController {

    @Autowired
    private IShippingService shippingService;

    @PostMapping("/shippings")
    public CommonRes add(@Valid @RequestBody ShippingReq shippingReq,
                         HttpServletRequest request) throws BusinessException {
        User user = (User) request.getSession().getAttribute(Const.CURRENT_USER_SESSION);
        return CommonRes.create(shippingService.add(user.getId(),shippingReq));
    }

    @DeleteMapping("/shippings/shippingId")
    public CommonRes delete(@PathVariable Integer shippingId,
                            HttpServletRequest request) throws BusinessException {
        User user = (User) request.getSession().getAttribute(Const.CURRENT_USER_SESSION);
        shippingService.delete(user.getId(),shippingId);
        return CommonRes.create("删除收货地址成功");
    }

    @PutMapping("/shippings/shippingId")
    public CommonRes update(@PathVariable Integer shippingId,
                            @Valid @RequestBody ShippingReq shippingReq,
                            HttpServletRequest request) throws BusinessException {
        User user = (User) request.getSession().getAttribute(Const.CURRENT_USER_SESSION);
        shippingService.update(user.getId(),shippingId,shippingReq);
        return CommonRes.create("更新收货地址成功");
    }

    @GetMapping("/shippings")
    public CommonRes list(@RequestParam(required = false,defaultValue = "1") Integer pageNum,
                          @RequestParam(required = false,defaultValue = "1") Integer pageSize,
                          HttpServletRequest request){
        User user = (User) request.getSession().getAttribute(Const.CURRENT_USER_SESSION);
        return CommonRes.create(shippingService.list(user.getId(),pageNum,pageSize));
    }

}
