package com.campuswall.file.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campuswall.file.entity.FileRecord;
import com.campuswall.file.mapper.FileRecordMapper;
import com.campuswall.file.service.FileRecordService;
import org.springframework.stereotype.Service;

@Service
public class FileRecordServiceImpl extends ServiceImpl<FileRecordMapper, FileRecord> implements FileRecordService {
}
