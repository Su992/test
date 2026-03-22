package org.example.nyh.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.nyh.common.Result;
import org.example.nyh.entity.SysAnnouncement;
import org.example.nyh.service.ISysAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/announcement")
public class AdminAnnouncementController {

    @Autowired
    private ISysAnnouncementService announcementService;

    @GetMapping("/page")
    public Result<Page<SysAnnouncement>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String title) {
        Page<SysAnnouncement> page = new Page<>(current, size);
        LambdaQueryWrapper<SysAnnouncement> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(title)) {
            wrapper.like(SysAnnouncement::getTitle, title);
        }
        wrapper.orderByDesc(SysAnnouncement::getCreateTime);
        return Result.success(announcementService.page(page, wrapper));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody SysAnnouncement announcement) {
        return Result.success(announcementService.save(announcement));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody SysAnnouncement announcement) {
        return Result.success(announcementService.updateById(announcement));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(announcementService.removeById(id));
    }
}