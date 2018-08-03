package com.idata.sale.service.web.base.dao;

import java.util.Map;

public class BeanMapper {
    private String sqlName;

    private FieldMapper idFieldMapper;

    private String tableName;

    private String beanName;

    private Map<String, FieldMapper> fieldsMap;

    private Map<String, FieldMapper> columnsMap;

    private Map<Class<?>, FieldMapper> joinsMap;

    private String joinSelectFields;

    public BeanMapper() {

    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Map<String, FieldMapper> getFieldsMap() {
        return fieldsMap;
    }

    public void setFieldsMap(Map<String, FieldMapper> fieldsMap) {
        this.fieldsMap = fieldsMap;
    }

    public Map<String, FieldMapper> getColumnsMap() {
        return columnsMap;
    }

    public void setColumnsMap(Map<String, FieldMapper> columnsMap) {
        this.columnsMap = columnsMap;
    }

    /**
     * joinSelectFields.
     *
     * @return the joinSelectFields
     */
    public String getJoinSelectFields() {
        return joinSelectFields;
    }

    /**
     * joinSelectFields.
     *
     * @param joinSelectFields the joinSelectFields to set
     */
    public void setJoinSelectFields(String joinSelectFields) {
        this.joinSelectFields = joinSelectFields;
    }

    /**
     * idFieldMapper.
     *
     * @return the idFieldMapper
     */
    public FieldMapper getIdFieldMapper() {
        return idFieldMapper;
    }

    /**
     * idFieldMapper.
     *
     * @param idFieldMapper the idFieldMapper to set
     */
    public void setIdFieldMapper(FieldMapper idFieldMapper) {
        this.idFieldMapper = idFieldMapper;
    }

    /**
     * joinsMap.
     *
     * @return the joinsMap
     */
    public Map<Class<?>, FieldMapper> getJoinsMap() {
        return joinsMap;
    }

    /**
     * joinsMap.
     *
     * @param joinsMap the joinsMap to set
     */
    public void setJoinsMap(Map<Class<?>, FieldMapper> joinsMap) {
        this.joinsMap = joinsMap;
    }

    /**
     * sqlName.
     *
     * @return the sqlName
     */
    public String getSqlName() {
        return sqlName;
    }

    /**
     * sqlName.
     *
     * @param sqlName the sqlName to set
     */
    public void setSqlName(String sqlName) {
        this.sqlName = sqlName;
    }
}
