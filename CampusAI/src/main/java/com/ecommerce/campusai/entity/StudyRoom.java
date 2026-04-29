package com.ecommerce.campusai.entity;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StudyRoom {
    private Integer id;
    private String roomName;
    private Integer totalSeat;
    private Integer status;
    private LocalDateTime createTime;
}