package com.idata.sale.service.web.base.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    public String jdbcName() default "";

    public String jdbcType() default "";

    public boolean isNull() default false;

    public String defaultValue() default "";

    public boolean isDate() default false;

    public String dateFormat() default "yyyy-MM-dd hh:MM:ss";
}
