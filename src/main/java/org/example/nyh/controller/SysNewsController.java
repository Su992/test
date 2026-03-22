// 修改 org.example.nyh.controller.SysNewsController
package org.example.nyh.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.nyh.common.Result;
import org.example.nyh.entity.SysNews;
import org.example.nyh.service.ISysNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/sys-news")
public class SysNewsController {

    @Autowired
    private ISysNewsService newsService;

    // 获取首页推荐消息 (获取最新的3条)
    @GetMapping("/recommend")
    public Result<List<SysNews>> getRecommendNews() {
        LambdaQueryWrapper<SysNews> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(SysNews::getCreateTime).last("LIMIT 3");
        return Result.success(newsService.list(wrapper));
    }
}