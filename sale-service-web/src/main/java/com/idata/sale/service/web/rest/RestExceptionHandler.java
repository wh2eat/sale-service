package com.idata.sale.service.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.idata.sale.service.web.base.service.ServiceException;

@RestControllerAdvice
@ResponseBody
public class RestExceptionHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

    public RestExceptionHandler() {
        // TODO Auto-generated constructor stub
    }

    // Rest异常
    @ExceptionHandler(RestException.class)
    public RestResult runtimeExceptionHandler(RestException e) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.error("runtimeExceptionHandler", e);
        }
        return RestResultFactory.getResult(e.getCode(), "[" + e.getCode().getDesc() + "][" + e.getMessage() + "]", -1l);
    }

    // 业务时异常
    @ExceptionHandler(ServiceException.class)
    public RestResult runtimeExceptionHandler(ServiceException e) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.error("runtimeExceptionHandler", e);
        }
        return RestResultFactory.getResult(RestCode.ServiceException,
                "[" + e.getCode().getCode() + "][" + e.getMessage() + "]", -1l);
    }

    // 业务时异常
    @ExceptionHandler(Exception.class)
    public RestResult runtimeExceptionHandler(Exception e) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.error("runtimeExceptionHandler", e);
        }

        return RestResultFactory.getResult(RestCode.ServiceException, "[" + e.getMessage() + "]", -1l);
    }

}
