package com.ecommerce.campusai.controller;

import com.ecommerce.campusai.entity.Notice;
import com.ecommerce.campusai.service.NoticeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    // 查询所有公告
    @GetMapping("/findAll")
    public List<Notice> findAll() {
        return noticeService.findAll();
    }

    // 发布公告
    @PostMapping("/add")
    public String addNotice(@RequestBody Notice notice) {
        // 自动设置当前时间
        notice.setCreateTime(String.valueOf(System.currentTimeMillis()));

        int result = noticeService.addNotice(notice);
        if (result > 0) {
            return "公告发布成功！";
        } else {
            return "发布失败！";
        }
    }
}