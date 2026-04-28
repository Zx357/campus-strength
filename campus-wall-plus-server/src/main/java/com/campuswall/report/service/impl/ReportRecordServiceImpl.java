package com.campuswall.report.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campuswall.report.entity.ReportRecord;
import com.campuswall.report.mapper.ReportRecordMapper;
import com.campuswall.report.service.ReportRecordService;
import org.springframework.stereotype.Service;

@Service
public class ReportRecordServiceImpl extends ServiceImpl<ReportRecordMapper, ReportRecord> implements ReportRecordService {
}
