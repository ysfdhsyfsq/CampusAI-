package com.ecommerce.campusai.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Activity {
    private Integer id;
    private String title;
    private String content;
    private String type;
    private Integer maxNum;
    private Integer enrollNum;
    private LocalDateTime endTime;
    private Integer status;
}