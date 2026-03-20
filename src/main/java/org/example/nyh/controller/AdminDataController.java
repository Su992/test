package org.example.nyh.controller;

import org.example.nyh.common.Result;
import org.example.nyh.service.ISysUserService;
import org.example.nyh.service.ITestQuestionService;
import org.example.nyh.service.IUserSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/data")
public class AdminDataController {

    @Autowired
    private ISysUserService userService;
    @Autowired
    private IUserSubmissionService submissionService;
    @Autowired
    private ITestQuestionService questionService;

    // 获取基础统计数据
    @GetMapping("/dashboard")
    public Result<Map<String, Long>> getDashboardData() {
        Map<String, Long> data = new HashMap<>();
        data.put("totalUsers", userService.count()); // 总用户数
        data.put("totalSubmissions", submissionService.count()); // 总留言投稿数
        data.put("totalQuestions", questionService.count()); // 总试题数
        return Result.success(data);
    }
}