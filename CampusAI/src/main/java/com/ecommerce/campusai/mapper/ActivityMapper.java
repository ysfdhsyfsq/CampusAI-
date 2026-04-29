package com.ecommerce.campusai.mapper;

import com.ecommerce.campusai.dto.ActivityReportDTO;
import com.ecommerce.campusai.entity.Activity;
import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Map;

@Mapper
public interface ActivityMapper {

    @Select("select * from activity order by id desc")
    List<Activity> list();

    @Select("select * from activity where id=#{id}")
    Activity getById(Integer id);

    @Update("update activity set status=0 where end_time < now() and status=1")
    void updateStatus();

    @Update("update activity set enroll_num=enroll_num+1 where id=#{id}")
    void incrEnroll(Integer id);

    @Insert("insert into activity(title,content,type,max_num,end_time) values(#{title},#{content},#{type},#{maxNum},#{endTime})")
    int add(Activity activity);

    @Delete("delete from activity")
    void clear();

    @Select("SELECT title as name, enroll_num as value FROM activity ORDER BY enroll_num DESC LIMIT 10")
    List<Map<String, Object>> getActivityRanking();

    @Select("SELECT " +
            "  title as activityTitle, " +
            "  max_num as maxNum, " +
            "  enroll_num as enrollNum, " +
            "  CAST(CASE WHEN max_num > 0 THEN ROUND(enroll_num * 100.0 / max_num, 2) ELSE 0 END AS DECIMAL(10,2)) as enrollRate, " +
            "  DATE_FORMAT(end_time, '%Y-%m-%d %H:%i') as endTime, " +
            "  CASE WHEN status = 1 THEN '报名中' ELSE '已截止' END as status " +
            "FROM activity " +
            "ORDER BY enroll_num DESC")
    List<ActivityReportDTO> getActivityReportDTO();

    @Select("SELECT " +
            "  title as activityTitle, " +
            "  max_num as maxNum, " +
            "  enroll_num as enrollNum, " +
            "  CASE WHEN max_num > 0 THEN ROUND(enroll_num * 100.0 / max_num, 2) ELSE 0 END as enrollRate, " +
            "  DATE_FORMAT(end_time, '%Y-%m-%d %H:%i') as endTime, " +
            "  CASE WHEN status = 1 THEN '报名中' ELSE '已截止' END as status " +
            "FROM activity " +
            "ORDER BY enroll_num DESC")
    List<Map<String, Object>> getActivityReport();
}