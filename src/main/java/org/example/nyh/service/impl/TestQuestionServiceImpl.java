// src/main/java/org/example/nyh/service/impl/TestQuestionServiceImpl.java
package org.example.nyh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.nyh.entity.TestQuestion;
import org.example.nyh.mapper.TestQuestionMapper;
import org.example.nyh.service.ITestQuestionService;
import org.springframework.stereotype.Service;

@Service
public class TestQuestionServiceImpl extends ServiceImpl<TestQuestionMapper, TestQuestion> implements ITestQuestionService {
}
