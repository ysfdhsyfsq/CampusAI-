package com.ecommerce.campusai.service;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationOutput;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QwenChatServiceImpl implements AiChatService {

    @Value("${dashscope.api.key}")
    private String apiKey;

    @Value("${dashscope.api.model}")
    private String model;

    @Override
    public String chat(String prompt) {
        try {
            Generation gen = new Generation();
            List<Message> messages = new ArrayList<>();
            messages.add(Message.builder()
                    .role(Role.SYSTEM.getValue())
                    .content("你是CampusAI智慧校园助手，请用简洁友好的语气回答校园相关问题。")
                    .build());
            messages.add(Message.builder()
                    .role(Role.USER.getValue())
                    .content(prompt)
                    .build());

            GenerationParam param = GenerationParam.builder()
                    .apiKey(apiKey)
                    .model(model)
                    .messages(messages)
                    .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                    .build();

            GenerationResult result = gen.call(param);
            GenerationOutput output = result.getOutput();
            return output.getChoices().get(0).getMessage().getContent();
        } catch (NoApiKeyException | InputRequiredException e) {
            throw new RuntimeException("通义千问调用失败：" + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> chatWithParams(String prompt, Map<String, Object> params) {
        // 对于需要结构化响应的场景，可以在prompt中要求返回JSON格式
        String jsonPrompt = prompt + "\n请以JSON格式返回，包含以下字段：{...}";
        String response = chat(jsonPrompt);
        // 实际项目中建议使用JSON解析库（如Jackson）转换为Map
        // 这里简化为直接返回（需自行完善JSON解析逻辑）
        Map<String, Object> result = new HashMap<>();
        result.put("response", response);
        return result;
    }
}
