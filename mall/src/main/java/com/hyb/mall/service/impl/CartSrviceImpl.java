package com.hyb.mall.service.impl;

import com.hyb.mall.common.Constant;
import com.hyb.mall.exception.MallException;
import com.hyb.mall.exception.MallExceptionEnum;
import com.hyb.mall.model.dao.CartMapper;
import com.hyb.mall.model.dao.ProductMapper;
import com.hyb.mall.model.pojo.Cart;
import com.hyb.mall.model.pojo.Product;
import com.hyb.mall.model.vo.CartVO;
import com.hyb.mall.service.CartSrvice;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 描述：购物车service实现类
 */
@Service
public class CartSrviceImpl implements CartSrvice {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    CartMapper cartMapper;

    @Override
    public List<CartVO> list(Integer userId) {
        List<CartVO> cartVOS = cartMapper.selectList(userId);
        //计算总价
        for (int i = 0; i < cartVOS.size(); i++) {
            CartVO cartVO = cartVOS.get(i);
            cartVO.setTotalPrice(cartVO.getPrice() * cartVO.getQuantity());
        }
        return cartVOS;
    }

    @Override
    public List<CartVO> add(Integer userId, Integer productId, Integer count) {
        validProduct(productId, count);
        Cart cart = cartMapper.selectCartByUserIdAndProduct(userId, productId);
        //判断查出来的cart是不是为空
        if (cart == null) {
            //这个商品之前不再购物车，需要新增一个记录
            cart = new Cart();
            cart.setProductId(productId);
            cart.setUserId(userId);
            cart.setQuantity(count);
            cart.setSelected(Constant.Cart.CHCKED);
            cartMapper.insertSelective(cart);
        } else {
            //这个商品已经在购物车里了，则数量相加
            count = cart.getQuantity() + count;
            Cart cartNew = new Cart();
            cartNew.setQuantity(count);
            cartNew.setId(cart.getId());
            cartNew.setProductId(cart.getProductId());
            cartNew.setUserId(cart.getUserId());
            cartNew.setSelected(Constant.Cart.CHCKED);
            cartMapper.updateByPrimaryKeySelective(cartNew);
        }
        return this.list(userId);
    }


    //验证添加是不是合法的
    private void validProduct(Integer productId, Integer count) {
        Product product = productMapper.selectByPrimaryKey(productId);
        //判断商品是否存在，商品是否上架
        if (product == null || product.getStatus().equals(Constant.SaleStatus.NOT_SALE)) {
            throw new MallException(MallExceptionEnum.NOT_SALE);
        }
        //判断商品库存
        if (count > product.getStock()) {
            throw new MallException(MallExceptionEnum.NOT_ENOUGH);
        }
    }

    //更新购物车商品
    @Override
    public List<CartVO> update(Integer userId, Integer productId, Integer count) {
        validProduct(productId, count);
        Cart cart = cartMapper.selectCartByUserIdAndProduct(userId, productId);
        //判断查出来的cart是不是为空
        if (cart == null) {
            //这个商品之前不再购物车，无法更新
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        } else {
            //这个商品已经在购物车里了，则更新数量
            Cart cartNew = new Cart();
            cartNew.setQuantity(count);
            cartNew.setId(cart.getId());
            cartNew.setProductId(cart.getProductId());
            cartNew.setUserId(cart.getUserId());
            cartNew.setSelected(Constant.Cart.CHCKED);
            cartMapper.updateByPrimaryKeySelective(cartNew);
        }
        return this.list(userId);
    }


    //删除购物车商品
    @Override
    public List<CartVO> delete(Integer userId, Integer productId) {
        Cart cart = cartMapper.selectCartByUserIdAndProduct(userId, productId);
        //判断查出来的cart是不是为空
        if (cart == null) {
            //这个商品之前不再购物车，无法删除
            throw new MallException(MallExceptionEnum.DELETE_FAILE);
        } else {
            //这个商品已经在购物车里了，则可以删除
            cartMapper.deleteByPrimaryKey(cart.getId());
        }
        return this.list(userId);
    }

    //单选和反选购物车的商品
    @Override
    public List<CartVO> selectOrNot(Integer userId, Integer productId, Integer selected) {
        //将购物车查询出来
        Cart cart = cartMapper.selectCartByUserIdAndProduct(userId, productId);
        //判断查出来的cart是不是为空
        if (cart == null) {
            //这个商品之前不再购物车，无法选择/不选中
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        } else {
            //这个商品已经在购物车里了，可以选中/不选中
            cartMapper.selectOrNot(userId, productId, selected);
        }
        return this.list(userId);
    }

    //全选和全不选购物车商品
    @Override
    public List<CartVO> selectAllOrNot(Integer userId, Integer selected) {
        //改变选中状态
        cartMapper.selectOrNot(userId, null, selected);
        return this.list(userId);
    }
}
