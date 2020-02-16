package com.zzf.medical.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zzf.medical.exception.BusinessException;
import com.zzf.medical.request.CartAddReq;
import com.zzf.medical.vo.CartVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ICartServiceTest {

    @Autowired
    private ICartService cartService;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void add() throws BusinessException {
        CartAddReq cartAddReq = new CartAddReq();
        cartAddReq.setProductId(29);
        cartAddReq.setSelected(true);
        cartService.add(1,cartAddReq);
    }
    @Test
    public void list(){
        CartVo list = cartService.list(1);
        log.info("list={}" ,gson.toJson(list));
    }
    @Test
    public void selectAll() {
        CartVo list = cartService.selectAll(1);
        log.info("list={}" ,gson.toJson(list));
    }
    @Test
    public void unSelectAll() {
        CartVo list = cartService.unSelectAll(1);
        log.info("list={}" ,gson.toJson(list));
    }

    @Test
    public void sum() {
        Integer sum = cartService.sum(1);
        log.info("sum={}",sum);
    }
}