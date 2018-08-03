package com.idata.sale.service.web.base.service;

import java.util.List;

import com.idata.sale.service.web.base.dao.dbo.RepairPackageDbo;

public interface IRepairSearchService {

    public List<RepairPackageDbo> searchBySn(String sn);

    public List<RepairPackageDbo> searchByExpressNumber(String expressNumber);

}
