package com.campuswall.notice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campuswall.notice.entity.Notice;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {
}
