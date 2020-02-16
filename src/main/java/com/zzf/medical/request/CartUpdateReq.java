package com.zzf.medical.request;

import lombok.Data;

@Data
public class CartUpdateReq {
    private Integer quantity;
    private Boolean selected;
}
