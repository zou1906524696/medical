package com.zzf.medical.dao;

import com.zzf.medical.pojo.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface ProductMapper {
    int insert(Product record);

    int insertSelective(Product record);

    List<Product> selectByCategoryIdSet(@Param("categoryIdSet") Set<Integer> categoryIdSet);

    List<Product> selectByCartIdSet(@Param("cartIdSet") Set<String> cartIdSet);

    List<Product> selectByProductIdSet(@Param("productIdSet") Set<Integer> productIdSet);

    Product selectByPrimaryKey(Integer productId);

    int updateByPrimaryKeySelective(Product record);
}