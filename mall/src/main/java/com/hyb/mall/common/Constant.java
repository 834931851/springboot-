package com.hyb.mall.common;


import com.google.common.collect.Sets;
import com.hyb.mall.exception.MallException;
import com.hyb.mall.exception.MallExceptionEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 描述：常量
 */
@Component
public class Constant {
    //自定义常量，越复杂越好，和MD5混合使用，避免MD5反破解
    public static final String SALT = "82ps[d]][sffs.a";
    public static final String HYB_MALL_USER = "hyb_mall_user";

    //因为存在静态的static，用普通的方式进行处理，是注入不进去的 在进行图片上传的时候回报错
    public static String FILE_UPLOAD_DIR;

    @Value("${file.upload.dir}")
    public void setFileUploadDir(String fileUploadDir) {
        FILE_UPLOAD_DIR = fileUploadDir;
    }

    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price desc","price asc");
    }

    /**
     * 增加商品的上下架状态
     */
    public interface SaleStatus{
        int NOT_SALE = 0;//商品的下架状态
        int SALE = 1;//商品的上架状态
    }

    /**
     * 购物车是否选中状态
     */
    public interface Cart{
        int UN_CHCKED = 0;//购物车未选中
        int CHCKED = 1;//购物车选中
    }

    /**
     * 订单状态
     */
    public enum OrderStatusEnum{
        CANCELED(0,"用户已取消"),
        NOT_PAID(10,"未付款"),
        PAID(20,"已付款"),
        DELIVERED(30,"已发货"),
        FINISHED(40,"交易完成");
        private String value;
        private int code;

        OrderStatusEnum(int code,String value) {
            this.value = value;
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        //通过code返回意思
        public static OrderStatusEnum cedeOf(int code){
            for(OrderStatusEnum orderStatusEnum:values()){
                if (orderStatusEnum.getCode() == code){
                    return orderStatusEnum;
                }
            }
            throw new MallException(MallExceptionEnum.NO_ENUM);
        }
    }
}
