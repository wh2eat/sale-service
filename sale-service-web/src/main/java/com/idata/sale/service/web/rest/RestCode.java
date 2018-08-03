package com.idata.sale.service.web.rest;

public enum RestCode {

    SuccessPage(0, "SuccessPageResult"), Success(100, ""),

    ServiceException(400, "Bussiness Service Exception"),

    UnknownException(500, "Unknown exception"), FieldIsEmpty(501, "Field is emtpy"), ObjectNotFound(502,
            "Object not found"), DeviceApiError(503, "Device api not found"), TokenLoseError(504,
                    "not found token"), TokenTimeoutError(505,
                            "token timeout"), FieldValueNotSupport(506, "Field value not support"),

    UserPasswordError(601, "user password error"), UserNotAllowLoginError(602, "user not allow login error"),

    RepairDeviceStatusError(701, "repair device status error");

    private int code;

    private String desc;

    private RestCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    @Override
    public String toString() {
        return "RestCode [code=" + code + ", desc=" + desc + "]";
    }

}
