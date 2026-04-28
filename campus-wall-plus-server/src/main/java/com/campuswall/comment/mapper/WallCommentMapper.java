package com.campuswall.comment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campuswall.comment.entity.WallComment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WallCommentMapper extends BaseMapper<WallComment> {
}
