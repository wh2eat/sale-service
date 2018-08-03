package com.idata.sale.service.web.base.dao;

import java.util.List;

public interface IBaseDao<T> {
    // 保存
    public long save(T t) throws BaseDaoException;

    public void batchSave(List<T> list) throws BaseDaoException;

    // 更新
    public int update(T t) throws BaseDaoException;

    public Integer batchUpdate(List<T> beans) throws BaseDaoException;

    // 删除
    public int delete(T t) throws BaseDaoException;

    public void deleteById(int id);

    // 查询
    public T findById(int id);

    // 获取全部
    public List<T> findAll() throws BaseDaoException;

    // 获取全部
    public List<T> findAllByPage(String sql, Object[] parameterValues) throws BaseDaoException;

}
