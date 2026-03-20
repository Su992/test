package org.example.nyh.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class SysUser implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String avatar;
    private Integer role;
    private String preferences;
    @TableLogic // 逻辑删除注解
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}