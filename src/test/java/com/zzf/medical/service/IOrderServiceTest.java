package com.zzf.medical.service;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zzf.medical.exception.BusinessException;
import com.zzf.medical.vo.OrderVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class IOrderServiceTest {

    @Autowired
    private IOrderService orderService;

    private Integer uid = 1;

    private Integer shippingId = 4 ;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void create() throws BusinessException {
        OrderVo orderVo = orderService.create(uid, shippingId);
        log.info("redult={}" + gson.toJson(orderVo));
    }
    @Test
    public void list(){
        PageInfo list = orderService.list(uid, 1, 5);
        log.info("redult={}" + gson.toJson(list));
    }
}