package com.hyb.mall.service.impl;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hyb.mall.exception.MallException;
import com.hyb.mall.exception.MallExceptionEnum;
import com.hyb.mall.model.dao.CategoryMapper;
import com.hyb.mall.model.pojo.Category;
import com.hyb.mall.model.request.AddCategoryReq;
import com.hyb.mall.model.request.UpdateCategoryReq;
import com.hyb.mall.model.vo.CategoryVO;
import com.hyb.mall.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：目录分类实现类
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    //写上override,让他直接实现categoryService接口的方法
    @Override
    public void add(AddCategoryReq addCategoryReq) {
        Category category = new Category();
        //小技巧：将拷贝的源，拷贝到目标类中
        BeanUtils.copyProperties(addCategoryReq, category);
        //获取到商品在数据库的名字，以便判断商品的重名
        Category categoryOld = categoryMapper.selectByName(addCategoryReq.getName());
        //如果数据库有重名，则抛出异常不允许创建
        if (categoryOld != null) {
            throw new MallException(MallExceptionEnum.NAME_EXISTED);
        }
        int count = categoryMapper.insertSelective(category);
        //判断是否插入成功
        if (count == 0) {
            throw new MallException(MallExceptionEnum.CREATE_FAILE);
        }
    }

    @Override
    public void update(Category updateCategoryReq) {
        //1.查找有没有重复的商品分类名字
        if (updateCategoryReq.getName() != null) {
            //获取需要更新商品的名字
            Category categoryOld = categoryMapper.selectByName(updateCategoryReq.getName());
            if (categoryOld != null && !categoryOld.getId().equals(updateCategoryReq.getId())) {
                throw new MallException(MallExceptionEnum.NAME_EXISTED);
            }
        }
        int count = categoryMapper.updateByPrimaryKeySelective(updateCategoryReq);
        if (count == 0) {
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public void delete(Integer id) {
        //1.先进行查找，判断是否能在数据库中找到要删除的id
        Category categoryOld = categoryMapper.selectByPrimaryKey(id);
        //2.查找不到id记录，无法删除，删除失败
        if (categoryOld == null) {
            throw new MallException(MallExceptionEnum.DELETE_FAILE);
        }
        //3.进行删除操作
        int count = categoryMapper.deleteByPrimaryKey(id);
        //4.记录为0，则删除失败
        if (count == 0) {
            throw new MallException(MallExceptionEnum.DELETE_FAILE);
        }
    }

    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize) {
        //type是第一优先级，order_num是第二优先级
        PageHelper.startPage(pageNum, pageSize, "type,order_num");
        //做查询工作
        List<Category> categoryList = categoryMapper.selectList();
        //返回前端的类型
        PageInfo pageInfo = new PageInfo(categoryList);
        return pageInfo;
    }

    @Override
    @Cacheable(value = "listCategoryForCustomer") //是spring所提供的
    public List<CategoryVO> listCategoryForCustomer(Integer parentId){
        ArrayList<CategoryVO> categoryVOList = new ArrayList<>();
        recursivelyFindCategories(categoryVOList,parentId);
        return categoryVOList;
    }
    private void recursivelyFindCategories(List<CategoryVO> categoryVOList,Integer parentId){
        //递归获取所有子类别，并组合成为一个目录树
        List<Category> categoryList = categoryMapper.selectCategoriesByParentId(parentId);
        if (!CollectionUtils.isEmpty(categoryList)) {
            for (int i = 0; i < categoryList.size(); i++) {
                Category category = categoryList.get(i);
                CategoryVO categoryVO = new CategoryVO();
                BeanUtils.copyProperties(category,categoryVO);
                categoryVOList.add(categoryVO);
                recursivelyFindCategories(categoryVO.getChildCategory(),categoryVO.getId());
            }
        }
    }
}
