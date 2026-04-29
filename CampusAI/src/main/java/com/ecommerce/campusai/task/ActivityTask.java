package com.ecommerce.campusai.task;

import com.ecommerce.campusai.mapper.ActivityMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ActivityTask {

    private final ActivityMapper activityMapper;

    public ActivityTask(ActivityMapper activityMapper) {
        this.activityMapper = activityMapper;
    }

    // 每分钟执行：截止自动关闭报名
    @Scheduled(fixedRate = 60000)
    public void autoUpdateStatus() {
        activityMapper.updateStatus();
    }
}