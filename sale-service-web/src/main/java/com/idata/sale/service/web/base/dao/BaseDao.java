package com.idata.sale.service.web.base.dao;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.idata.sale.service.web.base.dao.annotation.Column;
import com.idata.sale.service.web.base.dao.annotation.Table;

public abstract class BaseDao<T> implements IBaseDao<T> {

    protected final String _b = " ";

    protected final String _select = " select ";

    protected final String _update = " UPDATE ";

    protected final String _delete = " delete ";

    protected final String _set = " set ";

    protected final String _where = " where ";

    protected final String _from = " from ";

    protected final String _and = " and ";

    protected final String _or = " or ";

    protected final String _leftJoin = " left join ";

    protected final String _on = " on ";

    protected final String _blank = " ";

    protected final String _leftBrack = "(";

    protected final String _rightBrack = ")";

    protected final String _equal = "=";

    protected final String _notEqual = "!=";

    protected final String _moreThan = ">";

    protected final String _moreThanEqual = ">=";

    protected final String _lessThan = "<";

    protected final String _lessThanEqual = "<=";

    @Autowired
    private BaseDaoConfiguration baseDaoConfiguration;

    private final static String PK_NAME = "id";

    private final static Logger logger = LoggerFactory.getLogger(BaseDao.class);

    private final static Set<?> NULL_SET = Collections.singleton(null);

    private final static Map<Class<?>, String> INSERT_SQL_MAP = new HashMap<>();

    protected Class<T> entityClass;

