package com.ecommerce.campusai.controller;

import com.ecommerce.campusai.entity.Activity;
import com.ecommerce.campusai.mapper.ActivityMapper;
import com.ecommerce.campusai.util.StaticizeUtil;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/activity")
public class ActivityController {

    private final ActivityMapper activityMapper;
    private final StaticizeUtil staticizeUtil;

    public ActivityController(ActivityMapper activityMapper, StaticizeUtil staticizeUtil) {
        this.activityMapper = activityMapper;
        this.staticizeUtil = staticizeUtil;
    }

    @GetMapping("/list")
    public List<Activity> list() {
        return activityMapper.list();
    }

    @GetMapping("/{id}")
    public Activity detail(@PathVariable Integer id) {
        return activityMapper.getById(id);
    }

    @PostMapping("/add")
    public String add(@RequestBody Activity activity) throws Exception {
        activityMapper.add(activity);
        generateStaticPages();
        return "发布成功，已生成静态页面";
    }

    @PostMapping("/enroll/{id}")
    public String enroll(@PathVariable Integer id) throws Exception {
        Activity act = activityMapper.getById(id);
        if (act == null) {
            return "活动不存在";
        }
        if (act.getStatus() == 0) {
            return "报名已截止";
        }
        if (act.getEnrollNum() >= act.getMaxNum()) {
            return "报名已满员";
        }
        activityMapper.incrEnroll(id);
        generateStaticPages();
        return "报名成功";
    }

    @GetMapping("/clear")
    public String clear() {
        activityMapper.clear();
        return "已清空错误活动数据";
    }

    @GetMapping("/regenerate")
    public String regenerate() throws Exception {
        generateStaticPages();
        return "静态页面重新生成成功";
    }

    private void generateStaticPages() throws Exception {
        List<Activity> list = activityMapper.list();
        Map<String,Object> data = new HashMap<>();
        data.put("list", list);

        staticizeUtil.createHtml("activity-list.ftl", "activity-list.html", data);

        for (Activity act : list) {
            data.put("act", act);
            staticizeUtil.createHtml("activity-detail.ftl", "activity-" + act.getId() + ".html", data);
        }
    }
}