package com.ecommerce.campusai.service;

import java.util.Map;

public interface AiChatService {
    /**
     * 通用文本对话接口
     * @param prompt 提示词
     * @return 大模型返回的响应文本
     */
    String chat(String prompt);

    /**
     * 带参数的对话接口（用于复杂场景，如推荐、分析）
     * @param prompt 提示词
     * @param params 参数（如用户偏好、历史数据等）
     * @return 结构化响应（建议返回JSON格式，方便后续解析）
     */
    Map<String, Object> chatWithParams(String prompt, Map<String, Object> params);
}
