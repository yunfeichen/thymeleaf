package com.openaidata.thymeleaf.common.filter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

public class AccessRecorderFilter implements Filter {
    private Logger logger = LoggerFactory.getLogger(AccessRecorderFilter.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletReuest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletReuest;
        String uri = request.getRequestURI();
        if(uri.endsWith(".css") || uri.endsWith(".js") || uri.endsWith(".map") || uri.endsWith(".jpg") || uri.endsWith(".png") || uri.endsWith(".woff") || uri.endsWith(".woff2")) {
            filterChain.doFilter(servletReuest, servletResponse);
            return;
        }
        String ua = request.getHeader("user-agent");
        String ip = request.getRemoteAddr();

        Long st = new Date().getTime();
        filterChain.doFilter(servletReuest, servletResponse);
        Long et = new Date().getTime();
        logger.info("uri:{},ip:{},time:{},ua:{}", uri, ip, (et-st), ua);
    }

    @Override
    public void destroy() {

    }
}
