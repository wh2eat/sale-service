package com.idata.sale.service.web.base.service.constant;

public enum ServiceCode {

    system_db_exception("system_db_exception"), system_param_error("system_param_error"), system_busy_error(
            "system_busy")

    , system_object_busy_error("system_object_busy"), system_object_not_exist_error("system_object_not_exist"), system_object_exist_error("system_object_exist"), system_object_has_relation_error("system_object_has_relation"), system_object_uk_error("system_object_uk"),

    repair_device_status_error("repair_device_status_error"),

    user_not_fonud("user_not_fonud"), user_loginName_exist("user_loginName_exist"), user_password_error(
            "user_password_error"),

    repair_invoice_field_is_empty("repair_invoice_field_is_empty"), repair_invoice_not_found(
            "repair_invoice_not_found"), repair_invoice_param_error("repair_invoice_param_error");

    private String code;

    private ServiceCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
