package com.campuswall.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campuswall.system.entity.SysOperationLog;
import com.campuswall.system.mapper.SysOperationLogMapper;
import com.campuswall.system.service.SysOperationLogService;
import org.springframework.stereotype.Service;

@Service
public class SysOperationLogServiceImpl extends ServiceImpl<SysOperationLogMapper, SysOperationLog> implements SysOperationLogService {
}
