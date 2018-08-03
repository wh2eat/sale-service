package com.idata.sale.service.web.controller;

public enum ControllerErrorCode {

    unknown_exception(100000, "unknown exception"), not_found_obj(100001, "not found obj"), field_is_empty(100002,
            "field is empty"), user_password_error(100101,
                    "password error"), user_not_allow_login_error(100102, "user not allow login by web");

    private int code;

    private String desc;

    private ControllerErrorCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
