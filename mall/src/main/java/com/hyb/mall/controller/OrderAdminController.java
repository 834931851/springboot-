package com.hyb.mall.controller;

import com.github.pagehelper.PageInfo;
import com.hyb.mall.common.ApiRestResponse;
import com.hyb.mall.service.OrderSrvice;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderAdminController {
    @Autowired
    OrderSrvice orderSrvice;
    @GetMapping("/admin/order/list")
    @ApiOperation("管理员订单列表")
    public ApiRestResponse listForAdmin(@RequestParam Integer pageNum,@RequestParam Integer pageSize){
        PageInfo pageInfo = orderSrvice.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    @PostMapping("/admin/qrcode")
    @ApiOperation("生成二维码")
    public ApiRestResponse listForAdmin(@RequestParam String orderNo){
        String pngAddress = orderSrvice.qrcode(orderNo);
        return ApiRestResponse.success(pngAddress);
    }

    @GetMapping("/pay")
    @ApiOperation("前台：支付订单")
    public ApiRestResponse pay(@RequestParam String orderNo){
        orderSrvice.pay(orderNo);
        return ApiRestResponse.success();
    }

    @PostMapping("/admin/order/delivered")
    @ApiOperation("后台：订单发货")
    public ApiRestResponse delivered(@RequestParam String orderNo){
        orderSrvice.delivered(orderNo);
        return ApiRestResponse.success();
    }

    @PostMapping("/order/finish")
    @ApiOperation("订单完结")
    public ApiRestResponse finish(@RequestParam String orderNo){
        orderSrvice.finish(orderNo);
        return ApiRestResponse.success();
    }
}
