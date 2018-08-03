package com.idata.sale.service.web;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.idata.sale.service.web.filter.EncodeFilter;
import com.idata.sale.service.web.util.TimeUtil;

@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

    private final static Logger LOGGER = LoggerFactory.getLogger(WebMvcConfiguration.class);

    public WebMvcConfiguration() {

    }

    @Bean
    public FilterRegistrationBean getEncodeFilterBean() {
        EncodeFilter encodeFilter = new EncodeFilter();

        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(encodeFilter);
        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/*");
        registrationBean.setUrlPatterns(urlPatterns);
        registrationBean.setOrder(1);

        return registrationBean;
    }

    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        // 1.需要先定义一个converters转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

        // 2.添加fastjson的配置信息，比如: 是否需要格式化返回的json数据
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        fastJsonConfig.setDateFormat(TimeUtil._yyyyMMddHHmmss);
        fastJsonConfig.setCharset(Charset.forName("utf-8"));
        fastJsonConfig.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect);

        // 3.在converter中添加配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);

        // 4.将converter赋值给HttpMessageConverter
        HttpMessageConverter<?> converter = fastConverter;

        // 5.返回HttpMessageConverters对象
        return new HttpMessageConverters(converter);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        // registry.addInterceptor(new
        // DeviceTokenInterceptor()).addPathPatterns("/api/device/*");
        super.addInterceptors(registry);
    }

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        return converter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.add(responseBodyConverter());
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }

}
