package org.example.nyh.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.nyh.common.Result;
import org.example.nyh.entity.DrugKnowledge;
import org.example.nyh.service.IDrugKnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/drug-knowledge")
public class DrugKnowledgeController {
    @Autowired
    private IDrugKnowledgeService drugKnowledgeService;

    @GetMapping("/{id}")
    public Result<DrugKnowledge> getById(@PathVariable Long id) {
        DrugKnowledge knowledge = drugKnowledgeService.getById(id);
        return knowledge != null ? Result.success(knowledge) : Result.error("未找到相关数据");
    }

    // 示例请求：GET /drug-knowledge/page?current=1&size=10
    @GetMapping("/page")
    public Result<Page<DrugKnowledge>> pageQuery(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {

        Page<DrugKnowledge> page = new Page<>(current, size);
        // 此处可以使用 QueryWrapper 加入条件查询，为了演示目前是全量分页
        Page<DrugKnowledge> resultPage = drugKnowledgeService.page(page);

        return Result.success(resultPage);
    }

    // 新增科普
    @PostMapping
    public Result<Boolean> add(@RequestBody DrugKnowledge drugKnowledge) {
        return Result.success(drugKnowledgeService.save(drugKnowledge));
    }

    // 修改科普
    @PutMapping
    public Result<Boolean> update(@RequestBody DrugKnowledge drugKnowledge) {
        return Result.success(drugKnowledgeService.updateById(drugKnowledge));
    }

    // 删除科普
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(drugKnowledgeService.removeById(id));
    }

}