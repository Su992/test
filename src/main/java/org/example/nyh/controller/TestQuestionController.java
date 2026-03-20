package org.example.nyh.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.nyh.common.Result;
import org.example.nyh.entity.TestQuestion;
import org.example.nyh.service.ITestQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestQuestionController {

    @Autowired
    private ITestQuestionService questionService;

    // 随机获取 N 道测试题供用户练习
    @GetMapping("/random")
    public Result<List<TestQuestion>> getRandomQuestions(@RequestParam(defaultValue = "10") Integer limit) {
        // 使用 MySQL 的 rand() 函数进行随机排序抽取
        QueryWrapper<TestQuestion> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("RAND()").last("LIMIT " + limit);

        List<TestQuestion> questions = questionService.list(wrapper);
        return Result.success(questions);
    }
}