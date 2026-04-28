package com.campuswall.audit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campuswall.audit.entity.AuditRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuditRecordMapper extends BaseMapper<AuditRecord> {
}
