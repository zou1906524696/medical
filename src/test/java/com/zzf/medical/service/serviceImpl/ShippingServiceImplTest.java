package com.zzf.medical.service.serviceImpl;


import com.github.pagehelper.PageInfo;
import com.zzf.medical.exception.BusinessException;
import com.zzf.medical.request.ShippingReq;
import com.zzf.medical.service.IShippingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ShippingServiceImplTest {

    @Autowired
    private IShippingService shippingService;

    private Integer uid =1;

    @Test
    public void add() throws BusinessException {
        ShippingReq shippingReq = new ShippingReq();
        shippingReq.setReceiverName("邹志峰");
        shippingReq.setReceiverAddress("华庄");
        shippingReq.setReceiverCity("无锡");
        shippingReq.setReceiverPhone("13970979081");
        shippingReq.setReceiverMobile("13970979081");
        Map<String,Integer> map = shippingService.add(uid,shippingReq);
        log.info("map={}",map);
    }

    @Test
    public void delete() throws BusinessException {
        Integer shippingId = 9;
        shippingService.delete(uid,shippingId);
    }

    @Test
    public void update() throws BusinessException {
        Integer shippingId = 8;
        ShippingReq shippingReq = new ShippingReq();
        shippingReq.setReceiverName("邹志峰");
        shippingReq.setReceiverAddress("华庄");
        shippingReq.setReceiverCity("南昌");
        shippingReq.setReceiverPhone("13970979081");
        shippingReq.setReceiverMobile("13970979081");
        shippingService.update(uid,shippingId,shippingReq);
    }

    @Test
    public void list() {
        PageInfo pageInfo = shippingService.list(uid,1,2);
        log.info("page={}" + pageInfo);
    }
}