package com.hyb.mall.model.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 描述：updateCategoryReq更新修改目录请求类
 * 不用pojo中Category，是因为保持每个类都有自己的职责，也是保证程序的安全性
 * 因为更新的名字类型等可以不进行修改，所以不要求不为空，但是，id是更新的依据，要求不为空
 */
public class UpdateCategoryReq {

    @NotNull(message = "分类目录id不能为空")
    private Integer id;

    @Size(min = 2, max = 5)
    private String name;

    @Max(3)
    private Integer type;

    private Integer parentId;

    private Integer orderNum;

    public UpdateCategoryReq() {
    }

    public UpdateCategoryReq(String name, Integer type, Integer parentId, Integer orderNum) {
        this.name = name;
        this.type = type;
        this.parentId = parentId;
        this.orderNum = orderNum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public String toString() {
        return "UpdateCategoryReq{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", parentId=" + parentId +
                ", orderNum=" + orderNum +
                '}';
    }
}
