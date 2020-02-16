package com.zzf.medical.service;

import com.zzf.medical.exception.BusinessException;
import com.zzf.medical.pojo.User;

public interface IUserService {
    /**
     * 注册
     * */
    User register(User user) throws BusinessException;
    /**
     * 登录
     * */
    User login(String username,String password) throws BusinessException;
    /**
     * 获取用户信息
     * */
    User getUserByToken(String token);
    /**
     * 更新
     * **/
    void updateByPrimaryKey(User user);
}
