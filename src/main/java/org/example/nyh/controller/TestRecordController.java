package org.example.nyh.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.nyh.common.Result;
import org.example.nyh.entity.TestRecord;
import org.example.nyh.service.ITestRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/test-record")
public class TestRecordController {

    @Autowired
    private ITestRecordService testRecordService;

    // 1. 交卷时保存测试记录
    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody TestRecord testRecord) {
        testRecord.setCreateTime(LocalDateTime.now());
        return Result.success(testRecordService.save(testRecord));
    }

    // 2. 个人中心获取我的测试记录
    @GetMapping("/my-records")
    public Result<List<TestRecord>> myRecords(@RequestParam Long userId) {
        LambdaQueryWrapper<TestRecord> wrapper = new LambdaQueryWrapper<>();
        // 按时间倒序，最新的在前面
        wrapper.eq(TestRecord::getUserId, userId).orderByDesc(TestRecord::getCreateTime);
        return Result.success(testRecordService.list(wrapper));
    }
}