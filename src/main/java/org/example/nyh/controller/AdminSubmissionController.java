package org.example.nyh.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.nyh.common.Result;
import org.example.nyh.entity.UserSubmission;
import org.example.nyh.service.IUserSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/submission")
public class AdminSubmissionController {

    @Autowired
    private IUserSubmissionService submissionService;

    // 分页查询留言 (支持根据人工审核状态筛选：0待审核，1通过，2不通过)
    @GetMapping("/page")
    public Result<Page<UserSubmission>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer manualAuditStatus) {
        
        Page<UserSubmission> page = new Page<>(current, size);
        LambdaQueryWrapper<UserSubmission> wrapper = new LambdaQueryWrapper<>();
        if (manualAuditStatus != null) {
            wrapper.eq(UserSubmission::getManualAuditStatus, manualAuditStatus);
        }
        wrapper.orderByDesc(UserSubmission::getCreateTime);
        return Result.success(submissionService.page(page, wrapper));
    }

    // 人工审核接口 (传入 id 和 状态值)
    @PutMapping("/audit/{id}")
    public Result<Boolean> audit(@PathVariable Long id, @RequestParam Integer status) {
        if (status != 1 && status != 2) {
            return Result.error("非法的审核状态");
        }
        UserSubmission submission = new UserSubmission();
        submission.setId(id);
        submission.setManualAuditStatus(status);
        return Result.success(submissionService.updateById(submission));
    }
    
    // 删除违规留言
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(submissionService.removeById(id));
    }
}