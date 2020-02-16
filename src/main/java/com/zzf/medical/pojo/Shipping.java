package com.zzf.medical.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class Shipping implements Serializable {
    private Integer id;

    private Integer userId;

    private String receiverName;

    private String receiverPhone;

    private String receiverMobile;

    private String receiverProvince;

    private String receiverCity;

    private String receiverDistrict;

    private String receiverAddress;

    private String receiverZip;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}