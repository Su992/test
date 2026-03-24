package org.example.nyh.controller;

import org.example.nyh.common.Result;
import org.example.nyh.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/ai-assistant")
public class AiAssistantController {

    @Autowired
    private AiService aiService;

    // 系统预设指令：严格定义 AI 的角色
    private static final String SYSTEM_PROMPT = 
        "你是一个专业、温和且具有同理心的“禁毒科普小助手”。" +
        "你的任务是回答用户关于毒品知识、禁毒法律法规以及戒毒相关的问题。" +
        "你也可以进行正常的情感交流和心理疏导，但请务必引导用户远离毒品，保持积极向上的生活态度。" +
        "你可以解答用户关于编程、生活等各个领域的问题，但请在适当时机结合禁毒知识进行正能量引导。";

    @PostMapping("/chat")
    public Result<String> chat(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return Result.error("消息不能为空");
        }
        
        // 调用大模型获取回复
        String aiReply = aiService.callDeepSeek(SYSTEM_PROMPT, userMessage);
        return Result.success(aiReply);
    }
}