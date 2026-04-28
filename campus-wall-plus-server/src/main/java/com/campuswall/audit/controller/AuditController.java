package com.campuswall.audit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campuswall.audit.dto.AuditDTO;
import com.campuswall.audit.entity.AuditRecord;
import com.campuswall.audit.mapper.AuditRecordMapper;
import com.campuswall.comment.entity.WallComment;
import com.campuswall.comment.service.WallCommentService;
import com.campuswall.common.dto.PageQuery;
import com.campuswall.common.exception.BusinessException;
import com.campuswall.common.result.PageResult;
import com.campuswall.common.result.Result;
import com.campuswall.goods.entity.SecondGoods;
import com.campuswall.goods.service.SecondGoodsService;
import com.campuswall.help.entity.CampusHelp;
import com.campuswall.help.service.CampusHelpService;
import com.campuswall.activity.entity.CampusActivity;
import com.campuswall.activity.service.CampusActivityService;
import com.campuswall.lostfound.entity.LostFound;
import com.campuswall.lostfound.service.LostFoundService;
import com.campuswall.post.entity.WallPost;
import com.campuswall.post.service.WallPostService;
import com.campuswall.security.SecurityUtils;
import com.campuswall.tenant.TenantContext;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin/audit")
public class AuditController {
    private final AuditRecordMapper mapper;
    private final WallPostService postService;
    private final WallCommentService commentService;
    private final LostFoundService lostFoundService;
    private final SecondGoodsService goodsService;
    private final CampusHelpService helpService;
    private final CampusActivityService activityService;

    public AuditController(
            AuditRecordMapper mapper,
            WallPostService postService,
            WallCommentService commentService,
            LostFoundService lostFoundService,
            SecondGoodsService goodsService,
            CampusHelpService helpService,
            CampusActivityService activityService
    ) {
        this.mapper = mapper;
        this.postService = postService;
        this.commentService = commentService;
        this.lostFoundService = lostFoundService;
        this.goodsService = goodsService;
        this.helpService = helpService;
        this.activityService = activityService;
    }

    @GetMapping("/page")
    public Result<PageResult<AuditRecord>> page(@Valid PageQuery query) {
        return Result.success(PageResult.of(mapper.selectPage(new Page<>(query.getCurrent(), query.getSize()), new LambdaQueryWrapper<AuditRecord>().orderByDesc(AuditRecord::getCreatedAt))));
    }

    @PostMapping("/pass")
    public Result<Void> pass(@Valid @RequestBody AuditDTO dto) {
        save(dto, 1);
        return Result.success();
    }

    @PostMapping("/reject")
    public Result<Void> reject(@Valid @RequestBody AuditDTO dto) {
        save(dto, 2);
        return Result.success();
    }

    @GetMapping("/record")
    public Result<PageResult<AuditRecord>> record(@Valid PageQuery query) {
        return page(query);
    }

    private void save(AuditDTO dto, int status) {
        applyTargetStatus(dto, status);
        AuditRecord record = new AuditRecord();
        record.setTenantId(TenantContext.getTenantId());
        record.setTargetType(dto.getTargetType());
        record.setTargetId(dto.getTargetId());
        record.setAuditStatus(status);
        record.setAuditReason(dto.getReason());
        record.setAuditorId(SecurityUtils.userId());
        record.setAuditedAt(LocalDateTime.now());
        mapper.insert(record);
    }

    private void applyTargetStatus(AuditDTO dto, int auditStatus) {
        String type = dto.getTargetType();
        Long id = dto.getTargetId();
        String reason = dto.getReason();
        boolean pass = auditStatus == 1;
        if ("post".equals(type)) {
            WallPost entity = require(postService.getById(id));
            entity.setStatus(pass ? 1 : 2);
            entity.setAuditReason(reason);
            postService.updateById(entity);
            return;
        }
        if ("comment".equals(type)) {
            WallComment entity = require(commentService.getById(id));
            entity.setStatus(pass ? 1 : 2);
            commentService.updateById(entity);
            return;
        }
        if ("lost".equals(type) || "lost-found".equals(type)) {
            LostFound entity = require(lostFoundService.getById(id));
            entity.setStatus(pass ? 1 : 4);
            entity.setAuditReason(reason);
            lostFoundService.updateById(entity);
            return;
        }
        if ("goods".equals(type)) {
            SecondGoods entity = require(goodsService.getById(id));
            entity.setStatus(pass ? 1 : 4);
            entity.setAuditReason(reason);
            goodsService.updateById(entity);
            return;
        }
        if ("help".equals(type)) {
            CampusHelp entity = require(helpService.getById(id));
            entity.setStatus(pass ? 1 : 4);
            helpService.updateById(entity);
            return;
        }
        if ("activity".equals(type)) {
            CampusActivity entity = require(activityService.getById(id));
            entity.setStatus(pass ? 1 : 2);
            activityService.updateById(entity);
            return;
        }
        throw new BusinessException("Unsupported audit target: " + type);
    }

    private <T> T require(T entity) {
        if (entity == null) {
            throw new BusinessException("Audit target not found");
        }
        return entity;
    }
}
