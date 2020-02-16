package com.zzf.medical.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ShippingReq {
    @NotBlank
    @ApiModelProperty("收货人姓名")
    private String receiverName;
    @NotBlank
    @ApiModelProperty("收货人电话")
    private String receiverPhone;
    @NotBlank
    @ApiModelProperty("收货人手机号")
    private String receiverMobile;
    @NotBlank
    @ApiModelProperty("收货人省份")
    private String receiverProvince;

    @NotBlank
    @ApiModelProperty("收货人城市")
    private String receiverCity;

    @NotBlank
    @ApiModelProperty("收货人区")
    private String receiverDistrict;

    @NotBlank
    @ApiModelProperty("收货人详细地址")
    private String receiverAddress;

    @NotBlank
    @ApiModelProperty("邮编")
    private String receiverZip;
}
