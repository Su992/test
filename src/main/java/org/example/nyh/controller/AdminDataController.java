package org.example.nyh.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.nyh.common.Result;
import org.example.nyh.entity.SysUser;
import org.example.nyh.service.ISysUserService;
import org.example.nyh.service.ITestQuestionService;
import org.example.nyh.service.IUserSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @GetMapping("/preferences")
    public Result<List<Map<String, Object>>> getPreferencesStats() {
        // 获取所有设置了偏好的用户
        List<SysUser> users = userService.list(new LambdaQueryWrapper<SysUser>().isNotNull(SysUser::getPreferences));
        Map<String, Integer> counts = new HashMap<>();

        // 切割逗号分隔的字符串并计数
        for (SysUser user : users) {
            if (StringUtils.hasText(user.getPreferences())) {
                String[] prefs = user.getPreferences().split(",");
                for (String pref : prefs) {
                    pref = pref.trim();
                    if (!pref.isEmpty()) {
                        counts.put(pref, counts.getOrDefault(pref, 0) + 1);
                    }
                }
            }
        }

        // 转换为 ECharts 需要的 [{name: 'xxx', value: 10}] 格式
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", entry.getKey());
            map.put("value", entry.getValue());
            result.add(map);
        }

        return Result.success(result);
    }
}