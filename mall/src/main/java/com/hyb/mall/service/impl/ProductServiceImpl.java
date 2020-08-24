package com.hyb.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hyb.mall.common.ApiRestResponse;
import com.hyb.mall.common.Constant;
import com.hyb.mall.exception.MallException;
import com.hyb.mall.exception.MallExceptionEnum;
import com.hyb.mall.model.dao.ProductMapper;
import com.hyb.mall.model.pojo.Product;
import com.hyb.mall.model.request.AddProductReq;
import com.hyb.mall.model.request.ProductListReq;
import com.hyb.mall.model.vo.CategoryVO;
import com.hyb.mall.query.ProductListQuery;
import com.hyb.mall.service.CategoryService;
import com.hyb.mall.service.ProductService;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductMapper productMapper;

    @Autowired
    CategoryService categoryService;

    @Override
    public void add(AddProductReq addProductReq) {
        //1.创建product实例
        Product product = new Product();
        //2.将新增的product复制到pojo中product类中，对数据进行一次覆盖
        BeanUtils.copyProperties(addProductReq, product);
        //3.通过查询数据库是否有重名的商品，有就添加失败
        Product productOld = productMapper.selectByName(addProductReq.getName());
        if (productOld != null) {
            throw new MallException(MallExceptionEnum.NAME_EXISTED);
        }
        //4.进行插入数据操作并判断是否有效插入
        int count = productMapper.insertSelective(product);
        if (count == 0) {
            throw new MallException(MallExceptionEnum.CREATE_FAILE);
        }
    }

    @Override
    public void update(Product updateProduct) {
        //1.查询是否有相同的商品名
        Product productOld = productMapper.selectByName(updateProduct.getName());
        //2.同名且不同id不能继续修改
        if (productOld != null && productOld.getId().equals(updateProduct.getId())) {
            throw new MallException(MallExceptionEnum.NAME_EXISTED);
        }
        //3.通过校验
        int count = productMapper.updateByPrimaryKeySelective(updateProduct);
        if (count == 0) {
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public void delete(Integer id) {
        //1.查询商品的id主键是否存在
        Product productOld = productMapper.selectByPrimaryKey(id);
        if (productOld == null) {
            throw new MallException(MallExceptionEnum.DELETE_FAILE);
        }
        //2.存在商品的id则进行删除操作
        int count = productMapper.deleteByPrimaryKey(id);
        if (count == 0) {
            throw new MallException(MallExceptionEnum.DELETE_FAILE);
        }
    }

    @Override
    public void batchUpdateSellStatus(Integer[] ids, Integer sellStatus) {
        productMapper.batchUpdateSellStatus(ids, sellStatus);
    }

    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> products = productMapper.selectListForAdmin();
        PageInfo pageInfo = new PageInfo(products);
        return pageInfo;
    }

    @Override
    public Product detail(Integer id) {
        Product product = productMapper.selectByPrimaryKey(id);
        return product;
    }

    @Override
    public PageInfo list(ProductListReq productListReq) {
        //构建query对象
        ProductListQuery productListQuery = new ProductListQuery();
        //搜索处理
        if (!StringUtils.isEmpty(productListReq.getKeyword())) {
            //合成字符串,就能利用数据库的模糊查找功能
            String keyword = new StringBuilder()
                    .append("%")
                    .append(productListReq.getKeyword())
                    .append("%")
                    .toString();
            productListQuery.setKeyWord(keyword);
        }
        //3.目录处理，如果查某个目录下的商品，不仅需要查出该目录下的，还需要把所有子目录的所有商品查出来，所以要拿到一个目录id的List
        if (productListReq.getCategoryId() != null) {
            //这里需要对listCategoryForCustomer进行重构，
            List<CategoryVO> categoryVOSList = categoryService.listCategoryForCustomer(productListReq.getCategoryId());
            //得到的categoryVOSList是一个树状结构，需要进行平铺展开，将子节点的ID都拿过来
            ArrayList<Integer> categoryIds = new ArrayList<>();
            categoryIds.add(productListReq.getCategoryId());
            getCategoryIds(categoryVOSList, categoryIds);
            productListQuery.setCategoryIds(categoryIds);
        }
        //排序处理
        //从前端请求拿到orderby
        String orderBy = productListReq.getOrderBy();
        if (Constant.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
            PageHelper.startPage(productListReq.getPageNum(), productListReq.getPageSize(), orderBy);
        } else {
            PageHelper.startPage(productListReq.getPageNum(), productListReq.getPageSize());
        }
        List<Product> productList = productMapper.selectList(productListQuery);
        PageInfo pageInfo = new PageInfo(productList);
        return pageInfo;
    }

    private void getCategoryIds(List<CategoryVO> categoryVOList, ArrayList<Integer> categoryIds) {
        for (int i = 0; i < categoryVOList.size(); i++) {
            CategoryVO categoryVO = categoryVOList.get(i);
            if (categoryVO != null) {
                categoryIds.add(categoryVO.getId());
                getCategoryIds(categoryVO.getChildCategory(), categoryIds);
            }
        }
    }

}
