package com.zzf.medical.exception;

import com.zzf.medical.common.CommonError;
import com.zzf.medical.enums.EmCommonError;

public class BusinessException extends Exception{
    private CommonError commonErr;

    public BusinessException(EmCommonError emBusinessError){
        super();
        this.commonErr = new CommonError(emBusinessError);
    }
    public BusinessException(EmCommonError emBusinessError, String errMsg){
        super();
        this.commonErr = new CommonError(emBusinessError);
        this.commonErr.setErrMsg(errMsg);
    }

    public CommonError getCommonErr() {
        return commonErr;
    }
}
