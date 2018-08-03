package com.idata.sale.service.web;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class SystemAsyncConfigurer implements AsyncConfigurer {

    private final static Logger LOGGER = LoggerFactory.getLogger(SystemAsyncConfigurer.class);

    public SystemAsyncConfigurer() {

    }

    @Override
    public Executor getAsyncExecutor() {
        LOGGER.info("[][getAsyncExecutor][start]");
        try {
            ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
            threadPool.setCorePoolSize(1);
            threadPool.setMaxPoolSize(1);
            threadPool.setWaitForTasksToCompleteOnShutdown(true);
            threadPool.setAwaitTerminationSeconds(60 * 15);
            threadPool.setThreadNamePrefix("Async-");
            threadPool.initialize();

            return threadPool;
        }
        finally {
            LOGGER.info("[][getAsyncExecutor][finish]");
        }

    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SystemAsyncExceptionHandler();
    }

    /**
     * 自定义异常处理类
     * @author hry
     *
     */
    private class SystemAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

        public SystemAsyncExceptionHandler() {
        }

        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... objs) {

            LOGGER.info("=========== SystemAsync UncaughtException start ================");
            LOGGER.info("Exception message - " + throwable.getMessage());
            LOGGER.info("Method name - " + method.getName());
            for (Object param : objs) {
                LOGGER.info("Parameter value - " + param);
            }
            LOGGER.info("=========== SystemAsync UncaughtException end ================");
        }

    }

}
