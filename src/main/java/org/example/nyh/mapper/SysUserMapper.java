package org.example.nyh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.nyh.entity.SysUser;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
}