package com.zzf.medical.service.serviceImpl;

import com.zzf.medical.dao.UserMapper;
import com.zzf.medical.enums.EmCommonError;
import com.zzf.medical.enums.RoleEnum;
import com.zzf.medical.exception.BusinessException;
import com.zzf.medical.pojo.User;
import com.zzf.medical.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public User register(User user) throws BusinessException {
        //用户名不能重复
        int count = userMapper.countByUsername(user.getUsername());
        if(count > 0){
            throw new BusinessException(EmCommonError.USERNAME_ALREADY_REGISTER);
        }
        //邮箱不能重复
        count = userMapper.countByEmail(user.getEmail());
        if(count > 0 ){
            throw new BusinessException(EmCommonError.EMAIL_ALREADY_REGISTER);
        }
        //MD5 摘要算法(Spring自带)
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8)));
        //写入数据库
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setRole(RoleEnum.COSTOMER.getCode());
        count = userMapper.insertSelective(user);
        if (count != 1){
            throw new BusinessException(EmCommonError.REGISTER_FAIL);
        }
        return user;
    }

    @Override
    public User login(String username, String password) throws BusinessException {
        //用户名不能重复
        User user = userMapper.selectByUsername(username);
        if(user == null){
            throw new BusinessException(EmCommonError.USERNAME_OR_PASSWORD_ERROR);
        }
        if(!user.getPassword().equalsIgnoreCase(
                DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)))){
            throw new BusinessException(EmCommonError.USERNAME_OR_PASSWORD_ERROR);
        }
        return user;
    }

    @Override
    public User getUserByToken(String token) {
        return userMapper.selectByToken(token);
    }

    @Override
    public void updateByPrimaryKey(User user) {
        userMapper.updateByPrimaryKey(user);
    }
}
