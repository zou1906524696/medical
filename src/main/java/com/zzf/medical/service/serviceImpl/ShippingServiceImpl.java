package com.zzf.medical.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzf.medical.dao.ShippingMapper;
import com.zzf.medical.enums.EmCommonError;
import com.zzf.medical.exception.BusinessException;
import com.zzf.medical.pojo.Shipping;
import com.zzf.medical.request.ShippingReq;
import com.zzf.medical.service.IShippingService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class ShippingServiceImpl implements IShippingService {
    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public Map<String,Integer> add(Integer uid, ShippingReq shippingReq) throws BusinessException {
        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(shippingReq,shipping);
        shipping.setUserId(uid);
        shipping.setCreateTime(new Date());
        shipping.setUpdateTime(new Date());
        int row = shippingMapper.insertSelective(shipping);
        if(row == 0){
            throw new BusinessException(EmCommonError.SERVICE_ERROR);
        }
        Map<String,Integer> map = new HashMap<>();
        map.put("shippingId",shipping.getId());
        return map;
    }

    @Override
    public void delete(Integer uid, Integer shippingId) throws BusinessException {
        int row = shippingMapper.deleteByIdAndUid(uid, shippingId);
        if (row == 0){
            throw new BusinessException(EmCommonError.DELETE_SHIPPING_ADDRESS_ERROR);
        }
    }

    @Override
    public void update(Integer uid, Integer shippingId, ShippingReq shippingReq) throws BusinessException {
        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(shippingReq,shipping);
        shipping.setUserId(uid);
        shipping.setId(shippingId);
        shipping.setUpdateTime(new Date());
        int row = shippingMapper.updateByPrimaryKeySelective(shipping);
        if (row == 0){
            throw new BusinessException(EmCommonError.NO_OBJECT_FOUND);
        }
    }

    @Override
    public PageInfo list(Integer uid, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUid(uid);
        PageInfo pageInfo = new PageInfo(shippingList);
        return pageInfo;
    }
}
