package com.ecommerce.campusai.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Setmeal implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private String name;
    private BigDecimal price;
    private Integer status;
    private String description;
    private String image;
    private Date createTime;
    private Date updateTime;
    
    private List<SetmealDish> setmealDishes;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public List<SetmealDish> getSetmealDishes() { return setmealDishes; }
    public void setSetmealDishes(List<SetmealDish> setmealDishes) { this.setmealDishes = setmealDishes; }
}