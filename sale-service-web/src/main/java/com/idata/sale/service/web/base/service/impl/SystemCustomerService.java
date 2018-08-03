package com.idata.sale.service.web.base.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.idata.sale.service.web.base.dao.BaseDaoException;
import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.dbo.RepairInvoiceDbo;
import com.idata.sale.service.web.base.dao.dbo.SystemCustomerDbo;
import com.idata.sale.service.web.base.dao.impl.SystemCustomerDao;
import com.idata.sale.service.web.base.service.ISystemCustomerService;
import com.idata.sale.service.web.base.service.ServiceException;

@Service
public class SystemCustomerService implements ISystemCustomerService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SystemCustomerService.class);

    public SystemCustomerService() {
        // TODO Auto-generated constructor stub
    }

    @Autowired
    private SystemCustomerDao customerDao;

    @Override
    public void add(SystemCustomerDbo customerDbo) throws ServiceException {

        String allInfo = customerDbo.getName() + "&" + customerDbo.getPhone() + "&" + customerDbo.getAddress();
        String identifer = DigestUtils.md5Hex(allInfo);

        SystemCustomerDbo dbCustomerDbo = customerDao.getByIdentifier(identifer);

        if (null == dbCustomerDbo) {

            Date now = new Date(System.currentTimeMillis());
            customerDbo.setCreateTime(now);
            customerDbo.setUpdateTime(now);
            customerDbo.setIdentifier(identifer);

            try {
                long id = customerDao.save(customerDbo);
                LOGGER.info("[][add][success,id:" + id + "," + customerDbo + "]");
            }
            catch (BaseDaoException e) {
                LOGGER.error("[][][save SystemCustomerDbo failed," + customerDbo + "]", e);
            }
        }
    }

    @Override
    public List<SystemCustomerDbo> list(PageInfo pageInfo, SystemCustomerDbo filterDbo) throws ServiceException {
        return customerDao.findList(pageInfo, filterDbo);
    }

    @Async
    @Override
    public void addAsync(RepairInvoiceDbo repairInvoice) throws ServiceException {

        SystemCustomerDbo customerDbo = new SystemCustomerDbo();
        customerDbo.setName(repairInvoice.getContacts());
        customerDbo.setPhone(repairInvoice.getContactNumber());
        customerDbo.setAddress(repairInvoice.getContactAddress());
        customerDbo.setEmail(repairInvoice.getEmail());
        add(customerDbo);
        customerDbo = null;
    }

}
