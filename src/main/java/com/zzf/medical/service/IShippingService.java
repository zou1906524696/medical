package com.zzf.medical.service;

import com.github.pagehelper.PageInfo;
import com.zzf.medical.exception.BusinessException;
import com.zzf.medical.request.ShippingReq;

import java.util.Map;

public interface IShippingService {
    /**
     * 新建地址
     * */
    Map<String,Integer> add(Integer uid, ShippingReq shippingReq) throws BusinessException;

    /**
     * 删除地址
     * **/
    void delete(Integer uid,Integer shippingId) throws BusinessException;

    /**
     * 更新地址
     * */
    void update(Integer uid,Integer shippingId,ShippingReq shippingReq) throws BusinessException;

    /**
     * 列表
     * **/
    PageInfo list(Integer uid,Integer pageNum,Integer pageSize);
}
