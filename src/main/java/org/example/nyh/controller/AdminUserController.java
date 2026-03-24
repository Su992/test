package org.example.nyh.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.nyh.common.Result;
import org.example.nyh.entity.SysUser;
import org.example.nyh.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/user")
public class AdminUserController {

    @Autowired
    private ISysUserService userService;

    // 分页查询用户 (支持按用户名模糊查询)
    @GetMapping("/page")
    public Result<Page<SysUser>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String username) {
        
        Page<SysUser> page = new Page<>(current, size);
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getRole, 1);
        if (StringUtils.hasText(username)) {
            wrapper.like(SysUser::getUsername, username);
        }
        return Result.success(userService.page(page, wrapper));
    }

    // 新增用户 (通常管理员添加的是其他管理员或特殊用户)
    @PostMapping
    public Result<Boolean> add(@RequestBody SysUser user) {
        return Result.success(userService.save(user));
    }

    // 修改用户信息
    @PutMapping
    public Result<Boolean> update(@RequestBody SysUser user) {
        return Result.success(userService.updateById(user));
    }

    // 删除用户
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(userService.removeById(id));
    }
    @PostMapping("/login")
    public Result<SysUser> login(@RequestBody SysUser user) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, user.getUsername())
                .eq(SysUser::getPassword, user.getPassword());
        SysUser adminUser = userService.getOne(queryWrapper);

        if (adminUser != null) {
            // 角色校验：0表示管理员
            if (adminUser.getRole() == null || adminUser.getRole() != 0) {
                return Result.error(403, "权限不足，非管理员账号");
            }
            return Result.success(adminUser);
        }
        return Result.error("用户名或密码错误");
    }
}