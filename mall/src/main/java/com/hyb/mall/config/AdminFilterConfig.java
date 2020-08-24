package com.hyb.mall.config;

import com.hyb.mall.filter.AdminFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 描述：管理员过滤器的配置
 */
@Configuration
public class AdminFilterConfig {
    @Bean
    public AdminFilter adminFilter(){
        return new AdminFilter();
    }

    //将整个filter放到整个链路中去
    @Bean(name = "adminFilterConf") //设置的名字不能和类名一样不然会有冲突
    public FilterRegistrationBean adminFilterConfig(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(adminFilter());
        //设置拦截的URL
        filterRegistrationBean.addUrlPatterns("/admin/category/*");
        filterRegistrationBean.addUrlPatterns("/admin/product/*");
        filterRegistrationBean.addUrlPatterns("/admin/order/*");
        //给过滤器配置设置名字，以便于区分不同的名字
        filterRegistrationBean.setName("adminFilterConfig");
        return filterRegistrationBean;
    }
}
