
package com.ecommerce.campusai.service;

import com.ecommerce.campusai.entity.Activity;
import com.ecommerce.campusai.mapper.ActivityMapper;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ActivitySummaryService {

    private final ActivityMapper activityMapper;
    private final AiChatService aiChatService;

    public ActivitySummaryService(ActivityMapper activityMapper,
                                  AiChatService aiChatService) {
        this.activityMapper = activityMapper;
        this.aiChatService = aiChatService;
    }

    /**
     * 生成活动智能摘要
     */
    public Map<String, Object> generateActivitySummary(Integer activityId) {
        Activity activity = activityMapper.getById(activityId);
        
        if (activity == null) {
            return createErrorResult("活动不存在");
        }

        Map<String, Object> summary = new HashMap<>();
        
        Map<String, String> extractedInfo = extractKeyInformation(activity);
        
        String oneSentenceSummary = generateOneSentenceSummary(extractedInfo);
        
        String detailedSummary = generateDetailedSummary(activity, extractedInfo);
        
        String highlights = generateActivityHighlights(extractedInfo);
        
        summary.put("activityId", activityId);
        summary.put("title", activity.getTitle());
        summary.put("extractedInfo", extractedInfo);
        summary.put("oneSentenceSummary", oneSentenceSummary);
        summary.put("detailedSummary", detailedSummary);
        summary.put("highlights", highlights);
        summary.put("generatedAt", java.time.LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        return summary;
    }

    /**
     * 从活动内容中提取关键信息
     */
    private Map<String, String> extractKeyInformation(Activity activity) {
        Map<String, String> info = new HashMap<>();
        String content = activity.getContent() != null ? activity.getContent() : "";
        String title = activity.getTitle() != null ? activity.getTitle() : "";
        
        info.put("title", title);
        
        Pattern timePattern = Pattern.compile("(\\d{4}[-/]\\d{1,2}[-/]\\d{1,2})[\\s\\u4e00-\\u9fa5]*(\\d{1,2}:\\d{2})?");
        Matcher timeMatcher = timePattern.matcher(content);
        if (timeMatcher.find()) {
            info.put("time", timeMatcher.group(0));
        } else if (activity.getEndTime() != null) {
            info.put("time", activity.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        }
        
        Pattern locationPattern = Pattern.compile("[\\u4e00-\\u9fa5]*(楼|馆|室|厅|场|中心)[\\u4e00-\\u9fa5]*\\d*号?");
        Matcher locationMatcher = locationPattern.matcher(content);
        if (locationMatcher.find()) {
            info.put("location", locationMatcher.group(0));
        } else {
            info.put("location", "待定");
        }
        
        Pattern organizerPattern = Pattern.compile("(主办|承办|组织)[\\uff1a:][\\s]*([\\u4e00-\\u9fa5]+)");
        Matcher organizerMatcher = organizerPattern.matcher(content);
        if (organizerMatcher.find()) {
            info.put("organizer", organizerMatcher.group(2));
        } else {
            info.put("organizer", "校团委");
        }
        
        if (content.length() > 50) {
            info.put("briefContent", content.substring(0, 50) + "...");
        } else {
            info.put("briefContent", content);
        }
        
        info.put("maxParticipants", activity.getMaxNum() != null ? activity.getMaxNum().toString() : "不限");
        info.put("currentEnrollments", activity.getEnrollNum() != null ? activity.getEnrollNum().toString() : "0");
        
        return info;
    }

    /**
     * 生成一句话摘要
     */
    private String generateOneSentenceSummary(Map<String, String> info) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("请将以下活动信息浓缩成一句话摘要（30字以内）：\n\n");
        prompt.append("活动名称：").append(info.get("title")).append("\n");
        prompt.append("时间：").append(info.getOrDefault("time", "待定")).append("\n");
        prompt.append("地点：").append(info.getOrDefault("location", "待定")).append("\n");
        prompt.append("主办方：").append(info.getOrDefault("organizer", "未知")).append("\n");
        prompt.append("内容简介：").append(info.getOrDefault("briefContent", "")).append("\n\n");
        prompt.append("要求：\n");
        prompt.append("1. 包含活动名称和核心亮点\n");
        prompt.append("2. 简洁明了，吸引人\n");
        prompt.append("3. 不超过30个字");
        
        return aiChatService.chat(prompt.toString());
    }

    /**
     * 生成详细摘要
     */
    private String generateDetailedSummary(Activity activity, Map<String, String> info) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("作为CampusAI智慧校园活动助手，请为以下活动生成一份吸引人的详细摘要：\n\n");
        prompt.append("【活动基本信息】\n");
        prompt.append("活动名称：").append(info.get("title")).append("\n");
        prompt.append("活动时间：").append(info.getOrDefault("time", "待定")).append("\n");
        prompt.append("活动地点：").append(info.getOrDefault("location", "待定")).append("\n");
        prompt.append("主办单位：").append(info.getOrDefault("organizer", "未知")).append("\n");
        prompt.append("参与人数：已报名").append(info.getOrDefault("currentEnrollments", "0"))
              .append("/").append(info.getOrDefault("maxParticipants", "不限")).append("人\n\n");
        
        prompt.append("【活动内容】\n");
        prompt.append(activity.getContent() != null ? activity.getContent() : "暂无详细内容").append("\n\n");
        
        prompt.append("请生成200字以内的活动摘要，包含：\n");
        prompt.append("1. 活动亮点介绍\n");
        prompt.append("2. 适合人群\n");
        prompt.append("3. 参与价值\n");
        prompt.append("4. 报名方式提示\n");
        prompt.append("语气要热情活泼，吸引学生参与。");
        
        return aiChatService.chat(prompt.toString());
    }

    /**
     * 生成活动亮点
     */
    private String generateActivityHighlights(Map<String, String> info) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("请从以下活动信息中提取3-5个亮点，用简短的标签形式呈现：\n\n");
        prompt.append("活动：").append(info.get("title")).append("\n");
        prompt.append("内容：").append(info.getOrDefault("briefContent", "")).append("\n\n");
        prompt.append("格式示例：🎯 专业讲座 | 🎁 精美礼品 | 👥 互动交流\n");
        prompt.append("请使用emoji图标，每个亮点不超过6个字，用 | 分隔。");
        
        return aiChatService.chat(prompt.toString());
    }

    /**
     * 批量生成活动摘要
     */
    public Map<String, Object> batchGenerateSummaries(java.util.List<Integer> activityIds) {
        Map<String, Object> result = new HashMap<>();
        java.util.List<Map<String, Object>> summaries = new java.util.ArrayList<>();
        
        for (Integer activityId : activityIds) {
            try {
                Map<String, Object> summary = generateActivitySummary(activityId);
                summaries.add(summary);
            } catch (Exception e) {
                Map<String, Object> errorSummary = new HashMap<>();
                errorSummary.put("activityId", activityId);
                errorSummary.put("error", "生成摘要失败：" + e.getMessage());
                summaries.add(errorSummary);
            }
        }
        
        result.put("totalCount", activityIds.size());
        result.put("successCount", summaries.stream().filter(s -> !s.containsKey("error")).count());
        result.put("summaries", summaries);
        
        return result;
    }

    /**
     * 创建错误结果
     */
    private Map<String, Object> createErrorResult(String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("error", message);
        return result;
    }
}
