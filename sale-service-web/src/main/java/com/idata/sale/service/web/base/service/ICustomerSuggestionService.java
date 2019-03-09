package com.idata.sale.service.web.base.service;

import java.util.List;

import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.dbo.CustomerSuggestionDbo;
import com.idata.sale.service.web.base.service.dto.CustomerSuggestionDto;

public interface ICustomerSuggestionService {

    public CustomerSuggestionDbo get(String uuid, Integer id);

    public int add(CustomerSuggestionDto suggestionDto);

    public List<CustomerSuggestionDbo> findList(PageInfo pageInfo);

}
