package org.example.nyh.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.nyh.common.Result;
import org.example.nyh.entity.UserSubmission;
import org.example.nyh.service.AiService;
import org.example.nyh.service.IUserSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/submission")
public class UserSubmissionController {

    @Autowired
    private IUserSubmissionService submissionService;

    @Autowired
    private AiService aiService;

    // 采用极简严厉的 Prompt，防止大模型发散乱说话
    private static final String AUDIT_SYSTEM_PROMPT =
            "你是一个严格的内容安全审核系统。请检查以下文本是否包含脏话、辱骂、暴力、政治敏感、色情等违规内容。\n" +
                    "【严格规则】：\n" +
                    "1. 如果文本合规，请严格且仅回复“PASS”。\n" +
                    "2. 如果发现违规词汇（如傻逼、cnmd等脏话），请严格且仅回复“REJECT:违规原因”（例如：REJECT:包含低俗辱骂词汇）。\n" +
                    "绝对不要输出任何其他多余的解释或标点符号！";

    // 用户提交留言/投稿
    @PostMapping("/add")
    public Result<Boolean> addSubmission(@RequestBody UserSubmission submission) {
        // 1. 调用 AI 进行文本预审
        String contentToAudit = "标题：" + submission.getTitle() + "\n内容：" + submission.getContent();

        try {
            // 获取 AI 结果并去除首尾空格
            String aiResult = aiService.callDeepSeek(AUDIT_SYSTEM_PROMPT, contentToAudit).trim();

            // 2. 核心拦截逻辑：只要包含 REJECT，直接驳回！
            if (aiResult.contains("REJECT")) {
                // 兼容中英文冒号，提取后面的违规原因
                String reason = aiResult.replaceAll(".*REJECT[:：]", "").trim();
                if (reason.isEmpty()) {
                    reason = "包含不良或违规言论";
                }
                // 【重点】直接返回 Result.error！这样前端的 Axios 拦截器会立刻捕获并在屏幕上方弹出红色的违规提示，且数据不会存入数据库。
                return Result.error("AI 安全拦截：" + reason);
            }

            // 3. 走到这里说明 AI 返回了 PASS，允许落库
            submission.setAiAuditStatus(1); // AI 审核通过
            submission.setAiAuditRemark("AI预审合规");
            submission.setManualAuditStatus(0); // 待人工复核
            submission.setCreateTime(LocalDateTime.now());

            boolean success = submissionService.save(submission);
            return success ? Result.success(true) : Result.error("提交失败");

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("AI 审核服务繁忙，请稍后再试");
        }
    }

    // 获取前端展示的留言列表
    @GetMapping("/public-list")
    public Result<Page<UserSubmission>> getPublicSubmissions(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {

        Page<UserSubmission> page = new Page<>(current, size);
        LambdaQueryWrapper<UserSubmission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserSubmission::getManualAuditStatus, 1) // 人工审核通过
                .orderByDesc(UserSubmission::getCreateTime);

        return Result.success(submissionService.page(page, wrapper));
    }
}