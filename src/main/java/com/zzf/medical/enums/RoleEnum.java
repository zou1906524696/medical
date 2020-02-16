package com.zzf.medical.enums;

public enum RoleEnum {
    ADMIN(0),
    COSTOMER(1);

    Integer code;

    RoleEnum(Integer code){
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
