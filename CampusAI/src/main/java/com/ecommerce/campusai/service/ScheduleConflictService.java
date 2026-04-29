
package com.ecommerce.campusai.service;

import com.ecommerce.campusai.entity.RoomReservation;
import com.ecommerce.campusai.mapper.RoomReservationMapper;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class ScheduleConflictService {

    private final RoomReservationMapper reservationMapper;
    private final AiChatService aiChatService;

    public ScheduleConflictService(RoomReservationMapper reservationMapper,
                                   AiChatService aiChatService) {
        this.reservationMapper = reservationMapper;
        this.aiChatService = aiChatService;
    }

    /**
     * 检测自习室预约与课程表的冲突
     */
    public Map<String, Object> checkScheduleConflict(Integer userId, Integer seatId, 
                                                      LocalDateTime startTime, LocalDateTime endTime) {
        Map<String, Object> result = new HashMap<>();
        
        List<Map<String, Object>> courseSchedule = getUserCourseSchedule(userId);
        
        List<Map<String, Object>> conflicts = detectConflicts(courseSchedule, startTime, endTime);
        
        boolean hasConflict = !conflicts.isEmpty();
        
        String timeSlot = getTimeSlot(startTime.toLocalTime());
        
        if (hasConflict) {
            result.put("canReserve", false);
            result.put("conflicts", conflicts);
            result.put("message", "检测到课程冲突，无法预约该时段");
            result.put("suggestion", generateConflictSuggestion(conflicts, startTime, endTime));
        } else {
            result.put("canReserve", true);
            result.put("conflicts", new ArrayList<>());
            result.put("message", "未检测到课程冲突，可以预约");
            result.put("reservationInfo", Map.of(
                "seatId", seatId,
                "startTime", startTime,
                "endTime", endTime,
                "timeSlot", timeSlot
            ));
        }
        
        result.put("userId", userId);
        result.put("seatId", seatId);
        result.put("requestedStartTime", startTime);
        result.put("requestedEndTime", endTime);
        
        return result;
    }

    /**
     * 检测时间冲突
     */
    private List<Map<String, Object>> detectConflicts(List<Map<String, Object>> courses,
                                                       LocalDateTime startTime, 
                                                       LocalDateTime endTime) {
        List<Map<String, Object>> conflicts = new ArrayList<>();
        
        for (Map<String, Object> course : courses) {
            DayOfWeek courseDay = (DayOfWeek) course.get("dayOfWeek");
            LocalTime courseStart = (LocalTime) course.get("startTime");
            LocalTime courseEnd = (LocalTime) course.get("endTime");
            
            if (startTime.getDayOfWeek() == courseDay) {
                LocalTime requestStart = startTime.toLocalTime();
                LocalTime requestEnd = endTime.toLocalTime();
                
                if (requestStart.isBefore(courseEnd) && requestEnd.isAfter(courseStart)) {
                    Map<String, Object> conflict = new HashMap<>();
                    conflict.put("courseName", course.get("courseName"));
                    conflict.put("courseDay", courseDay.toString());
                    conflict.put("courseTime", courseStart + " - " + courseEnd);
                    conflict.put("classroom", course.get("classroom"));
                    conflict.put("conflictReason", "预约时间与课程时间重叠");
                    conflicts.add(conflict);
                }
            }
        }
        
        return conflicts;
    }

    /**
     * 获取用户课程表（模拟数据，实际应从教务系统获取）
     */
    private List<Map<String, Object>> getUserCourseSchedule(Integer userId) {
        List<Map<String, Object>> schedule = new ArrayList<>();
        
        Map<String, Object> course1 = new HashMap<>();
        course1.put("courseName", "高等数学");
        course1.put("dayOfWeek", DayOfWeek.MONDAY);
        course1.put("startTime", LocalTime.of(8, 0));
        course1.put("endTime", LocalTime.of(9, 40));
        course1.put("classroom", "A101");
        schedule.add(course1);
        
        Map<String, Object> course2 = new HashMap<>();
        course2.put("courseName", "大学英语");
        course2.put("dayOfWeek", DayOfWeek.MONDAY);
        course2.put("startTime", LocalTime.of(10, 0));
        course2.put("endTime", LocalTime.of(11, 40));
        course2.put("classroom", "B203");
        schedule.add(course2);
        
        Map<String, Object> course3 = new HashMap<>();
        course3.put("courseName", "计算机基础");
        course3.put("dayOfWeek", DayOfWeek.TUESDAY);
        course3.put("startTime", LocalTime.of(14, 0));
        course3.put("endTime", LocalTime.of(15, 40));
        course3.put("classroom", "C305");
        schedule.add(course3);
        
        Map<String, Object> course4 = new HashMap<>();
        course4.put("courseName", "数据结构");
        course4.put("dayOfWeek", DayOfWeek.WEDNESDAY);
        course4.put("startTime", LocalTime.of(8, 0));
        course4.put("endTime", LocalTime.of(9, 40));
        course4.put("classroom", "D401");
        schedule.add(course4);
        
        Map<String, Object> course5 = new HashMap<>();
        course5.put("courseName", "体育");
        course5.put("dayOfWeek", DayOfWeek.THURSDAY);
        course5.put("startTime", LocalTime.of(16, 0));
        course5.put("endTime", LocalTime.of(17, 40));
        course5.put("classroom", "体育馆");
        schedule.add(course5);
        
        return schedule;
    }

    /**
     * 获取时段标识
     */
    private String getTimeSlot(LocalTime time) {
        int hour = time.getHour();
        if (hour >= 8 && hour < 12) {
            return "morning";
        } else if (hour >= 12 && hour < 14) {
            return "noon";
        } else if (hour >= 14 && hour < 18) {
            return "afternoon";
        } else if (hour >= 18 && hour < 22) {
            return "night";
        } else {
            return "unknown";
        }
    }

    /**
     * 生成冲突建议
     */
    private String generateConflictSuggestion(List<Map<String, Object>> conflicts,
                                               LocalDateTime startTime, 
                                               LocalDateTime endTime) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("作为CampusAI智慧校园助手，用户想预约自习室但检测到课程冲突。\n\n");
        prompt.append("预约时间：").append(startTime).append(" 至 ").append(endTime).append("\n\n");
        prompt.append("冲突课程：\n");
        for (Map<String, Object> conflict : conflicts) {
            prompt.append("- ").append(conflict.get("courseName"))
                  .append("（").append(conflict.get("courseTime")).append("，")
                  .append(conflict.get("classroom")).append("）\n");
        }
        
        prompt.append("\n请给出友好的建议，包含：\n");
        prompt.append("1. 说明冲突情况\n");
        prompt.append("2. 建议其他可预约时段\n");
        prompt.append("3. 温馨提示\n");
        prompt.append("控制在100字以内，语气亲切。");
        
        return aiChatService.chat(prompt.toString());
    }

    /**
     * 智能推荐可用时段
     */
    public List<Map<String, Object>> recommendAvailableSlots(Integer userId, DayOfWeek dayOfWeek) {
        List<Map<String, Object>> courseSchedule = getUserCourseSchedule(userId);
        
        List<Map<String, Object>> availableSlots = new ArrayList<>();
        
        List<LocalTime[]> predefinedSlots = Arrays.asList(
            new LocalTime[]{LocalTime.of(8, 0), LocalTime.of(10, 0)},
            new LocalTime[]{LocalTime.of(10, 0), LocalTime.of(12, 0)},
            new LocalTime[]{LocalTime.of(14, 0), LocalTime.of(16, 0)},
            new LocalTime[]{LocalTime.of(16, 0), LocalTime.of(18, 0)},
            new LocalTime[]{LocalTime.of(19, 0), LocalTime.of(21, 0)}
        );
        
        for (LocalTime[] slot : predefinedSlots) {
            boolean hasConflict = false;
            for (Map<String, Object> course : courseSchedule) {
                if (course.get("dayOfWeek") == dayOfWeek) {
                    LocalTime courseStart = (LocalTime) course.get("startTime");
                    LocalTime courseEnd = (LocalTime) course.get("endTime");
                    
                    if (slot[0].isBefore(courseEnd) && slot[1].isAfter(courseStart)) {
                        hasConflict = true;
                        break;
                    }
                }
            }
            
            if (!hasConflict) {
                Map<String, Object> availableSlot = new HashMap<>();
                availableSlot.put("startTime", slot[0]);
                availableSlot.put("endTime", slot[1]);
                availableSlot.put("timeSlot", getTimeSlot(slot[0]));
                availableSlot.put("available", true);
                availableSlots.add(availableSlot);
            }
        }
        
        return availableSlots;
    }
}
