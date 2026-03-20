// src/main/java/org/example/nyh/service/impl/TestRecordServiceImpl.java
package org.example.nyh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.nyh.entity.TestRecord;
import org.example.nyh.mapper.TestRecordMapper;
import org.example.nyh.service.ITestRecordService;
import org.springframework.stereotype.Service;

@Service
public class TestRecordServiceImpl extends ServiceImpl<TestRecordMapper, TestRecord> implements ITestRecordService {
}
