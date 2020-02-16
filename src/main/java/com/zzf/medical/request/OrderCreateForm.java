package com.zzf.medical.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrderCreateForm {
    @NotNull
    private Integer shippingId;
}
