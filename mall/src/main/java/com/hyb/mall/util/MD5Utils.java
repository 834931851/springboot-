package com.hyb.mall.util;

import com.hyb.mall.common.Constant;
import org.apache.tomcat.util.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 描述：MD5加密工具
 */
public class MD5Utils {
    public static String getMD5Str(String strValue) throws NoSuchAlgorithmException {
        //传入算法MD5
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        //传入的参数还需要进行base64的转码，方便存储
        return Base64.encodeBase64String(md5.digest((strValue + Constant.SALT).getBytes()));
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String md5 = getMD5Str("1234");
        System.out.println(md5);
    }
}
