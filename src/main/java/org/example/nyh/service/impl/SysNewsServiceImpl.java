// src/main/java/org/example/nyh/service/impl/SysNewsServiceImpl.java
package org.example.nyh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.nyh.entity.SysNews;
import org.example.nyh.mapper.SysNewsMapper;
import org.example.nyh.service.ISysNewsService;
import org.springframework.stereotype.Service;

@Service
public class SysNewsServiceImpl extends ServiceImpl<SysNewsMapper, SysNews> implements ISysNewsService {
}
