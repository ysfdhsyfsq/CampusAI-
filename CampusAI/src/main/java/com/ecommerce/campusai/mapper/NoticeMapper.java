package com.ecommerce.campusai.mapper;

import com.ecommerce.campusai.entity.Notice;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface NoticeMapper {

    // 查询所有公告
    @Select("SELECT * FROM notice ORDER BY id DESC")
    List<Notice> findAll();

    // 发布公告
    @Insert("INSERT INTO notice(title,content,createTime) VALUES(#{title},#{content},#{createTime})")
    int addNotice(Notice notice);
}