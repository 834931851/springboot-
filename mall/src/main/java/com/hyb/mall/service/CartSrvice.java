package com.hyb.mall.service;

import com.hyb.mall.model.vo.CartVO;

import java.awt.*;
import java.util.List;

/**
 * 描述：购物车service
 */
public interface CartSrvice {

    List<CartVO> list(Integer userId);

    List<CartVO> add(Integer userId, Integer productId, Integer count);

    //更新购物车
    List<CartVO> update(Integer userId, Integer productId, Integer count);

    //删除购物车商品
    List<CartVO> delete(Integer userId, Integer productId);


    //选择商品
    List<CartVO> selectOrNot(Integer userId, Integer productId, Integer selected);

    //全选和全不选购物车商品
    List<CartVO> selectAllOrNot(Integer userId, Integer selected);
}
