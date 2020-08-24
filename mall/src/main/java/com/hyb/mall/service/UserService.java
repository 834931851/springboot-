package com.hyb.mall.service;

import com.hyb.mall.exception.MallException;
import com.hyb.mall.model.pojo.User;

/**
 * 描述：UserService
 */
public interface UserService {
    User getUser();

    //用户注册
    void register(String userName, String password) throws MallException;

    //用户登录
    User login(String userName, String password) throws MallException;

    //更新签名
    void updateInformation(User user) throws MallException;

    //校验是否管理员
    boolean checkAdminRole(User user);
}
