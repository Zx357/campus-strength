package com.campuswall.report.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campuswall.comment.entity.WallComment;
import com.campuswall.comment.service.WallCommentService;
import com.campuswall.common.controller.CrudController;
import com.campuswall.common.exception.BusinessException;
import com.campuswall.common.result.Result;
import com.campuswall.goods.entity.SecondGoods;
import com.campuswall.goods.service.SecondGoodsService;
import com.campuswall.lostfound.entity.LostFound;
import com.campuswall.lostfound.service.LostFoundService;
import com.campuswall.post.entity.WallPost;
import com.campuswall.post.service.WallPostService;
import com.campuswall.report.entity.ReportRecord;
import com.campuswall.report.service.ReportRecordService;
import com.campuswall.security.SecurityUtils;
import com.campuswall.user.entity.SysUser;
import com.campuswall.user.service.SysUserService;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin/report")
public class ReportRecordController extends CrudController<ReportRecord> {
    private final ReportRecordService service;
    private final WallPostService postService;
    private final WallCommentService commentService;
    private final SecondGoodsService goodsService;
    private final LostFoundService lostFoundService;
    private final SysUserService userService;

    public ReportRecordController(
            ReportRecordService service,
            WallPostService postService,
            WallCommentService commentService,
            SecondGoodsService goodsService,
            LostFoundService lostFoundService,
            SysUserService userService
    ) {
        this.service = service;
        this.postService = postService;
        this.commentService = commentService;
        this.goodsService = goodsService;
        this.lostFoundService = lostFoundService;
        this.userService = userService;
    }

    @Override
    protected IService<ReportRecord> service() {
        return service;
    }

    @PutMapping("/handle")
    public Result<Void> handle(@RequestBody HandleReportDTO dto) {
        ReportRecord report = service.getById(dto.getId());
        if (report == null) {
            throw new BusinessException("Report not found");
        }
        report.setStatus(dto.getStatus() == null ? 1 : dto.getStatus());
        report.setHandleResult(dto.getHandleResult());
        report.setHandlerId(SecurityUtils.userId());
        report.setHandledAt(LocalDateTime.now());
        service.updateById(report);
        if (Boolean.TRUE.equals(dto.getTakeDown())) {
            takeDown(report.getTargetType(), report.getTargetId());
        }
        return Result.success();
    }

    private void takeDown(String targetType, Long targetId) {
        if ("post".equals(targetType)) {
            WallPost entity = require(postService.getById(targetId));
            entity.setStatus(4);
            postService.updateById(entity);
            return;
        }
        if ("comment".equals(targetType)) {
            WallComment entity = require(commentService.getById(targetId));
            entity.setStatus(3);
            commentService.updateById(entity);
            return;
        }
        if ("goods".equals(targetType)) {
            SecondGoods entity = require(goodsService.getById(targetId));
            entity.setStatus(3);
            goodsService.updateById(entity);
            return;
        }
        if ("lost".equals(targetType) || "lost-found".equals(targetType)) {
            LostFound entity = require(lostFoundService.getById(targetId));
            entity.setStatus(3);
            lostFoundService.updateById(entity);
            return;
        }
        if ("user".equals(targetType)) {
            SysUser user = require(userService.getById(targetId));
            user.setStatus(0);
            userService.updateById(user);
            return;
        }
        throw new BusinessException("Unsupported report target: " + targetType);
    }

    private <T> T require(T entity) {
        if (entity == null) {
            throw new BusinessException("Report target not found");
        }
        return entity;
    }

    @Data
    public static class HandleReportDTO {
        @NotNull
        private Long id;
        private Integer status;
        private String handleResult;
        private Boolean takeDown;
    }
}
