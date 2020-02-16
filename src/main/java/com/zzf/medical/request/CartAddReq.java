package com.zzf.medical.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CartAddReq {

    @NotNull
    @ApiModelProperty("商品ID")
    private Integer productId;
    @ApiModelProperty("购物车中是否被选中")
    private Boolean selected = true;
}
