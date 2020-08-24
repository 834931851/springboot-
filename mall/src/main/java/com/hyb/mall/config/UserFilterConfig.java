package com.hyb.mall.config;

import com.hyb.mall.filter.AdminFilter;
import com.hyb.mall.filter.UserFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 描述：用户过滤器的配置
 */
@Configuration
public class UserFilterConfig {
    @Bean
    public UserFilter userFilter(){
        return new UserFilter();
    }

    //将整个filter放到整个链路中去
    @Bean(name = "userFilterConf") //设置的名字不能和类名一样不然会有冲突
    public FilterRegistrationBean adminFilterConfig(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(userFilter());
        //设置拦截的URL
        filterRegistrationBean.addUrlPatterns("/cart/*");
        filterRegistrationBean.addUrlPatterns("/order/*");
        //给过滤器配置设置名字，以便于区分不同的名字
        filterRegistrationBean.setName("UserFilterConfig");
        return filterRegistrationBean;
    }
}
