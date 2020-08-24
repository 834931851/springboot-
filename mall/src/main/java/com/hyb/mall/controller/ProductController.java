package com.hyb.mall.controller;

import com.github.pagehelper.PageInfo;
import com.hyb.mall.common.ApiRestResponse;
import com.hyb.mall.model.dao.ProductMapper;
import com.hyb.mall.model.pojo.Product;
import com.hyb.mall.model.request.ProductListReq;
import com.hyb.mall.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述 ： 前台商品controller
 */
@RestController
public class ProductController {
    @Autowired
    ProductService productService;

    @ApiOperation("前台商品详情")
    @PostMapping("/product/detail")
    public ApiRestResponse detail(@RequestParam Integer id){
        Product detail = productService.detail(id);
        return ApiRestResponse.success(detail);
    }

    @ApiOperation("前台商品列表")
    @PostMapping("/product/list")
    public ApiRestResponse list(ProductListReq productListReq){
        PageInfo list = productService.list(productListReq);
        return ApiRestResponse.success(list);
    }
}
