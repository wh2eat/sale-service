package com.idata.sale.service.web.rest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Configuration
public class RestResultAspect {

    private final static Logger LOGGER = LoggerFactory.getLogger(RestResultAspect.class);

    public RestResultAspect() {
        // TODO Auto-generated constructor stub
    }

    // 定义切点Pointcut
    @Pointcut("execution(* com.idata.sale.service.web.rest.*.impl.*Service.*(..))")
    public void excudeService() {
    }

    @Around("excudeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {

        String ruri = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
                .getRequestURI();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][][request uri:" + ruri + "]");
        }

        long startMillis = System.currentTimeMillis();

        Object result = pjp.proceed();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][doAround][" + result + "]");
        }

        // 分页数据返回格式单独处理
        if (null != result && result instanceof RestPageResult) {
            return result;
        }

        if (null != result && result instanceof RestResult) {
            return result;
        }

        return RestResultFactory.getResult(result, (System.currentTimeMillis() - startMillis));
    }

}
