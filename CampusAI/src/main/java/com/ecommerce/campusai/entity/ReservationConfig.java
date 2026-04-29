package com.ecommerce.campusai.entity;

import lombok.Data;

@Data
public class ReservationConfig {
    private Integer id;
    private String configKey;
    private String configValue;
    private String description;
}
