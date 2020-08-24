package com.hyb.mall.controller;

import com.github.pagehelper.PageInfo;
import com.hyb.mall.common.ApiRestResponse;
import com.hyb.mall.common.Constant;
import com.hyb.mall.exception.MallException;
import com.hyb.mall.exception.MallExceptionEnum;
import com.hyb.mall.model.pojo.Category;
import com.hyb.mall.model.pojo.User;
import com.hyb.mall.model.request.AddCategoryReq;
import com.hyb.mall.model.request.UpdateCategoryReq;
import com.hyb.mall.model.vo.CategoryVO;
import com.hyb.mall.service.CategoryService;
import com.hyb.mall.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
 * 描述：目录controller
 */
@Controller
public class CategoryController {
    @Autowired
    UserService userService;
    @Autowired
    CategoryService categoryService;

    /**
     * 后台添加目录
     *
     * @param session
     * @param addCategoryReq
     * @return
     */
    @ApiOperation("后台添加商品分类目录")
    @PostMapping("/admin/category/add")
    @ResponseBody
    public ApiRestResponse addCategory(HttpSession session, @Valid @RequestBody AddCategoryReq addCategoryReq) {
        //1.获取session中的用户
        User currentUser = (User) session.getAttribute(Constant.HYB_MALL_USER);
        //2.判断当前用户是否为空，为空则需要登录
        if (currentUser == null) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_LOGIN);
        }
        //3.登录校验是否为管理员
        boolean adminRole = userService.checkAdminRole(currentUser);
        if (adminRole) {
            //是管理员，则执行添加商品类别操作
            categoryService.add(addCategoryReq);
            return ApiRestResponse.success();
        } else {
            return ApiRestResponse.error(MallExceptionEnum.NEED_ADMIN);
        }
    }

    @ApiOperation("后台更新商品分类目录")
    @PostMapping("/admin/category/update")
    @ResponseBody
    public ApiRestResponse updateCategory(@Valid @RequestBody UpdateCategoryReq updateCategoryReq, HttpSession session) {
        //1.获取session中的用户
        User currentUser = (User) session.getAttribute(Constant.HYB_MALL_USER);
        //2.判断当前用户是否为空，为空则需要登录
        if (currentUser == null) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_LOGIN);
        }
        //3.登录校验是否为管理员
        boolean adminRole = userService.checkAdminRole(currentUser);
        if (adminRole) {
            //是管理员，则执行添加商品类别操作
            Category category = new Category();
            BeanUtils.copyProperties(updateCategoryReq, category);
            categoryService.update(category);
            return ApiRestResponse.success();
        } else {
            return ApiRestResponse.error(MallExceptionEnum.NEED_ADMIN);
        }
    }

    @ApiOperation("后台删除商品分类目录")
    @PostMapping("/admin/category/delete")
    @ResponseBody
    public ApiRestResponse deleteCategory(@RequestParam Integer id) {
        categoryService.delete(id);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台商品分类列表")
    @GetMapping("/admin/category/list")
    @ResponseBody
    public ApiRestResponse listCategoryForAdmin(@RequestParam Integer pageNum,@RequestParam Integer pageSize){
        PageInfo pageInfo = categoryService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    @ApiOperation("前台商品分类列表")
    @GetMapping("/category/list")
    @ResponseBody
    public ApiRestResponse listCategoryForCustomer(){
        //parentId设置成0，就能查询所有的数据
        List<CategoryVO> categoryVOS = categoryService.listCategoryForCustomer(0);
        return ApiRestResponse.success(categoryVOS);
    }
}
