package com.zzf.medical.dao;

import com.zzf.medical.pojo.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    int insert(Category record);

    int insertSelective(Category record);

    List<Category> selectAll();
}