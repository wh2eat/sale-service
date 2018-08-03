package com.idata.sale.service.web.rest.device;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.idata.sale.service.web.base.service.ISystemUserTokenService;
import com.idata.sale.service.web.rest.RestCode;
import com.idata.sale.service.web.rest.RestHeader;
import com.idata.sale.service.web.rest.RestResultFactory;

@Aspect
@Configuration
@ConfigurationProperties
public class DeviceTokenCheckAspect {

    private final static Logger LOGGER = LoggerFactory.getLogger(DeviceTokenCheckAspect.class);

    @Value("${com.idt.ss.run.environment}")
    private String environment;

    public DeviceTokenCheckAspect() {
    }

    @Pointcut("@annotation(com.idata.sale.service.web.rest.device.DeviceTokenCheck)")
    public void excudeService() {
    }

    @Autowired
    private ISystemUserTokenService userTokenService;

    private final static String enviroment_local = "local";

    @Around("excudeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();

        if (LOGGER.isDebugEnabled()) {
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                LOGGER.debug("[][][" + headerName + "=" + request.getHeader(headerName) + "]");
            }
            LOGGER.debug("[][][environment:" + environment + "]");
        }

        if (enviroment_local.equals(environment)) {
            return pjp.proceed();
        }

        String ruri = request.getRequestURI();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[DeviceTokenAspect][doBefore][request uri:" + ruri + "]");
        }

        String token = request.getHeader(RestHeader.DeviceToken.getName());
        if (StringUtils.isEmpty(token)) {
            return RestResultFactory.getResult(RestCode.TokenTimeoutError, "not found token", -1);
        }
        else if (!userTokenService.exist(token) || userTokenService.hasTimeout(token)) {
            return RestResultFactory.getResult(RestCode.TokenTimeoutError, "token timeout", -1);
        }
        return pjp.proceed();
    }

}
