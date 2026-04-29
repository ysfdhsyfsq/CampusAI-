package com.ecommerce.campusai.mapper;

import com.ecommerce.campusai.entity.StudyRoom;
import com.ecommerce.campusai.entity.StudyRoomSeat;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StudyRoomSeatMapper {

    @Select("select * from study_room_seat")
    List<StudyRoomSeat> list();

    @Select("select * from study_room_seat where room_id=#{roomId}")
    List<StudyRoomSeat> listByRoomId(@Param("roomId") Integer roomId);

    @Select("select * from study_room")
    List<StudyRoom> listRooms();

    @Insert("insert into study_room_seat(room_id,seat_code,status) values(#{roomId},#{seatCode},0)")
    void insert(StudyRoomSeat seat);
}