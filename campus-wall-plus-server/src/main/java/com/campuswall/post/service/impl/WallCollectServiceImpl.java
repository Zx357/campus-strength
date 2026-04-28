package com.campuswall.post.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campuswall.post.entity.WallCollect;
import com.campuswall.post.mapper.WallCollectMapper;
import com.campuswall.post.service.WallCollectService;
import org.springframework.stereotype.Service;

@Service
public class WallCollectServiceImpl extends ServiceImpl<WallCollectMapper, WallCollect> implements WallCollectService {
}
