package com.zzf.medical.service;

import com.zzf.medical.vo.CategoryVo;

import java.util.List;
import java.util.Set;

public interface ICategoryService {
    List<CategoryVo> selectAll();
    void findSubCategoryId(Integer id, Set<Integer> resultSet);
}
