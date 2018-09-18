package com.idata.sale.service.web.base.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.idata.sale.service.web.base.dao.BaseDao;
import com.idata.sale.service.web.base.dao.BaseDaoException;
import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.constant.SystemExportTaskStatus;
import com.idata.sale.service.web.base.dao.dbo.SystemExportTaskDbo;
import com.idata.sale.service.web.base.dao.fo.SystemExportTaskFo;
import com.idata.sale.service.web.util.TimeUtil;

@Repository
public class SystemExportTaskDao extends BaseDao<SystemExportTaskDbo> {

    private final static Logger LOGGER = LoggerFactory.getLogger(SystemExportTaskDao.class);

    public SystemExportTaskDao() {
    }

    public void exportCompleted(int id, String storePath) {
        updateExportStatus(id, SystemExportTaskStatus.Completed, storePath);
    }

    public void exportFailed(int id) {
        updateExportStatus(id, SystemExportTaskStatus.Failed);
    }

    public void exportStart(int id) {
        updateExportStatus(id, SystemExportTaskStatus.Exporting);
    }

    private void updateExportStatus(int id, SystemExportTaskStatus status) {
        updateExportStatus(id, status, null);
    }

    private void updateExportStatus(int id, SystemExportTaskStatus status, String storePath) {

        List<Object> values = new ArrayList<>((null == storePath ? 3 : 4));
        String updateSql = "update " + getTableName() + " set status=?";
        values.add(status.code);
        if (StringUtils.isNotBlank(storePath)) {
            updateSql += ",store_path=?";
            values.add(storePath);
        }
        updateSql += ",update_time=? where id=?";
        values.add(TimeUtil.getNowTimeStr());
        values.add(id);
        try {
            update(updateSql, values.toArray());
        }
        finally {
            updateSql = null;
            values.clear();
            values = null;
        }
    }

    public List<SystemExportTaskDbo> findList(SystemExportTaskFo filter, PageInfo pageInfo) throws BaseDaoException {

        String sql = "select " + getAllCloumnStrWithComma() + " from " + getTableName() + " where 1=1 ";

        List<Object> values = new ArrayList<>();

        if (null != filter) {
            Integer userId = filter.getUserId();
            if (null != userId) {
                sql += " and user_id=?";
                values.add(userId);
            }

            String taskName = filter.getTaskName();
            if (StringUtils.isNotEmpty(taskName)) {
                sql += " and locate(?,task_name)>0";
                values.add(taskName);
            }

            String remark = filter.getRemark();
            if (StringUtils.isNotEmpty(remark)) {
                sql += " and locate(?,remark)>0";
                values.add(remark);
            }

            String createTime = filter.getCreateTime();
            if (StringUtils.isNotEmpty(createTime)) {
                sql += " and create_time>=? and create_time<=?";
                values.add(createTime + " 00:00:00");
                values.add(createTime + " 23:59:59");
            }
        }

        sql += " order by create_time desc";

        try {
            return findListByPage(sql, values.toArray(), pageInfo);
        }
        finally {
            sql = null;
            values.clear();
            values = null;
        }
    }

    public SystemExportTaskDbo getByDownloadId(String downloadId) {
        String sql = "select " + getAllCloumnStrWithComma() + " from " + getTableName() + " where download_id='"
                + downloadId + "'";
        List<SystemExportTaskDbo> dbos = findList(sql, null);
        if (CollectionUtils.isEmpty(dbos)) {
            return null;
        }
        return dbos.get(0);
    }

}
