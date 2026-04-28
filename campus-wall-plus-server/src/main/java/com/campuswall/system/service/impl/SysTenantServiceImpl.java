package com.campuswall.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campuswall.system.entity.SysTenant;
import com.campuswall.system.mapper.SysTenantMapper;
import com.campuswall.system.service.SysTenantService;
import org.springframework.stereotype.Service;

@Service
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements SysTenantService {
}
