package com.idata.sale.service.web.base.dao;

public enum BaseDaoCode {

    dao_exception(1000, "Dao Exception")

    , bean_field_mapping_failed(1001, "Bean Field Mapping failed"), bean_mapper_not_found_exception(1002, "Bean Mapper Not Found Exception")

    , bean_duplicate_entry_exception(1003, "bean_duplicate_entry_exception");

    private int code;

    private String desc;

    private BaseDaoCode(int code, String desc) {
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
