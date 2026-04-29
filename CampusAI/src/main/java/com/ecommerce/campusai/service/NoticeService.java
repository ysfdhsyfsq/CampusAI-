package com.ecommerce.campusai.service;

import com.ecommerce.campusai.entity.Notice;
import com.ecommerce.campusai.mapper.NoticeMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    // 查询所有公告
    public List<Notice> findAll() {
        return noticeMapper.findAll();
    }

    // 添加公告
    public int addNotice(Notice notice) {
        return noticeMapper.addNotice(notice);
    }
}