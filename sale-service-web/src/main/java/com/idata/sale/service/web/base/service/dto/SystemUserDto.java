package com.idata.sale.service.web.base.service.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.idata.sale.service.web.base.dao.dbo.SystemRepairStationDbo;
import com.idata.sale.service.web.base.dao.dbo.SystemUserDbo;
import com.idata.sale.service.web.base.service.constant.SystemUserLoginStatus;

public class SystemUserDto {

    @JSONField(serialize = false)
    private SystemUserLoginStatus loginStatus;

    @JSONField(name = "user")
    private SystemUserDbo dbo;

    @JSONField(serialize = false)
    private String loginName;

    @JSONField(serialize = false)
    private String password;

    @JSONField(serialize = false)
    private Integer keepSession;

    private String token;

    private SystemRepairStationDbo repairStation;

    public SystemUserDto() {

    }

    public SystemUserDbo getDbo() {
        return dbo;
    }

    public void setDbo(SystemUserDbo dbo) {
        this.dbo = dbo;
    }

    public SystemUserLoginStatus getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(SystemUserLoginStatus loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getKeepSession() {
        return keepSession;
    }

    public void setKeepSession(Integer keepSession) {
        this.keepSession = keepSession;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public SystemRepairStationDbo getRepairStation() {
        return repairStation;
    }

    public void setRepairStation(SystemRepairStationDbo repairStation) {
        this.repairStation = repairStation;
    }

    @Override
    public String toString() {
        return "SystemUserDto [loginStatus=" + loginStatus + ", dbo=" + dbo + ", loginName=" + loginName + ", password="
                + password + ", keepSession=" + keepSession + ", token=" + token + ", repairStation=" + repairStation
                + "]";
    }

}
