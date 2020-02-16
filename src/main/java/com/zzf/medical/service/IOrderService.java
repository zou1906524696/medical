package com.zzf.medical.service;

import com.github.pagehelper.PageInfo;
import com.zzf.medical.exception.BusinessException;
import com.zzf.medical.vo.OrderVo;

public interface IOrderService {
    OrderVo create(Integer uid,Integer shippingId) throws BusinessException;
    PageInfo list(Integer uid,Integer pageNum,Integer pageSize);
    OrderVo detail(Integer uid,Long orderNo) throws BusinessException;
    void cancel(Integer uid,Long orderNo) throws BusinessException;
    void paid(Long orderNo) throws BusinessException;
}
