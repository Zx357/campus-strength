package com.campuswall.notice.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campuswall.common.dto.PageQuery;
import com.campuswall.common.result.PageResult;
import com.campuswall.common.result.Result;
import com.campuswall.notice.entity.Notice;
import com.campuswall.notice.mapper.NoticeMapper;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/app/notice")
public class AppNoticeController {
    private final NoticeMapper mapper;
    public AppNoticeController(NoticeMapper mapper) { this.mapper = mapper; }
    @GetMapping("/page")
    public Result<PageResult<Notice>> page(@Valid PageQuery query) {
        return Result.success(PageResult.of(mapper.selectPage(new Page<>(query.getCurrent(), query.getSize()), new LambdaQueryWrapper<Notice>().eq(Notice::getStatus, 1).orderByDesc(Notice::getTopFlag).orderByDesc(Notice::getCreatedAt))));
    }
}
