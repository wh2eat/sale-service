package com.idata.sale.service.web.base.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseDaoConfiguration {

    @Value("${com.idt.ss.base.dao.batch.update.size}")
    private int batchUpdateSize;

    @Value("${com.idt.ss.base.dao.find.page.size}")
    private int defaultPageSize;

    public int getDefaultPageSize() {
        return defaultPageSize;
    }

    public void setDefaultPageSize(int defaultPageSize) {
        this.defaultPageSize = defaultPageSize;
    }

    public int getBatchUpdateSize() {
        return batchUpdateSize;
    }

    public void setBatchUpdateSize(int batchUpdateSize) {
        this.batchUpdateSize = batchUpdateSize;
    }

}
