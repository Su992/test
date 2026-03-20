package org.example.nyh.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("drug_knowledge")
public class DrugKnowledge implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String category;
    private String coverImage;
    private String content;
    @TableLogic
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}