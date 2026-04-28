package com.campuswall.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campuswall.post.entity.WallPost;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WallPostMapper extends BaseMapper<WallPost> {
}
