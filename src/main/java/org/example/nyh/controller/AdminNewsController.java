package org.example.nyh.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.nyh.common.Result;
import org.example.nyh.entity.SysNews;
import org.example.nyh.service.ISysNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/news")
public class AdminNewsController {

    @Autowired
    private ISysNewsService newsService;

    // 分页查询推荐消息
    @GetMapping("/page")
    public Result<Page<SysNews>> page(@RequestParam(defaultValue = "1") Integer current,
                                      @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(newsService.page(new Page<>(current, size)));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody SysNews news) {
        return Result.success(newsService.save(news));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody SysNews news) {
        return Result.success(newsService.updateById(news));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(newsService.removeById(id));
    }
}