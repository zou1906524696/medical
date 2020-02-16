package com.zzf.medical.service;

import com.github.pagehelper.PageInfo;
import com.zzf.medical.exception.BusinessException;
import com.zzf.medical.vo.ProductDetailVo;

public interface IProductService {
    PageInfo list(Integer categoryId, Integer pageNum, Integer pageSize);
    ProductDetailVo detail(Integer productId) throws BusinessException;
}
