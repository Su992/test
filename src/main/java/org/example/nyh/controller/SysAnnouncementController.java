package org.example.nyh.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.nyh.common.Result;
import org.example.nyh.entity.SysAnnouncement;
import org.example.nyh.service.ISysAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sys-announcement")
public class SysAnnouncementController {
    @Autowired
    private ISysAnnouncementService announcementService;

    // 获取首页发布的公告列表 (状态为1表示已发布)
    @GetMapping("/announcements")
    public Result<Page<SysAnnouncement>> getActiveAnnouncements(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "5") Integer size) {

        Page<SysAnnouncement> page = new Page<>(current, size);
        LambdaQueryWrapper<SysAnnouncement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysAnnouncement::getStatus, 1) // 仅查询已发布
                .orderByDesc(SysAnnouncement::getCreateTime); // 按时间倒序

        return Result.success(announcementService.page(page, wrapper));
    }

    // 获取单条公告详情
    @GetMapping("/announcement/{id}")
    public Result<SysAnnouncement> getAnnouncementDetail(@PathVariable Long id) {
        return Result.success(announcementService.getById(id));
    }
}
