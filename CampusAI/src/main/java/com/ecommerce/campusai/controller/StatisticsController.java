package com.ecommerce.campusai.controller;

import com.ecommerce.campusai.dto.StatisticsVO;
import com.ecommerce.campusai.mapper.ActivityMapper;
import com.ecommerce.campusai.mapper.OrdersMapper;
import com.ecommerce.campusai.mapper.RoomReservationMapper;
import com.ecommerce.campusai.mapper.UserMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    private final UserMapper userMapper;
    private final OrdersMapper ordersMapper;
    private final RoomReservationMapper roomReservationMapper;
    private final ActivityMapper activityMapper;

    public StatisticsController(UserMapper userMapper, OrdersMapper ordersMapper, 
                               RoomReservationMapper roomReservationMapper, ActivityMapper activityMapper) {
        this.userMapper = userMapper;
        this.ordersMapper = ordersMapper;
        this.roomReservationMapper = roomReservationMapper;
        this.activityMapper = activityMapper;
    }

    @GetMapping("/dashboard")
    public StatisticsVO getDashboardData() {
        StatisticsVO vo = new StatisticsVO();
        
        vo.setUserTrend(userMapper.getUserRegistrationTrend());
        vo.setOrderDistribution(ordersMapper.getOrderDistribution());
        vo.setRoomUsage(roomReservationMapper.getRoomUsageByTimeSlot());
        vo.setActivityRanking(activityMapper.getActivityRanking());
        vo.setRealTimeStats(buildRealTimeStats());
        
        return vo;
    }

    @GetMapping("/user-trend")
    public List<Map<String, Object>> getUserTrend() {
        return userMapper.getUserRegistrationTrend();
    }

    @GetMapping("/order-distribution")
    public List<Map<String, Object>> getOrderDistribution() {
        return ordersMapper.getOrderDistribution();
    }

    @GetMapping("/room-usage")
    public List<Map<String, Object>> getRoomUsage() {
        return roomReservationMapper.getRoomUsageByTimeSlot();
    }

    @GetMapping("/activity-ranking")
    public List<Map<String, Object>> getActivityRanking() {
        return activityMapper.getActivityRanking();
    }

    @GetMapping("/realtime")
    public Map<String, Object> getRealTimeStats() {
        return buildRealTimeStats();
    }

    private Map<String, Object> buildRealTimeStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userMapper.getTotalUserCount());
        stats.put("totalOrders", ordersMapper.getOrderStats());
        stats.put("totalReservations", roomReservationMapper.getTotalReservations());
        stats.put("totalActivities", activityMapper.list().size());
        return stats;
    }
}
