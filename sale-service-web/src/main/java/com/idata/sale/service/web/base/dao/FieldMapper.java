package com.idata.sale.service.web.base.dao;

import java.lang.reflect.Method;

public class FieldMapper {
    private String fieldName;

    private Class<?> fieldType;

    private String sqlName;

    private String columnName;

    private String columnType;

    private boolean isNull;

    private boolean isDate;

    private String dateFormat;

    private String defaultValue;

    private Method setMethod;

    private Method getMethod;

    public FieldMapper() {

    }

    public FieldMapper(String fieldName, Class<?> fieldType, String columnName, String columnType, boolean isNull,
            boolean isDate, String dateFormat, String defaultValue) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.columnName = columnName;
        this.columnType = columnType;
        this.isNull = isNull;
        this.isDate = isDate;
        this.dateFormat = dateFormat;
        this.defaultValue = defaultValue;
    }

    public boolean isNull() {
        return isNull;
    }

    public void setNull(boolean isNull) {
        this.isNull = isNull;
    }

    public boolean isDate() {
        return isDate;
    }

    public void setDate(boolean isDate) {
        this.isDate = isDate;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Class<?> getFieldType() {
        return fieldType;
    }

    public void setFieldType(Class<?> fieldType) {
        this.fieldType = fieldType;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    @Override
    public String toString() {
        return "columnName:" + this.columnName + ";columnType:" + this.columnType + ";isNull:" + this.isNull
                + ";isDate:" + this.isDate + ";dateFormat:" + this.dateFormat + ";defaultValue:" + this.defaultValue;
    }

    public Method getSetMethod() {
        return setMethod;
    }

    public void setSetMethod(Method setMethod) {
        this.setMethod = setMethod;
    }

    public Method getGetMethod() {
        return getMethod;
    }

    public void setGetMethod(Method getMethod) {
        this.getMethod = getMethod;
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
