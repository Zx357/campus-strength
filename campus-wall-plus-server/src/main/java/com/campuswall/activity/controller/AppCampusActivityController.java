package com.campuswall.activity.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campuswall.activity.entity.CampusActivity;
import com.campuswall.activity.entity.CampusActivitySignup;
import com.campuswall.activity.mapper.CampusActivityMapper;
import com.campuswall.activity.mapper.CampusActivitySignupMapper;
import com.campuswall.activity.service.CampusActivityService;
import com.campuswall.common.dto.IdDTO;
import com.campuswall.common.dto.PageQuery;
import com.campuswall.common.exception.BusinessException;
import com.campuswall.common.result.PageResult;
import com.campuswall.common.result.Result;
import com.campuswall.security.SecurityUtils;
import com.campuswall.tenant.TenantContext;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/app/activity")
public class AppCampusActivityController {
    private final CampusActivityService service;
    private final CampusActivityMapper activityMapper;
    private final CampusActivitySignupMapper signupMapper;

    public AppCampusActivityController(CampusActivityService service, CampusActivityMapper activityMapper, CampusActivitySignupMapper signupMapper) {
        this.service = service;
        this.activityMapper = activityMapper;
        this.signupMapper = signupMapper;
    }

    @GetMapping("/page")
    public Result<PageResult<CampusActivity>> page(PageQuery query) {
        return Result.success(PageResult.of(service.page(
                new Page<>(query.getCurrent(), query.getSize()),
                new LambdaQueryWrapper<CampusActivity>()
                        .eq(CampusActivity::getStatus, 1)
                        .like(query.getKeyword() != null && !query.getKeyword().isBlank(), CampusActivity::getTitle, query.getKeyword())
                        .orderByDesc(CampusActivity::getActivityTime)
        )));
    }

    @GetMapping("/detail")
    public Result<CampusActivity> detail(@RequestParam Long id) {
        return Result.success(service.getById(id));
    }

    @GetMapping("/my-signup-page")
    public Result<PageResult<CampusActivity>> mySignupPage(@Valid PageQuery query) {
        Long userId = SecurityUtils.userId();
        Page<CampusActivitySignup> signupPage = signupMapper.selectPage(
                new Page<>(query.getCurrent(), query.getSize()),
                new LambdaQueryWrapper<CampusActivitySignup>()
                        .eq(CampusActivitySignup::getUserId, userId)
                        .eq(CampusActivitySignup::getStatus, 1)
                        .orderByDesc(CampusActivitySignup::getCreatedAt)
        );
        List<Long> activityIds = signupPage.getRecords().stream().map(CampusActivitySignup::getActivityId).toList();
        if (activityIds.isEmpty()) {
            return Result.success(new PageResult<>(Collections.emptyList(), signupPage.getTotal(), signupPage.getCurrent(), signupPage.getSize()));
        }
        Map<Long, CampusActivity> activityMap = activityMapper.selectBatchIds(activityIds).stream()
                .collect(Collectors.toMap(CampusActivity::getId, item -> item));
        List<CampusActivity> records = activityIds.stream().map(activityMap::get).filter(Objects::nonNull).toList();
        return Result.success(new PageResult<>(records, signupPage.getTotal(), signupPage.getCurrent(), signupPage.getSize()));
    }

    @PostMapping("/signup")
    public Result<Void> signup(@Valid @RequestBody IdDTO dto) {
        Long userId = SecurityUtils.userId();
        CampusActivity activity = service.getById(dto.getId());
        if (activity == null) throw new BusinessException("活动不存在");
        if (!Objects.equals(activity.getStatus(), 1)) throw new BusinessException("活动暂不可报名");

        CampusActivitySignup exist = signupMapper.selectOne(new LambdaQueryWrapper<CampusActivitySignup>()
                .eq(CampusActivitySignup::getUserId, userId)
                .eq(CampusActivitySignup::getActivityId, dto.getId())
                .last("limit 1"));
        if (exist != null && Objects.equals(exist.getStatus(), 1)) {
            return Result.success();
        }
        if (exist != null) {
            exist.setStatus(1);
            signupMapper.updateById(exist);
            return Result.success();
        }

        CampusActivitySignup signup = new CampusActivitySignup();
        signup.setTenantId(TenantContext.getTenantId());
        signup.setUserId(userId);
        signup.setActivityId(dto.getId());
        signup.setStatus(1);
        signupMapper.insert(signup);
        return Result.success();
    }

    @PutMapping("/cancel-signup")
    public Result<Void> cancelSignup(@Valid @RequestBody IdDTO dto) {
        Long userId = SecurityUtils.userId();
        CampusActivitySignup exist = signupMapper.selectOne(new LambdaQueryWrapper<CampusActivitySignup>()
                .eq(CampusActivitySignup::getUserId, userId)
                .eq(CampusActivitySignup::getActivityId, dto.getId())
                .eq(CampusActivitySignup::getStatus, 1)
                .last("limit 1"));
        if (exist != null) {
            exist.setStatus(0);
            signupMapper.updateById(exist);
        }
        return Result.success();
    }
}
