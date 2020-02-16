package com.zzf.medical.common;

import com.zzf.medical.enums.EmCommonError;
import lombok.Data;

@Data
public class CommonError {
    private Integer errCode;
    private String errMsg;

    public CommonError(EmCommonError emBusinessError){
        this.errCode = emBusinessError.getErrCode();
        this.errMsg = emBusinessError.getErrMsg();
    }
}
