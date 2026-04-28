package com.campuswall.audit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campuswall.audit.entity.AuditRecord;
import com.campuswall.audit.mapper.AuditRecordMapper;
import com.campuswall.audit.service.AuditRecordService;
import org.springframework.stereotype.Service;

@Service
public class AuditRecordServiceImpl extends ServiceImpl<AuditRecordMapper, AuditRecord> implements AuditRecordService {
}
