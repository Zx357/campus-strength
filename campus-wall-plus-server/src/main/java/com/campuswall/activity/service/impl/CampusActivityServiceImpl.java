package com.campuswall.activity.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campuswall.activity.entity.CampusActivity;
import com.campuswall.activity.mapper.CampusActivityMapper;
import com.campuswall.activity.service.CampusActivityService;
import org.springframework.stereotype.Service;

@Service
public class CampusActivityServiceImpl extends ServiceImpl<CampusActivityMapper, CampusActivity> implements CampusActivityService {
}
