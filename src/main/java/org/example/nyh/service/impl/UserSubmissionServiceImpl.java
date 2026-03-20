// src/main/java/org/example/nyh/service/impl/UserSubmissionServiceImpl.java
package org.example.nyh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.nyh.entity.UserSubmission;
import org.example.nyh.mapper.UserSubmissionMapper;
import org.example.nyh.service.IUserSubmissionService;
import org.springframework.stereotype.Service;

@Service
public class UserSubmissionServiceImpl extends ServiceImpl<UserSubmissionMapper, UserSubmission> implements IUserSubmissionService {
}
