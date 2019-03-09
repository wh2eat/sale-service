package com.idata.sale.service.web.rest.browser.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.dbo.CustomerSuggestionDbo;
import com.idata.sale.service.web.base.service.ICustomerSuggestionService;
import com.idata.sale.service.web.base.service.dto.CustomerSuggestionDto;
import com.idata.sale.service.web.rest.RestResultFactory;

@RestController
@RequestMapping(path = "/api/browser/customer/suggestion", produces = { MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_FORM_URLENCODED_VALUE })
public class CustomerSuggestionRestService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CustomerSuggestionRestService.class);

    public CustomerSuggestionRestService() {

    }

    @Autowired
    private ICustomerSuggestionService suggestionService;

    @PostMapping(path = "save")
    public Object save(@RequestBody CustomerSuggestionDto suggestionDto) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][save][" + suggestionDto + "]");
        }

        suggestionService.add(suggestionDto);

        return true;
    }

    @RequestMapping(path = "find/list", method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public Object findList(@RequestParam("page") int pageNum, @RequestParam("limit") int limit,
            @RequestParam("params") String params) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][findList][" + pageNum + "]");
        }

        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(limit);

        List<CustomerSuggestionDbo> list = suggestionService.findList(pageInfo);

        try {
            return RestResultFactory.getPageResult(list,
                    null != pageInfo ? pageInfo.getTotal() : (null != list ? list.size() : 0));
        }
        finally {
            pageInfo = null;
        }
    }

    @PostMapping(path = "get/detail")
    public Object getDetail(@RequestBody CustomerSuggestionDto suggestionDto) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][save][" + suggestionDto + "]");
        }

        if (null == suggestionDto) {
            return null;
        }

        if (null == suggestionDto.getUid()) {
            return null;
        }

        if (null == suggestionDto.getId() || suggestionDto.getId() < 1) {
            return null;
        }
        return suggestionService.get(suggestionDto.getUid(), suggestionDto.getId());
    }

}
