package com.zzf.medical.listener;

import com.google.gson.Gson;
import com.zzf.medical.exception.BusinessException;
import com.zzf.medical.pojo.PayInfo;
import com.zzf.medical.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 关于payInfo正确应该是pay项目提供，mall项目引入jar
 * **/
@Component
@RabbitListener(queues = "payNotify")
@Slf4j
public class PayMsgListener {
    @Autowired
    private IOrderService orderService;
    @RabbitHandler
    public void process(String msg) throws BusinessException {
        log.info("接收到消息 => {}"+ msg);
        PayInfo payInfo = new Gson().fromJson(msg, PayInfo.class);
        if (payInfo.getPlatformStatus().equals("SUCCESS")){
            //修改订单状态
            orderService.paid(payInfo.getOrderNo());
        }
    }
}
