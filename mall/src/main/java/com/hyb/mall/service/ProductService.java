package com.hyb.mall.service;

import com.github.pagehelper.PageInfo;
import com.hyb.mall.common.ApiRestResponse;
import com.hyb.mall.model.pojo.Category;
import com.hyb.mall.model.pojo.Product;
import com.hyb.mall.model.request.AddCategoryReq;
import com.hyb.mall.model.request.AddProductReq;
import com.hyb.mall.model.request.ProductListReq;
import com.hyb.mall.model.vo.CategoryVO;

import java.util.List;

/**
 * 描述：商品Service
 */
public interface ProductService {

    void add(AddProductReq addProductReq);

    void update(Product updateProduct);

    void delete(Integer id);

    void batchUpdateSellStatus(Integer[] ids, Integer sellStatus);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    Product detail(Integer id);

    PageInfo list(ProductListReq productListReq);
}
