package com.campuswall.lostfound.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.campuswall.common.controller.CrudController;
import com.campuswall.common.dto.IdDTO;
import com.campuswall.common.dto.PageQuery;
import com.campuswall.common.exception.BusinessException;
import com.campuswall.common.result.PageResult;
import com.campuswall.common.result.Result;
import com.campuswall.lostfound.entity.LostFound;
import com.campuswall.lostfound.service.LostFoundService;
import com.campuswall.security.SecurityUtils;
import com.campuswall.tenant.TenantContext;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/app/lost-found")
public class LostFoundController extends CrudController<LostFound> {
    private final LostFoundService service;

    public LostFoundController(LostFoundService service) {
        this.service = service;
    }

    @Override
    protected IService<LostFound> service() {
        return service;
    }

    @GetMapping("/my-page")
    public Result<PageResult<LostFound>> myPage(@Valid PageQuery query) {
        return Result.success(PageResult.of(service.page(
                new Page<>(query.getCurrent(), query.getSize()),
                new LambdaQueryWrapper<LostFound>()
                        .eq(LostFound::getUserId, SecurityUtils.userId())
                        .like(query.getKeyword() != null && !query.getKeyword().isBlank(), LostFound::getTitle, query.getKeyword())
                        .orderByDesc(LostFound::getCreatedAt)
        )));
    }

    @PostMapping("/publish")
    public Result<Long> publish(@RequestBody LostFound entity) {
        entity.setTenantId(TenantContext.getTenantId());
        entity.setUserId(SecurityUtils.userId());
        entity.setStatus(0);
        service.save(entity);
        return Result.success(entity.getId());
    }

    @PutMapping("/found")
    public Result<Void> found(@Valid @RequestBody IdDTO dto) {
        LostFound entity = service.getById(dto.getId());
        if (entity == null) throw new BusinessException("信息不存在");
        if (!Objects.equals(entity.getUserId(), SecurityUtils.userId())) throw new BusinessException("只能操作自己的内容");
        entity.setStatus(2);
        service.updateById(entity);
        return Result.success();
    }

    @Override
    @PutMapping("/update")
    public Result<Void> update(@RequestBody LostFound entity) {
        LostFound old = service.getById(entity.getId());
        if (old == null) throw new BusinessException("信息不存在");
        if (!Objects.equals(old.getUserId(), SecurityUtils.userId())) throw new BusinessException("只能修改自己的内容");
        entity.setTenantId(old.getTenantId());
        entity.setUserId(old.getUserId());
        entity.setStatus(0);
        service.updateById(entity);
        return Result.success();
    }

    @Override
    @DeleteMapping("/delete")
    public Result<Void> delete(@RequestParam Long id) {
        LostFound entity = service.getById(id);
        if (entity == null) throw new BusinessException("信息不存在");
        if (!Objects.equals(entity.getUserId(), SecurityUtils.userId())) throw new BusinessException("只能删除自己的内容");
        entity.setStatus(3);
        service.updateById(entity);
        return Result.success();
    }
}
