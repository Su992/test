package org.example.nyh.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.nyh.common.Result;
import org.example.nyh.entity.SysUser;
import org.example.nyh.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class SysUserController {

    @Autowired
    private ISysUserService userService;

    // 用户登录
    @PostMapping("/login")
    public Result<SysUser> login(@RequestBody SysUser user) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, user.getUsername())
                    .eq(SysUser::getPassword, user.getPassword());
        SysUser loginUser = userService.getOne(queryWrapper);
        
        if (loginUser != null) {
            return Result.success(loginUser);
        }
        return Result.error("用户名或密码错误");
    }

    // 用户注册
    @PostMapping("/register")
    public Result<Boolean> register(@RequestBody SysUser user) {
        // 检查用户名是否已存在
        long count = userService.count(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, user.getUsername()));
        if (count > 0) {
            return Result.error("用户名已存在");
        }
        user.setRole(1); // 默认注册为普通用户: 1
        boolean success = userService.save(user);
        return success ? Result.success(true) : Result.error("注册失败");
    }
    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody SysUser user) {
        return Result.success(userService.updateById(user));
    }
}