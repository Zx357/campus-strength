package com.campuswall.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campuswall.system.entity.SensitiveWord;
import com.campuswall.system.mapper.SensitiveWordMapper;
import com.campuswall.system.service.SensitiveWordService;
import org.springframework.stereotype.Service;

@Service
public class SensitiveWordServiceImpl extends ServiceImpl<SensitiveWordMapper, SensitiveWord> implements SensitiveWordService {
}
