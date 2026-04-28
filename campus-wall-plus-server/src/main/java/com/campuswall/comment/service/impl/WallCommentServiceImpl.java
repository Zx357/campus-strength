package com.campuswall.comment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campuswall.comment.entity.WallComment;
import com.campuswall.comment.mapper.WallCommentMapper;
import com.campuswall.comment.service.WallCommentService;
import org.springframework.stereotype.Service;

@Service
public class WallCommentServiceImpl extends ServiceImpl<WallCommentMapper, WallComment> implements WallCommentService {
}
