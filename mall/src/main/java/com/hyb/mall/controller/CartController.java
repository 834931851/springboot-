package com.hyb.mall.controller;

import com.hyb.mall.common.ApiRestResponse;
import com.hyb.mall.filter.UserFilter;
import com.hyb.mall.model.vo.CartVO;
import com.hyb.mall.service.CartSrvice;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述：购物车controller
 */
@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    CartSrvice cartSrvice;

    @ApiOperation("商品购物车列表")
    @GetMapping("/list")
    public ApiRestResponse list() {
        //内部获取用户ID，防止横向越权
        List<CartVO> cartList = cartSrvice.list(UserFilter.currentUser.getId());
        return ApiRestResponse.success(cartList);

    }

    @ApiOperation("添加商品到购物车")
    @PostMapping("/add")
    public ApiRestResponse add(@RequestParam Integer productId,
                               @RequestParam Integer count) {
        List<CartVO> cartVOList = cartSrvice.add(UserFilter.currentUser.getId(), productId, count);
        return ApiRestResponse.success(cartVOList);
    }

    @ApiOperation("更新购物车")
    @PostMapping("/update")
    public ApiRestResponse update(@RequestParam Integer productId,
                               @RequestParam Integer count) {
        List<CartVO> cartVOList = cartSrvice.update(UserFilter.currentUser.getId(), productId, count);
        return ApiRestResponse.success(cartVOList);
    }

    @ApiOperation("删除购物车")
    @PostMapping("/delete")
    public ApiRestResponse delete(@RequestParam Integer productId) {
        //不能传入userID和cartID，防止黑客攻击
        List<CartVO> cartVOList = cartSrvice.delete(UserFilter.currentUser.getId(), productId);
        return ApiRestResponse.success(cartVOList);
    }

    @ApiOperation("单选和反选购物车商品")
    @PostMapping("/select")
    public ApiRestResponse select(@RequestParam Integer productId,
                                  @RequestParam Integer selected) {
        //不能传入userID和cartID，防止黑客攻击
        List<CartVO> cartVOList = cartSrvice.selectOrNot(UserFilter.currentUser.getId(), productId,selected);
        return ApiRestResponse.success(cartVOList);
    }

    @ApiOperation("全选和全不选购物车商品")
    @PostMapping("/selectAll")
    public ApiRestResponse selectAll(@RequestParam Integer selected) {
        //不能传入userID和cartID，防止黑客攻击
        List<CartVO> cartVOList = cartSrvice.selectAllOrNot(UserFilter.currentUser.getId(),selected);
        return ApiRestResponse.success(cartVOList);
    }
}
