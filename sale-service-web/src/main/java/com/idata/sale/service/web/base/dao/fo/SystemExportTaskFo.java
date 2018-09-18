package com.idata.sale.service.web.base.dao.fo;

public class SystemExportTaskFo {

    private Integer userId;

    private String taskName;

    private String remark;

    private String createTime;

    public SystemExportTaskFo() {
        // TODO Auto-generated constructor stub
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SystemExportTaskFo [userId=" + userId + ", taskName=" + taskName + ", remark=" + remark
                + ", createTime=" + createTime + "]";
    }

}
