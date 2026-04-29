package com.ecommerce.campusai.service;
import com.ecommerce.campusai.entity.RoomReservation;
import com.ecommerce.campusai.entity.StudyRoomSeat;
import com.ecommerce.campusai.entity.User;
import com.ecommerce.campusai.mapper.RoomReservationMapper;
import com.ecommerce.campusai.mapper.StudyRoomSeatMapper;
import com.ecommerce.campusai.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
public class RoomReservationService {
    private final RoomReservationMapper mapper;
    private final ScheduleConflictService conflictService;
    private final StudyRoomSeatMapper seatMapper;
    private final UserMapper userMapper;

    public RoomReservationService(RoomReservationMapper mapper, ScheduleConflictService conflictService, StudyRoomSeatMapper seatMapper, UserMapper userMapper) {
        this.mapper = mapper;
        this.conflictService = conflictService;
        this.seatMapper = seatMapper;
        this.userMapper = userMapper;
    }

    @Transactional
    public String reserve(RoomReservation reservation) {
        if (mapper.countConflict(reservation.getSeatId(), reservation.getTimeSlot()) > 0) {
            return "该座位此时段已被预约";
        }

        int userTodayCount = mapper.countUserTodayReservations(reservation.getUserId());
        if (userTodayCount >= 2) {
            return "您今天已达到最大预约次数（2次）";
        }

        StudyRoomSeat seat = seatMapper.list().stream()
                .filter(s -> s.getId().equals(reservation.getSeatId()))
                .findFirst().orElse(null);
        if (seat != null) {
            reservation.setSeatCode(seat.getSeatCode());
        }

        User user = userMapper.findById(reservation.getUserId());
        if (user != null) {
            reservation.setUserName(user.getUsername());
        }

        LocalDateTime now = LocalDateTime.now();
        reservation.setStartTime(now);
        switch (reservation.getTimeSlot()) {
            case "morning": reservation.setEndTime(now.plusHours(4)); break;
            case "afternoon": reservation.setEndTime(now.plusHours(4)); break;
            case "night": reservation.setEndTime(now.plusHours(3)); break;
            default: return "时段错误";
        }

        reservation.setStatus(1);
        reservation.setCreateTime(now);
        mapper.insert(reservation);
        return "预约成功";
    }

    @Transactional
    public String cancelReservation(Integer reservationId) {
        RoomReservation reservation = mapper.findById(reservationId);
        
        if (reservation == null) {
            return "预约记录不存在";
        }

        if (reservation.getStatus() == 0) {
            return "该预约已取消";
        }
        
        if (reservation.getStatus() == 2) {
            return "该预约已结束，无法取消";
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = reservation.getStartTime();
        
        // 计算距离预约开始还有多少小时
        long hoursUntilStart = java.time.Duration.between(now, startTime).toHours();
        
        // 允许在预约开始前 30 分钟内取消
        long minutesUntilStart = java.time.Duration.between(now, startTime).toMinutes();
        
        if (minutesUntilStart < 30) {
            return "预约即将开始（30分钟内）或已开始，无法取消";
        }

        mapper.updateStatus(reservationId, 0);
        return "取消成功";
    }

    public HashMap<String, Object> checkReservationConflict(Integer seatId, String timeSlot, Integer userId) {
        RoomReservation tempReservation = new RoomReservation();
        tempReservation.setSeatId(seatId);
        tempReservation.setTimeSlot(timeSlot);
        
        LocalDateTime now = LocalDateTime.now();
        switch (timeSlot) {
            case "morning":
                tempReservation.setStartTime(now.withHour(8).withMinute(0));
                tempReservation.setEndTime(now.withHour(12).withMinute(0));
                break;
            case "afternoon":
                tempReservation.setStartTime(now.withHour(14).withMinute(0));
                tempReservation.setEndTime(now.withHour(18).withMinute(0));
                break;
            case "night":
                tempReservation.setStartTime(now.withHour(19).withMinute(0));
                tempReservation.setEndTime(now.withHour(22).withMinute(0));
                break;
        }

        return (HashMap<String, Object>) conflictService.checkScheduleConflict(
            userId, 
            seatId, 
            tempReservation.getStartTime(), 
            tempReservation.getEndTime()
        );
    }

    public List<RoomReservation> listByUser(Integer userId) {
        List<RoomReservation> reservations = mapper.listByUserId(userId);
        
        // 动态更新已结束预约的状态
        LocalDateTime now = LocalDateTime.now();
        for (RoomReservation reservation : reservations) {
            if (reservation.getStatus() == 1 && reservation.getEndTime() != null) {
                if (now.isAfter(reservation.getEndTime())) {
                    // 预约已结束，更新状态为 2（已结束）
                    mapper.updateStatus(reservation.getId(), 2);
                    reservation.setStatus(2);
                }
            }
        }
        
        return reservations;
    }

    public List<RoomReservation> listAll() {
        return mapper.listAll();
    }
}