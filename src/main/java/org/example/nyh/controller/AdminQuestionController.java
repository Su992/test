package org.example.nyh.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.nyh.common.Result;
import org.example.nyh.entity.TestQuestion;
import org.example.nyh.service.ITestQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/question")
public class AdminQuestionController {

    @Autowired
    private ITestQuestionService questionService;

    @GetMapping("/page")
    public Result<Page<TestQuestion>> page(@RequestParam(defaultValue = "1") Integer current,
                                           @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(questionService.page(new Page<>(current, size)));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody TestQuestion question) {
        return Result.success(questionService.save(question));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody TestQuestion question) {
        return Result.success(questionService.updateById(question));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(questionService.removeById(id));
    }
}