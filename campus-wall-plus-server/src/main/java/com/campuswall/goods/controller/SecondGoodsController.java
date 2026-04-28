package com.campuswall.goods.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.campuswall.common.controller.CrudController;
import com.campuswall.common.dto.IdDTO;
import com.campuswall.common.dto.PageQuery;
import com.campuswall.common.exception.BusinessException;
import com.campuswall.common.result.PageResult;
import com.campuswall.common.result.Result;
import com.campuswall.goods.entity.SecondGoods;
import com.campuswall.goods.service.SecondGoodsService;
import com.campuswall.security.SecurityUtils;
import com.campuswall.tenant.TenantContext;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/app/goods")
public class SecondGoodsController extends CrudController<SecondGoods> {
    private final SecondGoodsService service;

    public SecondGoodsController(SecondGoodsService service) {
        this.service = service;
    }

    @Override
    protected IService<SecondGoods> service() {
        return service;
    }

    @GetMapping("/my-page")
    public Result<PageResult<SecondGoods>> myPage(@Valid PageQuery query) {
        return Result.success(PageResult.of(service.page(
                new Page<>(query.getCurrent(), query.getSize()),
                new LambdaQueryWrapper<SecondGoods>()
                        .eq(SecondGoods::getUserId, SecurityUtils.userId())
                        .like(query.getKeyword() != null && !query.getKeyword().isBlank(), SecondGoods::getTitle, query.getKeyword())
                        .orderByDesc(SecondGoods::getCreatedAt)
        )));
    }

    @PostMapping("/publish")
    public Result<Long> publish(@RequestBody SecondGoods entity) {
        entity.setTenantId(TenantContext.getTenantId());
        entity.setUserId(SecurityUtils.userId());
        entity.setStatus(0);
        service.save(entity);
        return Result.success(entity.getId());
    }

    @PutMapping("/sold")
    public Result<Void> sold(@Valid @RequestBody IdDTO dto) {
        SecondGoods entity = service.getById(dto.getId());
        if (entity == null) throw new BusinessException("商品不存在");
        if (!Objects.equals(entity.getUserId(), SecurityUtils.userId())) throw new BusinessException("只能操作自己的内容");
        entity.setStatus(2);
        service.updateById(entity);
        return Result.success();
    }

    @Override
    @PutMapping("/update")
    public Result<Void> update(@RequestBody SecondGoods entity) {
        SecondGoods old = service.getById(entity.getId());
        if (old == null) throw new BusinessException("商品不存在");
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
        SecondGoods entity = service.getById(id);
        if (entity == null) throw new BusinessException("商品不存在");
        if (!Objects.equals(entity.getUserId(), SecurityUtils.userId())) throw new BusinessException("只能删除自己的内容");
        entity.setStatus(3);
        service.updateById(entity);
        return Result.success();
    }
}
