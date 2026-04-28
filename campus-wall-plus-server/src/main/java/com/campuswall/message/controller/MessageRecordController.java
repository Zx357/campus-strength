package com.campuswall.message.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.campuswall.common.controller.CrudController;
import com.campuswall.common.dto.IdDTO;
import com.campuswall.common.dto.PageQuery;
import com.campuswall.common.result.PageResult;
import com.campuswall.common.result.Result;
import com.campuswall.message.entity.MessageRecord;
import com.campuswall.message.service.MessageRecordService;
import com.campuswall.security.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/app/message")
public class MessageRecordController extends CrudController<MessageRecord> {
    private final MessageRecordService service;

    public MessageRecordController(MessageRecordService service) {
        this.service = service;
    }

    @Override
    protected IService<MessageRecord> service() {
        return service;
    }

    @GetMapping("/page")
    public Result<PageResult<MessageRecord>> page(@Valid PageQuery query) {
        Long userId = SecurityUtils.userId();
        return Result.success(
                PageResult.of(
                        service.page(
                                new Page<>(query.getCurrent(), query.getSize()),
                                new LambdaQueryWrapper<MessageRecord>()
                                        .eq(MessageRecord::getUserId, userId)
                                        .orderByAsc(MessageRecord::getReadFlag)
                                        .orderByDesc(MessageRecord::getCreatedAt)
                        )
                )
        );
    }

    @PutMapping("/read")
    public Result<Void> read(@Valid @RequestBody IdDTO dto) {
        Long userId = SecurityUtils.userId();
        MessageRecord entity = service.getById(dto.getId());
        if (entity != null && userId != null && userId.equals(entity.getUserId())) {
            entity.setReadFlag(1);
            service.updateById(entity);
        }
        return Result.success();
    }
}
