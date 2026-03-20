// src/main/java/org/example/nyh/service/impl/SysAnnouncementServiceImpl.java
package org.example.nyh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.nyh.entity.SysAnnouncement;
import org.example.nyh.mapper.SysAnnouncementMapper;
import org.example.nyh.service.ISysAnnouncementService;
import org.springframework.stereotype.Service;

@Service
public class SysAnnouncementServiceImpl extends ServiceImpl<SysAnnouncementMapper, SysAnnouncement> implements ISysAnnouncementService {
}
