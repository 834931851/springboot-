package com.hyb.mall.filter;
import com.hyb.mall.common.Constant;
import com.hyb.mall.model.pojo.User;
import com.hyb.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 描述：用户校验过滤器
 * 登录后做更深层的校验，可以拿到用户信息
 */
public class UserFilter implements Filter {
    //登录后，能将用户信息保存下来
    public static User currentUser;

    @Autowired
    private UserService userService;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest servletRequest, 
                         ServletResponse servletResponse, 
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        //1.获取session中的用户
        currentUser = (User) session.getAttribute(Constant.HYB_MALL_USER);
        //2.判断当前用户是否为空，为空则需要登录
        if (currentUser == null) {
            PrintWriter out = new HttpServletResponseWrapper((HttpServletResponse)servletResponse).getWriter();
            out.write("{\n" +
                    "    \"status\": 10007,\n" +
                    "    \"msg\": \"NEED_LOGIN\",\n" +
                    "    \"data\": null\n" +
                    "}");
            out.flush();
            out.close();
            return;
        }
        //3.通过用户校验
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {}
}
