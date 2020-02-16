package com.zzf.medical.common;

public class Const {
    public static final String CURRENT_USER_SESSION = "currentUserSession";

    public static Integer ROOT_PARENT_ID = 0;
    public interface Role{
        int ROLE_CUSTOMER = 0; //普通用户
        int ROLE_ADMIN = 1;//管理员
    }
}
