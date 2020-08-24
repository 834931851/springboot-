package com.hyb.mall.query;

import java.util.List;

/**
 * 描述：查询前台商品列表的query
 */
public class ProductListQuery {
    //1.关键字
    private String keyword;

    //2.商品列表
    private List<Integer> categoryIds;

    public String getKeyWord() {
        return keyword;
    }

    public void setKeyWord(String keyWord) {
        this.keyword = keyWord;
    }

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }
}
