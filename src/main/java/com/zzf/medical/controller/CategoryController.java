package com.zzf.medical.controller;

import com.zzf.medical.common.CommonRes;
import com.zzf.medical.service.ICategoryService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "类目控制类")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/categories")
    public CommonRes selectAll(){
        return CommonRes.create(categoryService.selectAll());
    }
}
