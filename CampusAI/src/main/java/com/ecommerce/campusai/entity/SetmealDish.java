package com.ecommerce.campusai.entity;

import java.io.Serializable;

public class SetmealDish implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private Integer setmealId;
    private Integer dishId;
    private Integer quantity;
    
    private String dishName;
    private String dishImage;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getSetmealId() { return setmealId; }
    public void setSetmealId(Integer setmealId) { this.setmealId = setmealId; }
    public Integer getDishId() { return dishId; }
    public void setDishId(Integer dishId) { this.dishId = dishId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getDishName() { return dishName; }
    public void setDishName(String dishName) { this.dishName = dishName; }
    public String getDishImage() { return dishImage; }
    public void setDishImage(String dishImage) { this.dishImage = dishImage; }
}