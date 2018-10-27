package com.openaidata.thymeleaf;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.openaidata.thymeleaf.common.filter.AccessRecorderFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class ThymeleafApplication {

    @Bean
    @ConfigurationProperties(prefix="spring.datasource.druid")
    public DataSource druid() {
        DruidDataSource ds = new DruidDataSource();
        return ds;
    }

    //注册后台界面Servlet Bean，用于显示后台界面
    @Bean
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        Map<String, String> param = new HashMap<String, String>();
        param.put("loginUsername", "admin");
        param.put("loginPassword", "123456");
        param.put("allow", "127.0.0.1");
        param.put("deny", "33.31.51.88");
        servletRegistrationBean.setInitParameters(param);
        return servletRegistrationBean;

    }

    //用于监听获取应用的数据， Filter用于数据收集，Servlet用于展现数据
    @Bean
    public FilterRegistrationBean webStatFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        Map<String, String> param = new HashMap<String,String>();

        param.put("exclusions", "*.png,*.woff,*.js,*.css, /druid/*");
        filterRegistrationBean.setInitParameters(param);
        return filterRegistrationBean;

    }

    @Bean
    public FilterRegistrationBean filterRegiste() {
        FilterRegistrationBean regFilter = new FilterRegistrationBean();
        regFilter.setFilter(new AccessRecorderFilter());
        regFilter.addUrlPatterns("/*");
        regFilter.setName("AccessRecorder");
        regFilter.setOrder(1);
        return regFilter;
    }

    public static void main(String[] args) {
        SpringApplication.run(ThymeleafApplication.class, args);
    }
}
