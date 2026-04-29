package com.ecommerce.campusai.entity;

import java.io.Serializable;
import java.util.Date;

public class DishCategory implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private String name;
    private Integer sort;
    private Date createTime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getSort() { return sort; }
    public void setSort(Integer sort) { this.sort = sort; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}