package com.zzf.medical.service.serviceImpl;

import com.zzf.medical.exception.BusinessException;
import com.zzf.medical.pojo.User;
import com.zzf.medical.service.IUserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private IUserService userService;
    @Test
    void register() throws BusinessException {
        User user = new User();
        user.setUsername("test2");
        user.setPassword("123456");
        user.setEmail("2@qq.com");
        userService.register(user);
    }

    @Test
    void login() {
    }
}