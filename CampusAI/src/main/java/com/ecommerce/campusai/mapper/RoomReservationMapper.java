package com.ecommerce.campusai.mapper;

import com.ecommerce.campusai.entity.RoomReservation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface RoomReservationMapper {

    @Insert("insert into room_reservation(room_id, seat_id, seat_code, user_id, user_name, time_slot, start_time, end_time, status, create_time) " +
            "values(#{roomId}, #{seatId}, #{seatCode}, #{userId}, #{userName}, #{timeSlot}, #{startTime}, #{endTime}, #{status}, #{createTime})")
    void insert(RoomReservation reservation);

    @Select("select count(*) from room_reservation where seat_id=#{seatId} and time_slot=#{timeSlot} and status=1")
    int countConflict(@Param("seatId") Integer seatId, @Param("timeSlot") String timeSlot);

    @Select("select count(*) from room_reservation where user_id=#{userId} and DATE(create_time)=CURDATE() and status=1")
    int countUserTodayReservations(@Param("userId") Integer userId);

    @Select("select * from room_reservation where id=#{id}")
    RoomReservation findById(@Param("id") Integer id);

    @Update("update room_reservation set status=0 where id=#{id}")
    void updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    @Select("select * from room_reservation where user_id=#{userId}")
    List<RoomReservation> listByUserId(@Param("userId") Integer userId);

    @Select("select * from room_reservation")
    List<RoomReservation> listAll();

    @Select("SELECT time_slot as name, COUNT(*) as value FROM room_reservation WHERE status=1 GROUP BY time_slot ORDER BY FIELD(time_slot, '08:00-10:00', '10:00-12:00', '14:00-16:00', '16:00-18:00', '19:00-21:00')")
    List<Map<String, Object>> getRoomUsageByTimeSlot();

    @Select("SELECT COUNT(*) as total FROM room_reservation WHERE status=1")
    int getTotalReservations();
}