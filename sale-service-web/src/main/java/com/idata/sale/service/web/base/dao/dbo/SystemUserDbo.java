package com.idata.sale.service.web.base.dao.dbo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.idata.sale.service.web.base.dao.annotation.Column;
import com.idata.sale.service.web.base.dao.annotation.Table;
import com.idata.sale.service.web.base.dao.constant.SystemUserType;

@Table("system_user")
public class SystemUserDbo {

    @JSONField(serialize = false)
    private Integer id;

    @Column
    private Integer supportManager;

    @JSONField(name = "id")
    @Column()
    private String udid;

    @Column()
    private String loginName;

    @Column()
    private String userName;

    @Column()
    private String telephone;

    @JSONField(serialize = false)
    @Column()
    private Integer type;

    @JSONField(name = "type")
    private String typeStr;

    @Column()
    private String email;

    @JSONField(serialize = false)
    @Column()
    private String password;

    @Column()
    @JSONField(serialize = false)
    private Date createTime;

    @Column()
    @JSONField(serialize = false)
    private Date updateTime;

    @Column()
    private Date lastLoginTime;

    @Column
    private Integer repairStationId;

    private SystemRepairStationDbo repairStation;

    public Integer getRepairStationId() {
        return repairStationId;
    }

    public void setRepairStationId(Integer repairStationId) {
        this.repairStationId = repairStationId;
    }

    public SystemUserDbo() {
        // TODO Auto-generated constructor stub
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getTypeStr() {
        return SystemUserType.getName(null == type ? -1 : type.intValue());
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    public SystemRepairStationDbo getRepairStation() {
        return repairStation;
    }

    public void setRepairStation(SystemRepairStationDbo repairStation) {
        this.repairStation = repairStation;
    }

    @Override
    public String toString() {
        return "SystemUserDbo [id=" + id + ", udid=" + udid + ", loginName=" + loginName + ", userName=" + userName
                + ", telephone=" + telephone + ", type=" + type + ", typeStr=" + typeStr + ", email=" + email
                + ", password=" + password + ", createTime=" + createTime + ", updateTime=" + updateTime
                + ", lastLoginTime=" + lastLoginTime + ", repairStationId=" + repairStationId + ", repairStation="
                + repairStation + "]";
    }

    public Integer getSupportManager() {
        return supportManager;
    }

    public void setSupportManager(Integer supportManager) {
        this.supportManager = supportManager;
    }

}