    public BaseDao() {
        entityClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    private class BatchUpdateCallback implements ConnectionCallback<Integer> {

        private int batchUpdateSize;

        private String sql;

        private List<Object[]> parameterValues;

        private BatchUpdateCallback(String sql, List<Object[]> parameterValues) {
            this.sql = sql;
            this.parameterValues = parameterValues;
            this.batchUpdateSize = baseDaoConfiguration.getBatchUpdateSize();
        }

        private BatchUpdateCallback(String sql, List<Object[]> parameterValues, int batchUpdateSize) {
            this.sql = sql;
            this.parameterValues = parameterValues;
            this.batchUpdateSize = batchUpdateSize;
        }

        @Override
        public Integer doInConnection(Connection connection) throws SQLException, DataAccessException {

            connection.setAutoCommit(false);
            PreparedStatement pstmt = connection.prepareStatement(sql);

            int size = parameterValues.size();

            if (logger.isDebugEnabled()) {
                logger.debug("[BaseDaoImpl][batchUpdate][open batch model][connection auto commit:"
                        + connection.getAutoCommit() + "]");
            }

            long now = 0l;
            long start = 0l;

            if (logger.isDebugEnabled()) {
                now = System.currentTimeMillis();
                start = System.currentTimeMillis();
            }

            int affectRows = 0;
            for (int i = 0; i < size; i++) {
                Object[] values = parameterValues.get(i);
                int length = values.length;
                for (int j = 0; j < length; j++) {
                    pstmt.setObject(j + 1, values[j]);
                }
                pstmt.addBatch();

                if ((i + 1) % batchUpdateSize == 0 || (i + 1) == size) {
                    int[] batchAffects = pstmt.executeBatch();
                    for (int j : batchAffects) {
                        if (1 == j) {
                            affectRows = affectRows + 1;
                        }
                    }
                    batchAffects = null;
                    pstmt.clearBatch();

                    if (logger.isDebugEnabled()) {
                        logger.debug("[][BatchUpdateCallback][execute batch use time:"
                                + ((System.currentTimeMillis() - now) / 1000.00) + "S]");
                        now = System.currentTimeMillis();
                    }
                }
            }
            connection.commit();
            connection.setAutoCommit(true);

            pstmt.close();
            pstmt = null;

            if (logger.isDebugEnabled()) {
                logger.debug("[][BatchUpdateCallback][finish][use time:"
                        + ((System.currentTimeMillis() - start) / 1000.00) + "S]");
            }

            return affectRows;
        }

    }

    // 处理添加获取key的内部implement
    private class PreparedStatementCreatorImpl implements PreparedStatementCreator {

        private String sql;

        private Object[] values;

        public PreparedStatementCreatorImpl(String sql, Object[] values) {
            this.sql = sql;
            this.values = values;
        }

        @Override
        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < values.length; i++) {
                ps.setObject(i + 1, values[i]);
            }
            return ps;
        }
    }

    @Override
    public long save(T t) throws BaseDaoException {
        return save(t, entityClass);
    }

    private long save(Object t, Class<?> clas) throws BaseDaoException {
        if (logger.isDebugEnabled()) {
            logger.debug("save " + clas.getName() + " start");
        }
        BeanMapper beanMapper = dbMapper.get(clas);
        if (null != beanMapper) {
            Collection<FieldMapper> fieldMappers = beanMapper.getFieldsMap().values();
            List<Object> values = new ArrayList<>(fieldMappers.size());
            try {
                for (FieldMapper fieldMapper : fieldMappers) {
                    Object fieldValue = fieldMapper.getGetMethod().invoke(t);// BeanUtils.getProperty(t,
                    // fieldMapper.getFieldName());
                    if (null == fieldValue && !StringUtils.isEmpty(fieldMapper.getDefaultValue())) {
                        if (fieldMapper.getFieldType().equals(Integer.class)) {
                            fieldValue = Integer.parseInt(fieldMapper.getDefaultValue());
                        }
                        else {
                            fieldValue = fieldMapper.getDefaultValue();
                        }
                    }
                    values.add(fieldValue);
                }
            }
            catch (IllegalAccessException | InvocationTargetException e) {
                logger.error("[][][Mapping Vo Table failed]", e);
                throw new BaseDaoException(BaseDaoCode.bean_field_mapping_failed, e.getMessage());
            }

            String insertSql = INSERT_SQL_MAP.get(clas);

            Object[] parameterValues = new Object[values.size()];
            values.toArray(parameterValues);
            values.clear();
            values = null;

            if (logger.isDebugEnabled()) {
                logger.debug("[][][insertSql:" + insertSql.toString() + "]");
                logger.debug("[][][parameterValues:" + Arrays.toString(parameterValues) + "]");
            }

            try {
                KeyHolder keyHolder = new GeneratedKeyHolder();
                PreparedStatementCreator preparedStatementCreatorImpl = new PreparedStatementCreatorImpl(
                        insertSql.toString(), parameterValues);
                jdbcTemplate.update(preparedStatementCreatorImpl, keyHolder);
                preparedStatementCreatorImpl = null;
                parameterValues = null;
                if (logger.isDebugEnabled()) {
                    logger.debug("[][][save " + clas.getName() + " end]");
                }
                return keyHolder.getKey().longValue();
            }
            catch (Exception e) {
                String emsg = e.getMessage();
                if (emsg.indexOf("Duplicate entry") > -1) {
                    throw new BaseDaoException(BaseDaoCode.bean_duplicate_entry_exception, emsg);
                }
                else {
                    throw new BaseDaoException(BaseDaoCode.dao_exception, emsg);
                }
            }
        }
        else {
            throw new BaseDaoException(BaseDaoCode.bean_mapper_not_found_exception,
                    "not found bean mapper,bean class:" + clas.getName());
        }
    }

    @Override
    public void batchSave(List<T> list) throws BaseDaoException {
        String sql = INSERT_SQL_MAP.get(entityClass);
        if (logger.isDebugEnabled()) {
            logger.debug("[][][sql:" + sql + "]");
        }
        int batchSize = baseDaoConfiguration.getBatchUpdateSize();
        List<Object[]> values = gengrateInsertValuesIncludAllFiled(list);

        BatchUpdateCallback callback = new BatchUpdateCallback(sql, values, batchSize);
        int affectRows = jdbcTemplate.execute(callback);
        callback = null;
        if (logger.isDebugEnabled()) {
            logger.debug("[][batchSave][affect rows :" + affectRows + "]");
        }
    }

    @Override
    public int update(T t) throws BaseDaoException {
        return update(t, entityClass);
    }

    @Override
    public Integer batchUpdate(List<T> beans) throws BaseDaoException {
        BeanMapper beanMapper = dbMapper.get(entityClass);
        List<FieldMapper> updateFields = new ArrayList<>();
        StringBuilder updateSql = new StringBuilder();
        updateSql.append("update").append(" ").append(beanMapper.getTableName()).append(" ").append("set");
        Collection<FieldMapper> fieldMappers = beanMapper.getFieldsMap().values();
        List<Object[]> fieldValues = new ArrayList<>();
        try {
            T t = beans.get(0);
            for (FieldMapper fieldMapper : fieldMappers) {
                Object fieldValue = fieldMapper.getGetMethod().invoke(t);
                if (null != fieldValue) {
                    updateSql.append(" ").append(fieldMapper.getColumnName()).append("=").append("?").append(" ")
                            .append(",");
                    updateFields.add(fieldMapper);
                }
            }
            updateSql.replace(updateSql.length() - 1, updateSql.length(), " ");
            updateSql.append("where").append(" ").append("id=?");

            for (T bean : beans) {
                List<Object> objValues = new ArrayList<>();
                for (FieldMapper fm : updateFields) {
                    objValues.add(fm.getGetMethod().invoke(bean));
                }
                objValues.add(BeanUtils.getProperty(bean, "id"));
                fieldValues.add(objValues.toArray());
            }

        }
        catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.error("[][batchUpdate][failed,case: Bean Field Mapping failed]", e);
            throw new BaseDaoException(BaseDaoCode.bean_field_mapping_failed, e.getMessage());
        }

        BatchUpdateCallback callback = new BatchUpdateCallback(updateSql.toString(), fieldValues);
        int affecs = jdbcTemplate.execute(callback);

        callback = null;
        fieldValues.clear();
        fieldValues = null;
        updateSql = null;
        return affecs;
    }

    private int update(Object t, Class<?> clas) throws BaseDaoException {
        BeanMapper beanMapper = dbMapper.get(clas);

        assert null != beanMapper;

        StringBuilder updateSql = new StringBuilder();
        updateSql.append("update").append(" ").append(beanMapper.getTableName()).append(" ").append("set");
        Collection<FieldMapper> fieldMappers = beanMapper.getFieldsMap().values();
        List<Object> fieldValues = new ArrayList<>(fieldMappers.size());
        try {
            for (FieldMapper fieldMapper : fieldMappers) {
                Object fieldValue = fieldMapper.getGetMethod().invoke(t);
                if (null != fieldValue) {
                    updateSql.append(" ").append(fieldMapper.getColumnName()).append("=").append("?").append(" ")
                            .append(",");
                    fieldValues.add(fieldValue);
                }
            }
            updateSql.replace(updateSql.length() - 1, updateSql.length(), " ");
            updateSql.append("where").append(" ").append("id=?");
            fieldValues.add(BeanUtils.getProperty(t, "id"));
            fieldValues.removeAll(NULL_SET);
        }
        catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.error("[][update][failed,case:Bean Field Mapping Failed]", e);
            throw new BaseDaoException(BaseDaoCode.bean_field_mapping_failed, e.getMessage());
        }
        int affect = jdbcTemplate.update(updateSql.toString(), fieldValues.toArray());

        if (logger.isDebugEnabled()) {
            logger.debug(
                    "[][update][affects:" + affect + ";" + updateSql.toString() + ";" + fieldValues.toString() + "]");
        }

        fieldValues.clear();
        fieldValues = null;
        updateSql = null;
        return affect;

    }

    protected int update(String sql, Object[] parameterValues) {
        if (logger.isDebugEnabled()) {
            logger.debug("[BaseDaoImpl][update][sql:" + sql + "]");
            logger.debug("[BaseDaoImpl][update][parameterValues:" + Arrays.toString(parameterValues) + "]");
        }
        int affect = jdbcTemplate.update(sql, parameterValues);
        if (logger.isDebugEnabled()) {
            logger.debug("[BaseDaoImpl][update][affect:" + affect + "]");
        }
        return affect;
    }

    protected Integer batchSave(String sql, List<Object[]> parameterValues) {
        int batchUpdateSize = baseDaoConfiguration.getBatchUpdateSize();
        return batchUpdate(sql, parameterValues, batchUpdateSize);
    }

    protected Integer batchUpdate(String sql, List<Object[]> parameterValues) {
        int batchUpdateSize = baseDaoConfiguration.getBatchUpdateSize();
        return batchUpdate(sql, parameterValues, batchUpdateSize);
    }

    private Integer batchUpdate(String sql, List<Object[]> parameterValues, int batchUpdateSize) {

        if (logger.isDebugEnabled()) {
            logger.debug("[BaseDaoImpl][batchUpdate][sql:" + sql + "]");
        }

        BatchUpdateCallback updateTransactionCallback = new BatchUpdateCallback(sql, parameterValues, batchUpdateSize);

        int affectRows = jdbcTemplate.execute(updateTransactionCallback);

        try {
            return affectRows;
        }
        finally {
            updateTransactionCallback = null;
        }
    }

    @Override
    public int delete(T t) throws BaseDaoException {
        BeanMapper beanMapper = dbMapper.get(entityClass);
        assert null != beanMapper;
        StringBuilder deleteBuilder = new StringBuilder();
        deleteBuilder.append("delete from").append(" ").append(beanMapper.getTableName()).append(" ").append("where")
                .append(" ").append("id=?");
        String deleteSql = deleteBuilder.toString();
        Object[] values = null;
        try {
            values = new Object[] { BeanUtils.getSimpleProperty(t, "id") };
            if (logger.isDebugEnabled()) {
                logger.debug("[BaseDaoImpl][delete " + entityClass.getSimpleName() + "][" + deleteSql + "]");
                logger.debug(
                        "[BaseDaoImpl][delete " + entityClass.getSimpleName() + "][" + Arrays.toString(values) + "]");
            }
            int affect = jdbcTemplate.update(deleteSql, values);
            if (logger.isDebugEnabled()) {
                logger.debug("[BaseDaoImpl][delete " + entityClass.getSimpleName() + "][affect:" + affect + "]");
            }
            return affect;
        }
        catch (DataAccessException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.error("[BaseDaoImpl][delete][failed,class:" + entityClass.getName() + "]", e);
            throw new BaseDaoException(BaseDaoCode.dao_exception, e.getMessage());
        }
        finally {
            deleteBuilder = null;
            deleteSql = null;
            values = null;
        }

    }

    @Override
    public void deleteById(int id) {

        StringBuilder deleteBuilder = new StringBuilder();
        deleteBuilder.append("delete from").append(" ").append(getTableName()).append(" ").append("where").append(" ")
                .append("id=?");
        String deleteSql = deleteBuilder.toString();
        Object[] values = null;
        try {
            values = new Object[] { id };
            if (logger.isDebugEnabled()) {
                logger.debug("[BaseDaoImpl][delete " + entityClass.getSimpleName() + "][" + deleteSql + "]");
                logger.debug(
                        "[BaseDaoImpl][delete " + entityClass.getSimpleName() + "][" + Arrays.toString(values) + "]");
            }
            int affect = jdbcTemplate.update(deleteSql, values);
            if (logger.isDebugEnabled()) {
                logger.debug("[BaseDaoImpl][delete " + entityClass.getSimpleName() + "][affect:" + affect + "]");
            }
        }
        finally {
            deleteBuilder = null;
            deleteSql = null;
            values = null;
        }

    }

    protected void batchDelete(Object[] ids) {
        BeanMapper beanMapper = dbMapper.get(entityClass);
        assert null != beanMapper;
        int batchSize = baseDaoConfiguration.getBatchUpdateSize();
        String sql = "delete from " + beanMapper.getTableName() + " where id=?";
        int size = ids.length;
        List<Object[]> values = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            values.add(new Object[] { ids[i] });
        }

        BatchUpdateCallback callback = new BatchUpdateCallback(sql, values, batchSize);
        int affects = jdbcTemplate.execute(callback);
        if (logger.isDebugEnabled()) {
            logger.debug("[][batch delete " + entityClass.getName() + "][affect rows:" + affects + "]");
        }
        callback = null;
        values.clear();
        values = null;
        sql = null;
    }

    @Override
    public T findById(int id) {
        try {
            return (T) findById(entityClass, id);
        }
        catch (Exception e) {
            logger.error("[][findById][failed,id:" + id + ";class:" + entityClass.getName() + "]", e);
        }
        return null;
    }

    private Object findById(Class<?> clas, int id) throws BaseDaoException {
        BeanMapper beanMapper = dbMapper.get(clas);
        assert null != beanMapper;
        StringBuilder sltSql = new StringBuilder();
        sltSql.append("select ").append(getAllCloumnStrWithComma()).append(" from ").append(beanMapper.getTableName())
                .append(" ").append("where id=?");
        Object[] values = new Object[] { id };
        List list = findList(clas, sltSql.toString(), values);
        if (!CollectionUtils.isEmpty(list)) {
            try {
                return list.get(0);
            }
            finally {
                list = null;
                sltSql = null;
                values = null;
            }
        }
        return null;
    }

    @Override
    public List<T> findAll() throws BaseDaoException {
        return (List<T>) findAll(entityClass);
    }

    private List<Object> findAll(Class<?> clas) throws BaseDaoException {

        BeanMapper beanMapper = dbMapper.get(clas);
        assert null != beanMapper;
        StringBuilder sltSql = new StringBuilder();
        sltSql.append("select ").append(getAllCloumnStrWithComma()).append(" from").append(" ")
                .append(beanMapper.getTableName());
        try {
            return findList(clas, sltSql.toString(), null);
        }
        finally {
            sltSql = null;
        }
    }

    @Override
    public List<T> findAllByPage(String sql, Object[] parameterValues) throws BaseDaoException {

        int pageSize = baseDaoConfiguration.getBatchUpdateSize();

        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageSize(pageSize);
        pageInfo.setPageNum(1);

        List<T> ts = findListByPage(sql, parameterValues, pageInfo);

        try {
            if (pageInfo.getPageTotal() > 1) {
                int pageTotal = pageInfo.getPageTotal();
                for (int i = 2; i <= pageTotal; i++) {
                    pageInfo.setPageSize(i);
                    ts.addAll(findListByPage(sql, parameterValues, pageInfo, false));
                }
            }
            return ts;
        }
        finally {
            pageInfo = null;
        }
    }

    protected List<Map<String, Object>> findAllMapByPage(String sql, String countField, Object[] parameterValues) {

        int pageSize = baseDaoConfiguration.getDefaultPageSize();

        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageSize(pageSize);
        pageInfo.setPageNum(1);

        List<Map<String, Object>> ts = findMapListByPage(sql, parameterValues, pageInfo);
        findMapListByPage(sql, countField, parameterValues, pageInfo, true);

        try {
            if (pageInfo.getPageTotal() > 1) {
                int pageTotal = pageInfo.getPageTotal();
                for (int i = 2; i <= pageTotal; i++) {
                    pageInfo.setPageNum(i);
                    ts.addAll(findMapListByPage(sql, parameterValues, pageInfo, false));
                }
            }
            return ts;
        }
        finally {
            pageInfo = null;
        }
    }

    protected List findList(String sql, Object[] parameterValues) {
        try {
            return findList(entityClass, sql, parameterValues);
        }
        catch (Exception e) {
            logger.error("[BaseDao][findList][failed]", e);
        }
        return null;
    }

    private List findList(Class<?> clas, String sql, Object[] params) throws BaseDaoException {
        if (logger.isDebugEnabled()) {
            logger.debug("[][findList][sql:" + sql + "]");
            logger.debug("[][findList][params:" + (null == params ? "null" : Arrays.toString(params)) + "]");
        }
        List list = jdbcTemplate.queryForList(sql, params);
        if (!CollectionUtils.isEmpty(list)) {
            BeanMapper beanMapper = dbMapper.get(clas);
            Map<String, FieldMapper> columnsMap = beanMapper.getColumnsMap();

            Map<String, Object> firstRowMap = (Map<String, Object>) list.get(0);
            Set<String> columnSet = firstRowMap.keySet();

            List obejcts = new ArrayList<>(list.size());
            try {
                for (Object object : list) {
                    Object t = clas.newInstance();
                    Map<String, Object> rowMap = (Map<String, Object>) object;
                    if (rowMap.containsKey(PK_NAME)) {
                        BeanUtils.setProperty(t, PK_NAME, rowMap.get(PK_NAME));
                        rowMap.remove(PK_NAME);
                    }
                    for (String columnName : columnSet) {
                        FieldMapper fm = columnsMap.get(columnName);
                        if (null == fm) {
                            continue;
                        }
                        Object value = rowMap.get(columnName);
                        if (null != value) {
                            BeanUtils.setProperty(t, fm.getFieldName(), value);
                        }
                    }
                    obejcts.add(t);
                }
                list.clear();
                list = null;
                obejcts.removeAll(NULL_SET);
                return obejcts;
            }
            catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                logger.error("[][findList][failed,class name:" + e.getClass().getName() + "]", e);
                throw new BaseDaoException(BaseDaoCode.dao_exception, e.getMessage());
            }
        }
        return null;
    }

    protected List<T> findListByPage(String sql, Object[] params, PageInfo pageInfo) throws BaseDaoException {
        return findListByPage(sql, params, pageInfo, true);
    }

    private List<T> findListByPage(String sql, Object[] params, PageInfo pageInfo, boolean setPage)
            throws BaseDaoException {
        return findListByPage(entityClass, sql, params, pageInfo, setPage);
    }

    List findListByPage(Class<?> clas, String sql, Object[] params, PageInfo pageInfo) throws BaseDaoException {
        return findListByPage(clas, sql, params, pageInfo, true);
    }

    List findListByPage(Class<?> clas, String sql, String countField, Object[] params, PageInfo pageInfo)
            throws BaseDaoException {
        return findListByPage(clas, sql, countField, params, pageInfo, true);
    }

    List findListByPage(Class<?> clas, String sql, Object[] params, PageInfo pageInfo, boolean setPage)
            throws BaseDaoException {
        return findListByPage(clas, sql, "*", params, pageInfo, setPage);
    }

    List findListByPage(Class<?> clas, String sql, String coutField, Object[] params, PageInfo pageInfo,
            boolean setPage) throws BaseDaoException {

        if (logger.isDebugEnabled()) {
            logger.debug("sql:" + sql);
            logger.debug("params:" + (null == params ? "null" : Arrays.toString(params)));
        }

        if (null != pageInfo) {
            if (setPage) {
                setPage(sql, coutField, params, pageInfo);
            }
            return findList(clas, getLimitSql(sql, pageInfo), params);
        }
        else {
            return findList(clas, sql, params);
        }
    }

    private final static String QUERY_COUNT_FIELD_ALL = "*";

    protected List<Map<String, Object>> findMapListByPage(String sql, Object[] values, PageInfo pageInfo) {
        return findMapListByPage(sql, QUERY_COUNT_FIELD_ALL, values, pageInfo);
    }

    public List<Map<String, Object>> findMapListByPage(String sql, String countField, Object[] values,
            PageInfo pageInfo) {

        boolean setPage = true;
        int pageTotal = pageInfo.getPageTotal();
        if (pageTotal > 0) {
            setPage = false;
        }
        return findMapListByPage(sql, countField, values, pageInfo, setPage);
    }

    public List<Map<String, Object>> findMapListByPage(String sql, Object[] values, PageInfo pageInfo,
            boolean setPage) {
        return findMapListByPage(sql, QUERY_COUNT_FIELD_ALL, values, pageInfo, setPage);
    }

    public List<Map<String, Object>> findMapListByPage(String sql, String countField, Object[] values,
            PageInfo pageInfo, boolean setPage) {
        if (setPage) {
            setPage(sql, countField, values, pageInfo);
        }
        String limitSql = getLimitSql(sql, pageInfo);
        if (logger.isDebugEnabled()) {
            logger.debug("[BaseDaoImpl][findMapListByPage][values:"
                    + (null != values ? Arrays.toString(values) : "null") + "]");
        }
        return jdbcTemplate.queryForList(limitSql, values);
    }

    protected String getLimitSql(String sql, PageInfo pageInfo) {
        int pageSize = pageInfo.getPageSize();
        int pageNum = pageInfo.getPageNum();
        if (pageNum == 0) {
            pageNum = 1;
        }

        StringBuilder limitSql = new StringBuilder(sql);
        int start = (pageNum - 1) * pageSize;
        int end = pageSize * pageNum;
        limitSql.append(" ").append("limit").append(" ").append(start).append(",").append(pageSize);
        if (logger.isDebugEnabled()) {
            logger.debug("[BaseDaoImpl][getLimitSql][pageSize:" + pageInfo.getPageSize() + "pageNum:"
                    + pageInfo.getPageNum() + ";start:" + start + ";end:" + end + "]");
            logger.debug("[BaseDaoImpl][getLimitSql][limit sql:" + limitSql + "]");
        }
        return limitSql.toString();
    }

    protected PageInfo setPage(String sql, Object[] params, PageInfo pageInfo) {
        return setPage(sql, "*", params, pageInfo);
    }

    protected PageInfo setPage(String sql, String countFiled, Object[] params, PageInfo pageInfo) {
        int pageSize = pageInfo.getPageSize();
        if (pageSize < 1) {
            pageSize = baseDaoConfiguration.getDefaultPageSize();
            pageInfo.setPageSize(pageSize);
        }

        int pageNum = pageInfo.getPageNum();
        if (pageNum < 1) {
            pageNum = 1;
            pageInfo.setPageNum(1);
        }

        long total = count(sql, countFiled, params);
        pageInfo.setTotal(total);

        int pageTotal = (int) (total / pageSize);
        if ((total % pageSize) > 0) {
            pageTotal = pageTotal + 1;
        }
        pageInfo.setPageTotal(pageTotal);
        return pageInfo;
    }

    public long count(String sql, Object[] paramValues) {
        return count(sql, "*", paramValues);
    }

    public long count(String sql, String countField, Object[] paramValues) {

        int fromIndex = sql.indexOf("from");
        int joinIndex = -1;
        // int joinIndex = sql.indexOf("left join");
        // if (joinIndex == -1) {
        // joinIndex = sql.indexOf("inner join");
        // }
        // if (joinIndex == -1) {
        // joinIndex = sql.indexOf("outer join");
        // }
        int whereIndex = sql.indexOf("where");

        int orderByIndex = sql.lastIndexOf("order by");
        int groupByIndex = sql.lastIndexOf("group by");

        int lastBracketIndex = sql.lastIndexOf(")");

        if (groupByIndex < lastBracketIndex) {
            groupByIndex = -1;
        }

        if (orderByIndex < lastBracketIndex) {
            orderByIndex = -1;
        }

        int startIndex = fromIndex;
        int endIndex = orderByIndex;
        if (groupByIndex != -1 && groupByIndex < orderByIndex) {
            endIndex = groupByIndex;
        }
        if (endIndex == -1) {
            endIndex = sql.length();
        }

        StringBuilder countSql = new StringBuilder();
        countSql.append("select count(").append(countField).append(") ");
        if (joinIndex == -1) {
            countSql.append(sql.substring(startIndex, endIndex));
        }
        else {
            countSql.append(sql.substring(fromIndex, joinIndex));
            if (whereIndex != -1) {
                countSql.append(" ").append(sql.substring(whereIndex, endIndex));
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("[BaseDaoImpl][count][count sql:" + countSql.toString() + "]");
        }

        return jdbcTemplate.queryForObject(countSql.toString(), Long.class, paramValues);
    }

    public Long getCount(String sql, Object[] paramValues) {
        if (logger.isDebugEnabled()) {
            logger.debug("[BaseDao][getCount][sql:" + sql + "][values:" + Arrays.toString(paramValues) + "]");
        }
        return jdbcTemplate.queryForObject(sql, Long.class, paramValues);
    }

    public List<Map<String, Object>> find(String sql, Object[] parameterValues) {
        if (logger.isDebugEnabled()) {
            logger.debug(sql);
            logger.debug(Arrays.toString(parameterValues));
        }
        return jdbcTemplate.queryForList(sql, parameterValues);
    }

    // 动态生成对象并设置数据
    private Object gengrateObject(Class<?> clas, Map<String, Object> rowMap)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object t = clas.newInstance();
        BeanMapper beanMapper = dbMapper.get(clas);
        beanMapper.getIdFieldMapper().getSetMethod().invoke(t, rowMap.get(beanMapper.getIdFieldMapper().getSqlName()));
        Map<String, FieldMapper> fieldMap = beanMapper.getFieldsMap();
        Set<Entry<String, FieldMapper>> fieldSet = fieldMap.entrySet();
        for (Entry<String, FieldMapper> entry : fieldSet) {
            FieldMapper fm = entry.getValue();
            fm.getSetMethod().invoke(t, rowMap.get(fm.getSqlName()));
        }
        return t;
    }

    // 通过class获取数据库表名称
    private String getTableName(Class<?> clas) {
        if (dbMapper.containsKey(clas)) {
            return dbMapper.get(clas).getTableName();
        }
        return "";
    }

    protected String getTableName() {
        return getTableName(entityClass);
    }

    protected String getTableName(String tabAlias) {
        return " " + getTableName(entityClass) + " as " + tabAlias + " ";
    }

    // 生成插入sql，包含全部字段
    private String gengrateInsertSqlIncludAllFiled() {
        StringBuilder insertSql = new StringBuilder();
        insertSql.append("insert into");
        BeanMapper beanMapper = dbMapper.get(entityClass);
        insertSql.append(" ").append(beanMapper.getTableName());
        Collection<FieldMapper> fieldMappers = beanMapper.getFieldsMap().values();
        insertSql.append("(");
        for (FieldMapper fieldMapper : fieldMappers) {
            insertSql.append(fieldMapper.getColumnName()).append(",");
        }
        insertSql.replace(insertSql.length() - 1, insertSql.length(), ")");
        insertSql.append("values").append("(");
        for (int i = 0; i < fieldMappers.size(); i++) {
            insertSql.append("?,");
        }
        insertSql.replace(insertSql.length() - 1, insertSql.length(), ")");
        return insertSql.toString();
    }

    // 生成插入时字段值数组，包含全部字段
    public List<Object[]> gengrateInsertValuesIncludAllFiled(List<T> list) throws BaseDaoException {
        BeanMapper beanMapper = dbMapper.get(entityClass);
        Collection<FieldMapper> fieldMappers = beanMapper.getFieldsMap().values();

        List<Object[]> values = new ArrayList<>();

        try {
            for (T t : list) {
                List<Object> fieldValues = new ArrayList<>(fieldMappers.size());
                for (FieldMapper fieldMapper : fieldMappers) {
                    Object fieldValue = fieldMapper.getGetMethod().invoke(t);
                    if (null == fieldValue && !StringUtils.isEmpty(fieldMapper.getDefaultValue())) {
                        fieldValue = fieldMapper.getDefaultValue();
                    }
                    fieldValues.add(fieldValue);
                }

                values.add(fieldValues.toArray());
            }
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("[][gengrateInsertValuesIncludAllFiled][failed]", e);
            throw new BaseDaoException(BaseDaoCode.bean_field_mapping_failed, e.getMessage());
        }

        return values;
    }

    // 生成通过ID查询同时join关联对象的sql
    private String generateSql4FindByIdJoinFk(Class<?> clas) {

        BeanMapper beanMapper = dbMapper.get(clas);

        String sql = null;
        if (MapUtils.isNotEmpty(beanMapper.getJoinsMap())) {
            StringBuilder findByIdJoinFkSql = new StringBuilder();
            findByIdJoinFkSql.append("select ").append(beanMapper.getJoinSelectFields()).append(" ").append("from");
            findByIdJoinFkSql.append(" ").append(beanMapper.getTableName()).append(" ").append(beanMapper.getSqlName());

            Set<Class<?>> joinClasses = beanMapper.getJoinsMap().keySet();
            for (Class<?> joinClass : joinClasses) {
                BeanMapper joinBeanMapper = dbMapper.get(joinClass);
                findByIdJoinFkSql.append(" ").append("left join");
                findByIdJoinFkSql.append(" ").append(joinBeanMapper.getTableName()).append(" ")
                        .append(joinBeanMapper.getSqlName());
                findByIdJoinFkSql.append(" ").append("on").append(" ").append(beanMapper.getSqlName()).append(".")
                        .append(joinBeanMapper.getTableName()).append("_id").append("=")
                        .append(joinBeanMapper.getSqlName()).append(".").append(PK_NAME);
            }
            findByIdJoinFkSql.append(" ").append("where").append(" ").append(beanMapper.getSqlName()).append(".")
                    .append(PK_NAME).append("=?");
            sql = findByIdJoinFkSql.toString();
            findByIdJoinFkSql.reverse();
            findByIdJoinFkSql = null;
        }
        else {
            logger.error("[][][" + beanMapper.getBeanName() + " not has join object.]");
        }
        return sql;
    }

    private final static Map<Class<?>, BeanMapper> dbMapper = new HashMap<Class<?>, BeanMapper>();

    private final static String package_name = "com.idata.sale.service.web.base.dao.dbo";

    static {
        logger.info("[BaseDao][Static][Load dbMapper Start]");
        String basePath = BaseDao.class.getResource("/").getPath();
        String packageDirPath = "";
        if (basePath.indexOf("/") != -1) {
            packageDirPath = basePath + package_name.replaceAll("\\.", "/");
        }
        else {
            packageDirPath = basePath + package_name.replaceAll("\\.", "\\\\");
        }
        File packageDir = new File(packageDirPath);
        if (packageDir.isDirectory()) {
            String[] filePaths = packageDir.list();
            List<String> beanNames = new ArrayList<>(filePaths.length);
            for (String beanFilePath : packageDir.list()) {
                beanNames.add(package_name + "." + beanFilePath.substring(0, beanFilePath.length() - 6));
            }
            filePaths = null;
            try {
                for (String beanName : beanNames) {
                    Class clas = Class.forName(beanName);
                    Annotation clasAnnation = clas.getAnnotation(Table.class);
                    if (null != clasAnnation) {

                        logger.info("[Load VO Mapper:" + beanName + "]");

                        BeanMapper beanMapper = new BeanMapper();
                        beanMapper.setBeanName(clas.getSimpleName());
                        beanMapper.setSqlName(formatBeanName(clas.getSimpleName()));

                        Table table = (Table) clasAnnation;
                        beanMapper.setTableName(table.value());

                        Field[] classFields = clas.getDeclaredFields();
                        Map<String, FieldMapper> fields = new Hashtable<String, FieldMapper>(classFields.length);
                        Map<String, FieldMapper> columns = new Hashtable<String, FieldMapper>(classFields.length);
                        // Map<Class<?>, FieldMapper> joins = null;
                        for (Field field : classFields) {
                            Column jdbcFieldAnnation = field.getAnnotation(Column.class);
                            if (null != jdbcFieldAnnation) {
                                FieldMapper jfo = new FieldMapper();
                                jfo.setFieldName(field.getName());
                                jfo.setFieldType(field.getType());
                                jfo.setDateFormat(jdbcFieldAnnation.dateFormat());
                                jfo.setDefaultValue(jdbcFieldAnnation.defaultValue());
                                jfo.setDate(jdbcFieldAnnation.isDate());
                                jfo.setNull(jdbcFieldAnnation.isNull());
                                jfo.setColumnType(jdbcFieldAnnation.jdbcType());
                                jfo.setColumnName(jdbcFieldAnnation.jdbcName());
                                if (StringUtils.isEmpty(jfo.getColumnName())) {
                                    jfo.setColumnName(toColumnName(field.getName()));
                                }
                                jfo.setSqlName(beanMapper.getSqlName() + "." + jfo.getColumnName());
                                jfo.setGetMethod(clas.getMethod(getGetMethodName(field.getName())));
                                jfo.setSetMethod(clas.getMethod(getSetMethodName(field.getName()), field.getType()));
                                fields.put(jfo.getFieldName(), jfo);
                                columns.put(jfo.getColumnName(), jfo);
                            }
                            else if (field.getName().equals(PK_NAME)) {
                                FieldMapper jfo = new FieldMapper();
                                jfo.setSqlName(beanMapper.getSqlName() + "." + PK_NAME);
                                jfo.setGetMethod(clas.getMethod(getGetMethodName(PK_NAME)));
                                jfo.setSetMethod(clas.getMethod(getSetMethodName(PK_NAME), field.getType()));
                                beanMapper.setIdFieldMapper(jfo);
                            }
                        }
                        beanMapper.setFieldsMap(fields);
                        beanMapper.setColumnsMap(columns);
                        dbMapper.put(clas, beanMapper);
                    }
                }
                initInserSql();
                logger.info("[BaseDao][Static][Load dbMapper End]");
            }
            catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
                logger.error("[BaseDao][][Load dbMapper failed]", e);
                throw new RuntimeException("BasoDao Load BeanMapper failed");
            }
        }
    }

    private static String formatBeanName(String beanName) {
        char firsrChar = beanName.charAt(0);
        return "_" + Character.toLowerCase(firsrChar) + beanName.substring(1, beanName.length() - 2);
    }

    // 初始化 insert sql
    private static void initInserSql() {

        Set<Entry<Class<?>, BeanMapper>> dbMapperSet = dbMapper.entrySet();
        for (Entry<Class<?>, BeanMapper> entry : dbMapperSet) {
            Class<?> clas = entry.getKey();
            BeanMapper beanMapper = entry.getValue();

            StringBuilder insertSql = new StringBuilder();
            insertSql.append("insert into");
            insertSql.append(" ").append(beanMapper.getTableName());
            Collection<FieldMapper> fieldMappers = beanMapper.getFieldsMap().values();
            insertSql.append("(");
            for (FieldMapper fieldMapper : fieldMappers) {
                insertSql.append(fieldMapper.getColumnName()).append(",");
            }
            insertSql.replace(insertSql.length() - 1, insertSql.length(), ")");
            insertSql.append("values").append("(");
            for (int i = 0; i < fieldMappers.size(); i++) {
                insertSql.append("?,");
            }
            insertSql.replace(insertSql.length() - 1, insertSql.length(), ")");
            if (logger.isDebugEnabled()) {
                logger.debug(clas.getName() + ":" + insertSql.toString());
            }
            INSERT_SQL_MAP.put(clas, insertSql.toString());
        }
    }

    // 将vo字段名称转换为数据库字段,例如:roleName->role_name
    private static String toColumnName(String fieldName) {
        char[] chars = fieldName.toCharArray();
        StringBuilder jdbcName = new StringBuilder();
        for (char c : chars) {
            if (Character.isUpperCase(c)) {
                jdbcName.append("_").append(Character.toLowerCase(c));
            }
            else {
                jdbcName.append(c);
            }
        }
        chars = null;
        return jdbcName.toString();
    }

    // 获取get
    private static String getGetMethodName(String fieldName) {
        return "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1, fieldName.length());
    }

    private static String getSetMethodName(String fieldName) {
        return "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1, fieldName.length());
    }

    protected BeanMapper getBeanMapper(Class<?> clas) {
        return dbMapper.get(clas);
    }

    protected String getNamesString(List<Integer> ids, String columnName) {
        BeanMapper beanMapper = dbMapper.get(entityClass);
        if (null != beanMapper && CollectionUtils.isNotEmpty(ids)
                && beanMapper.getColumnsMap().containsKey(columnName)) {
            StringBuilder sql = new StringBuilder();
            sql.append("select ").append(columnName).append(" ").append("as columnName").append(" from ")
                    .append(beanMapper.getTableName()).append(" ").append("where");
            int length = ids.size();
            for (int i = 0; i < length; i++) {
                sql.append(" ").append("id=?").append(" or");
            }
            sql.replace(sql.length() - 2, sql.length(), "");
            List<Map<String, Object>> maps = this.find(sql.toString(), ids.toArray());
            if (CollectionUtils.isNotEmpty(maps)) {
                StringBuilder names = new StringBuilder();
                for (Map<String, Object> map : maps) {
                    names.append(map.get("columnName")).append(",");
                }
                if (names.length() > 0) {
                    names.replace(names.length() - 1, names.length(), "");
                }
                return names.toString();
            }
        }
        return null;
    }

    protected String getNamesString(List<Integer> ids) {
        return getNamesString(ids, "name");
    }

    protected String getNameString(Integer id, String name) {
        List<Integer> list = new ArrayList<>();
        list.add(id);
        return getNamesString(list, name);
    }

    protected String getNameString(Integer id) {
        return getNameString(id, "name");
    }

    protected boolean existByFiled(boolean isModify, Integer id, String[] fields, Object[] values) {

        BeanMapper beanMapper = dbMapper.get(entityClass);
        if (null != beanMapper) {
            StringBuilder sql = new StringBuilder();
            sql.append("select id from ").append(beanMapper.getTableName());
            if (null != fields && fields.length > 0) {
                sql.append(" ").append("where 1=1 and");
                for (String field : fields) {
                    sql.append(" ").append(field).append("=?").append(" and");
                }
                sql.replace(sql.length() - 3, sql.length(), "");

                List<Map<String, Object>> maps = this.find(sql.toString(), values);
                if (CollectionUtils.isNotEmpty(maps)) {
                    if (isModify) {
                        for (Map<String, Object> map : maps) {
                            if (map.get("id").equals(id)) {
                                maps.remove(map);
                                break;
                            }
                        }
                    }
                }
                if (CollectionUtils.isNotEmpty(maps)) {
                    maps.clear();
                    return true;
                }
                else {
                    return false;
                }
            }
            else {
                throw new RuntimeException("get exist by field throws exception,case:fields is null.");
            }
        }
        return false;
    }

    protected List<String> getQueryOrSqlsWithId(String baseSql, List values) {
        return getQueryOrSqls(baseSql, "id", values);
    }

    public List<String> getQueryOrSqls(String baseSql, String inFiledName, List values) {

        if (CollectionUtils.isNotEmpty(values)) {
            int whereIndex = baseSql.indexOf("where");
            boolean hasWhere = whereIndex > -1;
            int andIndex = baseSql.indexOf(" and ", whereIndex + 1);
            boolean hasAnd = andIndex > whereIndex;

            int queryInBatchSize = baseDaoConfiguration.getDefaultPageSize();
            int valuesSize = values.size();
            List<String> sqlList = new ArrayList<>();
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append(baseSql);
            if (!hasWhere) {
                sqlBuilder.append(" where ");
            }

            if (hasAnd) {
                sqlBuilder.append(" (");
            }
            for (int i = 0; i < valuesSize; i++) {
                sqlBuilder.append(" ").append(inFiledName).append("=").append(values.get(i)).append(" ").append("or");
                if ((i + 1) % queryInBatchSize == 0 || (i + 1) == valuesSize) {
                    String sql = sqlBuilder.substring(0, sqlBuilder.length() - 2);
                    if (hasAnd) {
                        sql += ")";
                    }
                    sqlList.add(sql);
                    if ((i + 1) != valuesSize) {
                        sqlBuilder = null;
                        sqlBuilder = new StringBuilder();
                        sqlBuilder.append(baseSql);
                        if (!hasWhere) {
                            sqlBuilder.append(" where ");
                        }
                        if (hasAnd) {
                            sqlBuilder.append(" (");
                        }
                    }
                }
            }
            return sqlList;
        }
        return null;
    }

    public List<String> getQueryInSqlList(String baseSql, List<Integer> ids) {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(baseSql);
        int start = sqlBuilder.lastIndexOf("(") + 1;
        int end = sqlBuilder.lastIndexOf(")");
        if (start == end) {
            sqlBuilder.insert(start + 1, " ");
            start = sqlBuilder.lastIndexOf("(") + 1;
            end = sqlBuilder.lastIndexOf(")");
        }

        int diff = sqlBuilder.length() - end;

        List<String> queryInSqls = new ArrayList<>();

        List<String> inIdsList = getQueryInIdsList(ids);
        for (String inIds : inIdsList) {
            String querySql = sqlBuilder.replace(start, sqlBuilder.length() - diff, inIds).toString();
            if (logger.isDebugEnabled()) {
                logger.debug("[][][" + querySql + "]");
            }
            queryInSqls.add(querySql);
        }
        inIdsList.clear();
        inIdsList = null;

        return queryInSqls;
    }

    protected List<String> getQueryInIdsList(List<Integer> ids) {
        int queryInBatchSize = baseDaoConfiguration.getDefaultPageSize();

        List<String> inIdList = new ArrayList<>();
        StringBuilder idBuilder = new StringBuilder();

        int size = ids.size();
        for (int i = 0; i < size; i++) {
            idBuilder.append(ids.get(i)).append(",");
            if (((i + 1) % queryInBatchSize == 0) || (i + 1) == size) {
                inIdList.add(idBuilder.substring(0, idBuilder.length() - 1).toString());
                idBuilder.replace(0, idBuilder.length(), "");
            }
        }
        try {
            return inIdList;
        }
        finally {
            idBuilder = null;
        }
    }

    protected String getAllCloumnStrWithComma() {
        StringBuilder columnBuilder = new StringBuilder();
        columnBuilder.append(" ").append("id");
        BeanMapper grupPolicyBeanMapper = getBeanMapper(entityClass);
        Set<String> columnSet = grupPolicyBeanMapper.getColumnsMap().keySet();
        for (String columnName : columnSet) {
            columnBuilder.append(",").append(columnName);
        }
        columnBuilder.append(" ");
        try {
            return columnBuilder.toString();
        }
        finally {
            columnBuilder = null;
        }
    }

    protected String getAllCloumnStrWithComma(String tabAliasName) {
        StringBuilder columnBuilder = new StringBuilder();
        columnBuilder.append(" ").append(tabAliasName).append(".id");
        BeanMapper grupPolicyBeanMapper = getBeanMapper(entityClass);
        Set<String> columnSet = grupPolicyBeanMapper.getColumnsMap().keySet();
        for (String columnName : columnSet) {
            columnBuilder.append(",").append(tabAliasName).append(".").append(columnName);
        }
        columnBuilder.append(" ");
        try {
            return columnBuilder.toString();
        }
        finally {
            columnBuilder = null;
        }
    }

    protected Map<String, Object> getMap(String sql, Object[] values) {
        List<Map<String, Object>> maps = find(sql, values);
        try {
            if (CollectionUtils.isNotEmpty(maps)) {
                return maps.get(0);
            }
        }
        finally {
            if (null != maps) {
                maps.clear();
                maps = null;
            }
            sql = null;
            values = null;
        }
        return null;
    }

    protected Object getObject(String sql, Object[] values) throws SQLException {
        Map<String, Object> map = getMap(sql, values);
        if (null != map) {
            try {
                Set<String> keys = map.keySet();
                if (keys.size() == 1) {
                    return map.get(keys.iterator().next());
                }
                else {
                    throw new SQLException(
                            "getObject only allow select one field,but current sql select multi field,sql:" + sql);
                }
            }
            finally {
                map.clear();
                map = null;
            }

        }
        return null;
    }

    protected Integer getInteger(String sql, Object[] values) throws SQLException {
        Object obj = getObject(sql, values);
        if (null == obj) {
            return null;
        }

        if (obj instanceof Long) {
            return ((Long) obj).intValue();
        }

        return (Integer) obj;
    }

    protected String getString(String sql, Object[] values) throws SQLException {
        Object obj = getObject(sql, values);
        if (null == obj) {
            return null;
        }
        return (String) obj;
    }
}
