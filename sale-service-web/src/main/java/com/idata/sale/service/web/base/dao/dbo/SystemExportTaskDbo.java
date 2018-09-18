package com.idata.sale.service.web.base.dao.dbo;

import java.util.Date;

import com.idata.sale.service.web.base.dao.annotation.Column;
import com.idata.sale.service.web.base.dao.annotation.Table;

@Table("system_export_task")
public class SystemExportTaskDbo {

    private Integer id;

    @Column
    private String taskName;

    @Column
    private String taskType;

    @Column
    private String downloadId;

    @Column
    private String downloadFileName;

    @Column
    private String storePath;

    @Column
    private Integer status;

    @Column
    private Integer userId;

    @Column
    private Date updateTime;

    @Column
    private Date createTime;

    @Column
    private String remark;

    public SystemExportTaskDbo() {
        // TODO Auto-generated constructor stub
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(String downloadId) {
        this.downloadId = downloadId;
    }

    public String getDownloadFileName() {
        return downloadFileName;
    }

    public void setDownloadFileName(String downloadFileName) {
        this.downloadFileName = downloadFileName;
    }

    public String getStorePath() {
        return storePath;
    }

    public void setStorePath(String storePath) {
        this.storePath = storePath;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SystemExportTaskDbo [id=" + id + ", taskName=" + taskName + ", taskType=" + taskType + ", downloadId="
                + downloadId + ", downloadFileName=" + downloadFileName + ", storePath=" + storePath + ", status="
                + status + ", userId=" + userId + ", updateTime=" + updateTime + ", createTime=" + createTime
                + ", remark=" + remark + "]";
    }

}
