package com.idata.sale.service.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.idata.sale.service.web.base.dao.dbo.SystemUserDbo;

@Aspect
@Configuration
public class LoginUserCheckAspect {

    private final static Logger LOGGER = LoggerFactory.getLogger(LoginUserCheckAspect.class);

    public LoginUserCheckAspect() {
    }

    @Pointcut("@annotation(com.idata.sale.service.web.controller.LoginUserCheck)")
    public void excudeService() {
    }

    @Around("excudeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][LoginUserAspect][run]");
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();

        Object loginUserObj = request.getSession().getAttribute(SessionAttr.LoginUser.getName());

        try {
            if (null != loginUserObj
                    || loginUserObj instanceof SystemUserDbo && null == ((SystemUserDbo) loginUserObj).getId()) {
                return pjp.proceed();
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][LoginUserAspect][loginUser session has invalid]");
            }

            return "redirect:/";
        }
        finally {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][LoginUserAspect][end]");
            }
        }

    }

}
