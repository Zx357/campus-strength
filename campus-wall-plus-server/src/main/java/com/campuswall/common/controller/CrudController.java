package com.campuswall.common.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.campuswall.common.dto.PageQuery;
import com.campuswall.common.dto.StatusDTO;
import com.campuswall.common.model.BaseEntity;
import com.campuswall.common.model.TenantEntity;
import com.campuswall.common.result.PageResult;
import com.campuswall.common.result.Result;
import com.campuswall.tenant.TenantContext;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

public abstract class CrudController<T extends BaseEntity> {
    protected abstract IService<T> service();

    @GetMapping("/page")
    public Result<PageResult<T>> page(@Valid PageQuery query) {
        return Result.success(PageResult.of(service().page(new Page<>(query.getCurrent(), query.getSize()), new QueryWrapper<T>().orderByDesc("created_at"))));
    }

    @GetMapping("/detail")
    public Result<T> detail(@RequestParam Long id) {
        return Result.success(service().getById(id));
    }

    @PostMapping("/create")
    public Result<Long> create(@RequestBody T entity) {
        if (entity instanceof TenantEntity tenantEntity && tenantEntity.getTenantId() == null) {
            tenantEntity.setTenantId(TenantContext.getTenantId());
        }
        service().save(entity);
        return Result.success(entity.getId());
    }

    @PutMapping("/update")
    public Result<Void> update(@RequestBody T entity) {
        service().updateById(entity);
        return Result.success();
    }

    @DeleteMapping("/delete")
    public Result<Void> delete(@RequestParam Long id) {
        service().removeById(id);
        return Result.success();
    }

    @PutMapping("/status")
    public Result<Void> status(@Valid @RequestBody StatusDTO dto) {
        T entity = service().getById(dto.getId());
        if (entity != null) {
            try {
                entity.getClass().getMethod("setStatus", Integer.class).invoke(entity, dto.getStatus());
            } catch (Exception ignored) {
            }
            service().updateById(entity);
        }
        return Result.success();
    }
}
