package com.zzf.medical.enums;
/**
 * 统一返回异常的所有错误码
 * */
public enum EmCommonError {

    //用户模块
    USERNAME_ALREADY_REGISTER(50001,"用户名已经注册"),
    EMAIL_ALREADY_REGISTER(50002,"邮箱已经注册"),
    REGISTER_FAIL(50003,"注册失败"),
    USERNAME_OR_PASSWORD_ERROR(50004,"用户名或密码错误"),

    //系统
    CLEAR_COOKIES_ERROR(10007,"清空Cookies发生异常"),
    SERVICE_ERROR(10008,"服务器异常"),

    //商品模块
    PRODUCT_OFF_SALE_OR_DELETE(20001,"商品下架或者删除"),
    PRODUCT_NOT_EXIST(20002,"商品不存在"),
    PRODUCT_STOCK_ERROR(20003,"库存不正确"),
    CART_PRODUCT_NOT_EXIST(20004,"购物车中无此商品"),

    //收货地址模块
    DELETE_SHIPPING_ADDRESS_ERROR(30001,"删除收货地址失败"),
    SHIPPING_ADDRESS_NOT_EXIST(30002,"收货地址不存在"),

    //购物车模块
    CART_SELECTED_IS_EMPTY(40001,"请选择商品后下单"),

    //订单模块
    ORDER_INSERT_ERROR(50001,"订单插入数据库错误"),
    ORDER_NOT_EXIST(50002,"订单不存在"),
    ORDER_STATUS_ERROR(50003,"订单状态有误"),

    NO_OBJECT_FOUND(10001,"请求对象不存在"),
    UNKNOWN_ERROR(10002,"未知错误"),
    NO_HANDLER_FOUND(10003,"找不到执行的路径操作"),
    BIND_EXCEPTION_ERROR(10004,"请求参数错误"),
    PARAMETER_VALIDATION_ERROR(10005,"请求参数校验失败"),
    MYSQL_FORMAT_ERROR(10006,"请求格式不正确"),

    PERMISSION_ERROR(10008,"权限限制,或许你应该先登录"),
    TOKEN_ERROR(10009,"没有Token,请登录试试"),
    REQUEST_METHOD_NOT_SUPPORTED(10010,"请确认请求方式，当前不是GET方法"),
    USER_NOT_LOGIN(10011,"用户未登录"),
    ;

    private Integer errCode;
    private String errMsg;
    EmCommonError(Integer errCode, String errMsg){
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
