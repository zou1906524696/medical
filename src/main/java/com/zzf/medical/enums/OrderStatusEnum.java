package com.zzf.medical.enums;

import lombok.Getter;

@Getter
public enum OrderStatusEnum {
    CANCELED(0,"已取消"),

    NO_PAY(10,"未支付"),

    PAID(20,"已支付"),

    SHIPPED(40,"已发货"),

    TRADE_SUCCESS(50,"交易成功"),

    TRADE_CLOSE(60,"交易关闭"),

    ;
    private Integer code;
    private String desc;

    OrderStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
