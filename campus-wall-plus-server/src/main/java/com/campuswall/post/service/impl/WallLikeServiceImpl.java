package com.campuswall.post.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campuswall.post.entity.WallLike;
import com.campuswall.post.mapper.WallLikeMapper;
import com.campuswall.post.service.WallLikeService;
import org.springframework.stereotype.Service;

@Service
public class WallLikeServiceImpl extends ServiceImpl<WallLikeMapper, WallLike> implements WallLikeService {
}
