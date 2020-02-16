package com.zzf.medical.controller;

import com.zzf.medical.common.CommonRes;
import com.zzf.medical.exception.BusinessException;
import com.zzf.medical.service.IProductService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "商品控制类")
public class ProductController {
    @Autowired
    private IProductService productService;
    @GetMapping("/products")
    public CommonRes list(@RequestParam(required = false) Integer categoryId,
                          @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                          @RequestParam(required = false, defaultValue = "10") Integer pageSize ){
        return CommonRes.create(productService.list(categoryId,pageNum,pageSize));
    }
    @GetMapping("/products/{productId}")
    public CommonRes detail(@PathVariable("productId") Integer productId ) throws BusinessException {
        return CommonRes.create(productService.detail(productId));
    }
}
