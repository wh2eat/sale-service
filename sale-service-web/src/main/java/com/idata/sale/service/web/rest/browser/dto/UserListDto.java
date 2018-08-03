package com.idata.sale.service.web.rest.browser.dto;

import com.idata.sale.service.web.base.dao.dbo.SystemUserDbo;

public class UserListDto {

    private String fieldName;

    private String fieldValue;

    private String udid;

    private SystemUserDbo user;

    private String userType;

    private String repairStationId;

    public UserListDto() {
    }

    public SystemUserDbo getUser() {
        return user;
    }

    public void setUser(SystemUserDbo user) {
        this.user = user;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    @Override
    public String toString() {
        return "UserListDto [fieldName=" + fieldName + ", fieldValue=" + fieldValue + ", udid=" + udid + ", user="
                + user + ", userType=" + userType + ", repairStationId=" + repairStationId + "]";
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getRepairStationId() {
        return repairStationId;
    }

    public void setRepairStationId(String repairStationId) {
        this.repairStationId = repairStationId;
    }

}
