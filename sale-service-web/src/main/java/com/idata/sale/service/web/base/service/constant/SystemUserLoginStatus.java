package com.idata.sale.service.web.base.service.constant;

public enum SystemUserLoginStatus {

    Success(1, "loginSuccess"), Failed_Password_Error(2, "passwordError"), Failed_Unsupport_Error(3, "unsupportError");
    ;

    private int code;

    private String desc;

    private SystemUserLoginStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
