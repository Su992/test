// src/main/java/org/example/nyh/service/impl/DrugKnowledgeServiceImpl.java
package org.example.nyh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.nyh.entity.DrugKnowledge;
import org.example.nyh.mapper.DrugKnowledgeMapper;
import org.example.nyh.service.IDrugKnowledgeService;
import org.springframework.stereotype.Service;

@Service
public class DrugKnowledgeServiceImpl extends ServiceImpl<DrugKnowledgeMapper, DrugKnowledge> implements IDrugKnowledgeService {
}
