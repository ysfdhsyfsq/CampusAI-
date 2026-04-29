package com.ecommerce.campusai.entity;
import lombok.Data;

@Data
public class StudyRoomSeat {
    private Integer id;
    private Integer roomId;
    private String seatCode;
    private Integer status;
}