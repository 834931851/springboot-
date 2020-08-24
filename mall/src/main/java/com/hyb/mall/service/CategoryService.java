package com.hyb.mall.service;

import com.github.pagehelper.PageInfo;
import com.hyb.mall.model.pojo.Category;
import com.hyb.mall.model.request.AddCategoryReq;
import com.hyb.mall.model.vo.CategoryVO;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * 描述：分类目录Service
 */
public interface CategoryService {

    void add(AddCategoryReq addCategoryReq);

    void update(Category updateCategoryReq);

    void delete(Integer id);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    List<CategoryVO> listCategoryForCustomer(Integer parentId);
}
