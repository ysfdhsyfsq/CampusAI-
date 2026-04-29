package com.ecommerce.campusai.controller;
import com.ecommerce.campusai.entity.RoomReservation;
import com.ecommerce.campusai.service.RoomReservationService;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reserve")
public class RoomReservationController {
    private final RoomReservationService service;

    public RoomReservationController(RoomReservationService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public String add(@RequestBody RoomReservation reservation) {
        return service.reserve(reservation);
    }

    @GetMapping("/list/{userId}")
    public List<RoomReservation> list(@PathVariable Integer userId) {
        return service.listByUser(userId);
    }

    @GetMapping("/calendar/events")
    public List<HashMap<String, Object>> events() {
        return service.listAll().stream().map(r -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", r.getId());
            map.put("title", r.getSeatCode() + " " + r.getUserName());
            map.put("start", r.getStartTime());
            map.put("end", r.getEndTime());
            map.put("color", r.getStatus() == 1 ? "#667eea" : "#f5576c");
            return map;
        }).collect(Collectors.toList());
    }

    @PutMapping("/cancel/{reservationId}")
    public String cancel(@PathVariable Integer reservationId) {
        return service.cancelReservation(reservationId);
    }

    @GetMapping("/check-conflict")
    public HashMap<String, Object> checkConflict(
            @RequestParam Integer seatId,
            @RequestParam String timeSlot,
            @RequestParam Integer userId) {
        return service.checkReservationConflict(seatId, timeSlot, userId);
    }
}