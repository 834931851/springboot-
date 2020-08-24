package com.hyb.mall.service.impl;

import com.hyb.mall.exception.MallException;
import com.hyb.mall.exception.MallExceptionEnum;
import com.hyb.mall.model.dao.UserMapper;
import com.hyb.mall.model.pojo.User;
import com.hyb.mall.service.UserService;
import com.hyb.mall.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

/**
 * 描述：UserService实现类
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public User getUser() {
        return userMapper.selectByPrimaryKey(1);
    }

    @Override
    public void register(String userName, String password) throws MallException {
        //1.查询用户名是否存在，不允许重名
        User result = userMapper.selectByName(userName);
        if (result != null) {
            throw new MallException(MallExceptionEnum.NAME_EXISTED);
        }
        //2.将用户名和密码写入数据库
        User user = new User();
        user.setUsername(userName);
        //使用MD5加密对密码进行加密处理
        try {
            user.setPassword(MD5Utils.getMD5Str(password));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //直接输入密码不进行处理，存在安全隐患
//        user.setPassword(password);
        int count = userMapper.insertSelective(user);
        if (count == 0) {
            throw new MallException(MallExceptionEnum.INSERT_FAILED);
        }
    }
    @Override
    public User login(String userName, String password) throws MallException {
        String md5Password = null;
        try {
            md5Password = MD5Utils.getMD5Str(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        User user = userMapper.selectLogin(userName,md5Password);
        if(user == null){
            throw new MallException(MallExceptionEnum.WRONG_PASSWORD);
        }
        return user;
    }
    @Override
    public void updateInformation(User user) throws MallException {
        //更新个性签名
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount>1){
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        }
    }
    @Override
    public boolean checkAdminRole(User user){
        //1是普通用户 2是管理员
        return user.getRole().equals(2);
    }
}
