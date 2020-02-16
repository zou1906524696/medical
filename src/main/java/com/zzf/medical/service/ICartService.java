package com.zzf.medical.service;

import com.zzf.medical.exception.BusinessException;
import com.zzf.medical.pojo.Cart;
import com.zzf.medical.request.CartAddReq;
import com.zzf.medical.request.CartUpdateReq;
import com.zzf.medical.vo.CartVo;

import java.util.List;

public interface ICartService {
    CartVo add(Integer uid,CartAddReq cartAddReq) throws BusinessException;
    CartVo list(Integer uid);
    CartVo update(Integer uid, Integer productId, CartUpdateReq cartUpdateReq) throws BusinessException;
    CartVo delete(Integer uid,Integer productId) throws BusinessException;
    CartVo selectAll(Integer uid);
    CartVo unSelectAll(Integer uid);
    int sum(Integer uid);
    List<Cart> listForCart(Integer uid);
}
