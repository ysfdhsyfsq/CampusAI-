package com.ecommerce.campusai.entity;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RoomReservation {
    private Integer id;
    private Integer roomId;
    private Integer seatId;
    private String seatCode;
    private Integer userId;
    private String userName;
    private String timeSlot;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;
    private LocalDateTime createTime;
}