package org.example.nyh.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.nyh.common.Result;
import org.example.nyh.entity.UserSubmission;
import org.example.nyh.service.AiService;
import org.example.nyh.service.IUserSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/submission")
public class UserSubmissionController {

    @Autowired
    private IUserSubmissionService submissionService;

    @Autowired
    private AiService aiService;

    private static final String AUDIT_SYSTEM_PROMPT =
            "你是一个内容安全审核员。请仔细检查用户提交的文本。" +
                    "判断是否包含违法、涉黄、涉政、暴力、侮辱谩骂或鼓励吸毒的不良言论。" +
                    "请严格按以下格式输出，不要有任何多余的话语：" +
                    "结果:[通过/不通过] | 原因:[如果通过填无，如果不通过请简述原因]";

    // 用户提交留言/投稿
    @PostMapping("/add")
    public Result<Boolean> addSubmission(@RequestBody UserSubmission submission) {
        // 1. 调用 AI 进行文本预审
        String contentToAudit = "标题：" + submission.getTitle() + "，内容：" + submission.getContent();
        String aiResult = aiService.callDeepSeek(AUDIT_SYSTEM_PROMPT, contentToAudit);

        // 2. 解析 AI 返回的结果 (例如: "结果:[通过] | 原因:[无]")
        if (aiResult.contains("结果:[不通过]")) {
            submission.setAiAuditStatus(2); // AI 审核不通过
            // 提取原因
            String remark = aiResult.substring(aiResult.indexOf("原因:[") + 4, aiResult.lastIndexOf("]"));
            submission.setAiAuditRemark(remark);
        } else {
            submission.setAiAuditStatus(1); // AI 审核通过
            submission.setAiAuditRemark("AI审核通过");
        }

        // 人工审核初始默认为 0（待审核），无论AI过没过，都需要人工复核才能发布
        submission.setManualAuditStatus(0);

        // 3. 保存入库
        boolean success = submissionService.save(submission);
        return success ? Result.success(true) : Result.error("提交失败");
    }

    // 获取前端展示的留言列表 (必须是人工审核通过的, status=1)
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