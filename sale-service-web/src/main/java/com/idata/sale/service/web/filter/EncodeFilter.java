package com.idata.sale.service.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang.math.RandomUtils;

public class EncodeFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    private final static String jsVersion = String.valueOf(RandomUtils.nextInt(10000));

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        request.setCharacterEncoding("utf-8");
        request.setAttribute("jsVersion", jsVersion);

        response.setCharacterEncoding("utf-8");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

}
