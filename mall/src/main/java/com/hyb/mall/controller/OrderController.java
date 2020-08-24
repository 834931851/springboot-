package com.hyb.mall.controller;

import com.github.pagehelper.PageInfo;
import com.hyb.mall.common.ApiRestResponse;
import com.hyb.mall.model.request.CreateOrderReq;
import com.hyb.mall.model.request.OrderVO;
import com.hyb.mall.service.OrderSrvice;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 描述：订单控制器
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderSrvice orderSrvice;

    @PostMapping("/create")
    @ApiOperation("创建订单")
    public ApiRestResponse create(@RequestBody CreateOrderReq createOrderReq){
        String orderNo = orderSrvice.create(createOrderReq);
        return ApiRestResponse.success(orderNo);
    }

    @GetMapping("/detail")
    @ApiOperation("前台订单详情")
    public ApiRestResponse detail(@RequestParam String orderNo){
        OrderVO orderVO = orderSrvice.detail(orderNo);
        return ApiRestResponse.success(orderVO);
    }

    @GetMapping("/list")
    @ApiOperation("前台订单列表")
    public ApiRestResponse list(@RequestParam Integer pageNum ,@RequestParam Integer pageSize){
        PageInfo pageInfo = orderSrvice.listForCustomer(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    @PostMapping("/cancel")
    @ApiOperation("前台取消订单")
    public ApiRestResponse cancel(@RequestParam String orderNo){
        orderSrvice.cancel(orderNo);
        return ApiRestResponse.success();
    }
}
