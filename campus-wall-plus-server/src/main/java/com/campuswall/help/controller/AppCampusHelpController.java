package com.campuswall.help.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campuswall.common.dto.IdDTO;
import com.campuswall.common.dto.PageQuery;
import com.campuswall.common.exception.BusinessException;
import com.campuswall.common.result.PageResult;
import com.campuswall.common.result.Result;
import com.campuswall.help.entity.CampusHelp;
import com.campuswall.help.service.CampusHelpService;
import com.campuswall.security.SecurityUtils;
import com.campuswall.tenant.TenantContext;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/app/help")
public class AppCampusHelpController {
    private final CampusHelpService service;

    public AppCampusHelpController(CampusHelpService service) {
        this.service = service;
    }

    @GetMapping("/page")
    public Result<PageResult<CampusHelp>> page(PageQuery query) {
        return Result.success(PageResult.of(service.page(
                new Page<>(query.getCurrent(), query.getSize()),
                new LambdaQueryWrapper<CampusHelp>()
                        .eq(CampusHelp::getStatus, 1)
                        .like(query.getKeyword() != null && !query.getKeyword().isBlank(), CampusHelp::getTitle, query.getKeyword())
                        .orderByDesc(CampusHelp::getCreatedAt)
        )));
    }

    @GetMapping("/detail")
    public Result<CampusHelp> detail(@RequestParam Long id) {
        return Result.success(service.getById(id));
    }

    @PostMapping("/publish")
    public Result<Long> publish(@RequestBody CampusHelp entity) {
        entity.setTenantId(TenantContext.getTenantId());
        entity.setUserId(SecurityUtils.userId());
        entity.setStatus(0);
        service.save(entity);
        return Result.success(entity.getId());
    }

    @GetMapping("/my-page")
    public Result<PageResult<CampusHelp>> myPage(PageQuery query) {
        return Result.success(PageResult.of(service.page(
                new Page<>(query.getCurrent(), query.getSize()),
                new LambdaQueryWrapper<CampusHelp>()
                        .eq(CampusHelp::getUserId, SecurityUtils.userId())
                        .orderByDesc(CampusHelp::getCreatedAt)
        )));
    }

    @PutMapping("/update")
    public Result<Void> update(@RequestBody CampusHelp entity) {
        CampusHelp old = service.getById(entity.getId());
        if (old == null) throw new BusinessException("互助信息不存在");
        if (!Objects.equals(old.getUserId(), SecurityUtils.userId())) throw new BusinessException("只能修改自己的内容");
        entity.setTenantId(old.getTenantId());
        entity.setUserId(old.getUserId());
        entity.setStatus(0);
        service.updateById(entity);
        return Result.success();
    }

    @PutMapping("/finish")
    public Result<Void> finish(@RequestBody IdDTO dto) {
        CampusHelp entity = service.getById(dto.getId());
        if (entity == null) throw new BusinessException("互助信息不存在");
        if (!Objects.equals(entity.getUserId(), SecurityUtils.userId())) throw new BusinessException("只能操作自己的内容");
        entity.setStatus(2);
        service.updateById(entity);
        return Result.success();
    }

    @DeleteMapping("/delete")
    public Result<Void> delete(@RequestParam Long id) {
        CampusHelp entity = service.getById(id);
        if (entity == null) throw new BusinessException("互助信息不存在");
        if (!Objects.equals(entity.getUserId(), SecurityUtils.userId())) throw new BusinessException("只能删除自己的内容");
        entity.setStatus(3);
        service.updateById(entity);
        return Result.success();
    }
}
