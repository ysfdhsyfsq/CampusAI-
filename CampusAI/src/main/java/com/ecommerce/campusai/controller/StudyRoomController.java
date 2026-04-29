package com.ecommerce.campusai.controller;

import com.ecommerce.campusai.entity.StudyRoom;
import com.ecommerce.campusai.entity.StudyRoomSeat;
import com.ecommerce.campusai.service.StudyRoomSeatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room")
public class StudyRoomController {
    
    private final StudyRoomSeatService seatService;

    public StudyRoomController(StudyRoomSeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping("/list")
    public List<StudyRoom> list() {
        return seatService.listRooms();
    }

    @GetMapping("/{roomId}/seats")
    public List<StudyRoomSeat> getSeatsByRoom(@PathVariable Integer roomId) {
        return seatService.listSeatsByRoom(roomId);
    }
}
