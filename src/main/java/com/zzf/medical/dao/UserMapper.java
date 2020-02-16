package com.zzf.medical.dao;

import com.zzf.medical.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    /**
     * 确认用户名的唯一性
     */
    int countByUsername(String username);
    /**
     * 确认邮箱的唯一性
     */
    int countByEmail(String username);
    /**
     *
     * **/
    User selectByToken(String token);
    /**
     * 登录
     * **/
    User selectByUsername(String username);
}