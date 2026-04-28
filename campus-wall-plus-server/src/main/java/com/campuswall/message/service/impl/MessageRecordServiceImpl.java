package com.campuswall.message.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campuswall.message.entity.MessageRecord;
import com.campuswall.message.mapper.MessageRecordMapper;
import com.campuswall.message.service.MessageRecordService;
import org.springframework.stereotype.Service;

@Service
public class MessageRecordServiceImpl extends ServiceImpl<MessageRecordMapper, MessageRecord> implements MessageRecordService {
}
