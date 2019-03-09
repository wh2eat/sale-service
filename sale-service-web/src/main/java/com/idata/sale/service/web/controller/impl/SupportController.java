package com.idata.sale.service.web.controller.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.idata.sale.service.web.controller.BaseController;
import com.idata.sale.service.web.controller.LoginUserCheck;

@Controller
public class SupportController extends BaseController {

    private final static Logger LOGGER = LoggerFactory.getLogger(SupportController.class);

    public SupportController() {
        // TODO Auto-generated constructor stub
    }

    @LoginUserCheck
    @RequestMapping(path = { "support/index" }, method = RequestMethod.GET)
    public String index(@RequestParam(name = "target", required = false) String target,
            @RequestParam(name = "uid", required = false) String uid,
            @RequestParam(name = "id", required = false) Integer id) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.info("[][][target:" + target + "]");
        }

        if ("customerList".equals(target)) {
            return "support/customerSuggestionList";
        }
        else if ("detail".equals(target)) {
            getRequest().setAttribute("uid", uid);
            getRequest().setAttribute("id", id);
            return "support/customerSuggestionDetail";
        }
        return "support/index";

    }

}
