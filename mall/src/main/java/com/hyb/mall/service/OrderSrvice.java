package com.hyb.mall.service;

import com.github.pagehelper.PageInfo;
import com.hyb.mall.model.request.CreateOrderReq;
import com.hyb.mall.model.request.OrderVO;
import com.hyb.mall.model.vo.CartVO;

import java.util.List;

/**
 * 描述：订单service
 */
public interface OrderSrvice {

    String create(CreateOrderReq createOrderReq);

    //查询订单详情
    OrderVO detail(String orderNo);

    PageInfo listForCustomer(Integer pageNum, Integer pageSize);

    void cancel(String orderNo);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    String qrcode(String orderNo);

    void pay(String orderNo);

    void delivered(String orderNo);

    void finish(String orderNo);
}
