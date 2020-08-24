package com.hyb.mall.controller;

import com.hyb.mall.common.ApiRestResponse;
import com.hyb.mall.common.Constant;
import com.hyb.mall.exception.MallException;
import com.hyb.mall.exception.MallExceptionEnum;
import com.hyb.mall.model.pojo.User;
import com.hyb.mall.service.UserService;
import com.hyb.mall.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;

/**
 * 描述：用户控制器
 */
@Controller
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/test")
    @ResponseBody
    public User personalPage() {
        return userService.getUser();
    }

    /**
     * 注册
     * @param userName
     * @param password
     * @return
     * @throws MallException
     */
    @PostMapping("/register")
    @ResponseBody
    public ApiRestResponse register(@RequestParam("userName") String userName,
                                    @RequestParam("password") String password) throws MallException {
        //1.校验：判断用户名是否为空
        if (StringUtils.isEmpty(userName)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_USER_NAME);
        }
        //2.校验：判断密码是否为空
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_PASSWORD);
        }
        //3.密码长度不少于8位
        if (password.length() < 8) {
            return ApiRestResponse.error(MallExceptionEnum.PASSWORD_TO_SHORT);
        }
        //4.调用useService的注册方法，将用户名和密码写入数据库
        userService.register(userName, password);

        //5.写入成功后返回成功
        return ApiRestResponse.success();
    }

    /**
     * 登录
     * @param userName
     * @param password
     * @param session
     * @return
     * @throws MallException
     */
    @PostMapping("/login")
    @ResponseBody
    public ApiRestResponse login(@RequestParam("userName") String userName,
                                 @RequestParam("password") String password,
                                 HttpSession session) throws MallException {
        //1.校验：判断用户名是否为空
        if (StringUtils.isEmpty(userName)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_USER_NAME);
        }
        //2.校验：判断密码是否为空
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(userName, password);
        //为了安全，保存用户信息时，不将密码进行保存，返回null
        user.setPassword(null);
        //将用户信息存在session中
        session.setAttribute(Constant.HYB_MALL_USER, user);
        return ApiRestResponse.success(user);
    }

    /**
     * 更新个性签名，验证session是否进行存储
     * @param session
     * @param signature
     * @return
     * @throws MallException
     */
    @PostMapping("/user/update")
    @ResponseBody
    public ApiRestResponse updateUserInfo(HttpSession session,
                                          @RequestParam String signature) throws MallException {
        User currentUser = (User) session.getAttribute(Constant.HYB_MALL_USER);
        if (currentUser == null) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_LOGIN);
        }
        User user = new User();
        user.setId(currentUser.getId());
        user.setPersonalizedSignature(signature);
        userService.updateInformation(user);
        return ApiRestResponse.success();
    }

    /**
     * 登出并且清除session
     * @param session
     * @return
     */
    @PostMapping("/user/logout")
    @ResponseBody
    public ApiRestResponse logout(HttpSession session) {
        session.removeAttribute(Constant.HYB_MALL_USER);
        return ApiRestResponse.success();
    }

    /**
     * 管理员登录接口
     * @param userName
     * @param password
     * @param session
     * @return
     * @throws MallException
     */
    @PostMapping("/adminLogin")
    @ResponseBody
    public ApiRestResponse adminlogin(@RequestParam("userName") String userName,
                                 @RequestParam("password") String password,
                                 HttpSession session) throws MallException {
        //1.校验：判断用户名是否为空
        if (StringUtils.isEmpty(userName)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_USER_NAME);
        }
        //2.校验：判断密码是否为空
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(userName, password);
        //判断登录的用户的身份是否是管理员
        if (userService.checkAdminRole(user)) {
            //为了安全，保存用户信息时，不将密码进行保存，返回null
            user.setPassword(null);
            //将用户信息存在session中
            session.setAttribute(Constant.HYB_MALL_USER, user);
            return ApiRestResponse.success(user);
        }else {
            return ApiRestResponse.error(MallExceptionEnum.NEED_ADMIN);
        }


    }

}
