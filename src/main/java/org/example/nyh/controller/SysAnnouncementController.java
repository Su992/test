package org.example.nyh.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.nyh.common.Result;
import org.example.nyh.entity.SysAnnouncement;
import org.example.nyh.service.ISysAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/sys-announcement")
public class SysAnnouncementController {
    @Autowired
    private ISysAnnouncementService announcementService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 获取首页发布的公告列表 (状态为1表示已发布)
    @GetMapping("/announcements")
    public Result<Page<SysAnnouncement>> getActiveAnnouncements(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "5") Integer size) {

        // 1. 构建 Redis Key (例如：announcement:page:1:5)
        String redisKey = "announcement:page:" + current + ":" + size;

        // 2. 先查 Redis 缓存
        Page<SysAnnouncement> cachedPage = (Page<SysAnnouncement>) redisTemplate.opsForValue().get(redisKey);
        if (cachedPage != null) {
            System.out.println("走 Redis 缓存获取公告数据~");
            return Result.success(cachedPage);
        }

        // 3. 缓存没有，查数据库
        System.out.println("走数据库查询公告数据...");
        Page<SysAnnouncement> page = new Page<>(current, size);
        LambdaQueryWrapper<SysAnnouncement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysAnnouncement::getStatus, 1)
                .orderByDesc(SysAnnouncement::getCreateTime);
        Page<SysAnnouncement> resultPage = announcementService.page(page, wrapper);

        // 4. 将查到的结果存入 Redis，设置 10 分钟过期
        redisTemplate.opsForValue().set(redisKey, resultPage, 10, TimeUnit.MINUTES);

        return Result.success(resultPage);
    }

    // 获取单条公告详情
    @GetMapping("/announcement/{id}")
    public Result<SysAnnouncement> getAnnouncementDetail(@PathVariable Long id) {
        return Result.success(announcementService.getById(id));
    }
}
