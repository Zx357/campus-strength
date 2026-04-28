package com.campuswall.post.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campuswall.post.entity.WallPost;
import com.campuswall.post.mapper.WallPostMapper;
import com.campuswall.post.service.WallPostService;
import org.springframework.stereotype.Service;

@Service
public class WallPostServiceImpl extends ServiceImpl<WallPostMapper, WallPost> implements WallPostService {
}
