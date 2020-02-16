package com.zzf.medical.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CommonRes {
    @ApiModelProperty("请求状态")
    private String status;
    @ApiModelProperty("数据")
    private Object data;

    public static CommonRes create(Object result){
        return CommonRes.create(result,"success");
    }

    public static CommonRes create(Object result, String status) {
        CommonRes commonRes = new CommonRes();
        commonRes.setStatus(status);
        commonRes.setData(result);
        return commonRes;
    }
}
