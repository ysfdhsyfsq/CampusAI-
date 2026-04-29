package com.ecommerce.campusai.job;

import com.ecommerce.campusai.entity.Activity;
import com.ecommerce.campusai.mapper.ActivityMapper;
import com.ecommerce.campusai.util.StaticizeUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ActivityStatusJob {

    private final ActivityMapper activityMapper;
    private final StaticizeUtil staticizeUtil;

    public ActivityStatusJob(ActivityMapper activityMapper, StaticizeUtil staticizeUtil) {
        this.activityMapper = activityMapper;
        this.staticizeUtil = staticizeUtil;
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    public void updateExpiredActivities() {
        try {
            activityMapper.updateStatus();
            System.out.println("活动状态更新完成，静态页面已刷新");
            regenerateStaticPages();
        } catch (Exception e) {
            System.err.println("活动状态更新失败: " + e.getMessage());
        }
    }

    private void regenerateStaticPages() throws Exception {
        List<Activity> list = activityMapper.list();
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);

        staticizeUtil.createHtml("activity-list.ftl", "activity-list.html", data);

        for (Activity act : list) {
            data.put("act", act);
            staticizeUtil.createHtml("activity-detail.ftl", "activity-" + act.getId() + ".html", data);
        }
    }
}
