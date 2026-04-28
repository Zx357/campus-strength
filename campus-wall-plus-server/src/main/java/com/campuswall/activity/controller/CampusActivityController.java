package com.campuswall.activity.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.campuswall.activity.entity.CampusActivitySignup;
import com.campuswall.activity.mapper.CampusActivitySignupMapper;
import com.campuswall.common.controller.CrudController;
import com.campuswall.common.dto.PageQuery;
import com.campuswall.common.result.PageResult;
import com.campuswall.common.result.Result;
import com.campuswall.activity.entity.CampusActivity;
import com.campuswall.activity.service.CampusActivityService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/activity")
public class CampusActivityController extends CrudController<CampusActivity> {
    private final CampusActivityService service;
    private final CampusActivitySignupMapper signupMapper;

    public CampusActivityController(CampusActivityService service, CampusActivitySignupMapper signupMapper) {
        this.service = service;
        this.signupMapper = signupMapper;
    }

    @Override
    protected IService<CampusActivity> service() {
        return service;
    }

    @GetMapping("/signup-page")
    public Result<PageResult<CampusActivitySignup>> signupPage(@Valid PageQuery query, @RequestParam(required = false) Long activityId) {
        return Result.success(PageResult.of(signupMapper.selectPage(
                new Page<>(query.getCurrent(), query.getSize()),
                new LambdaQueryWrapper<CampusActivitySignup>()
                        .eq(activityId != null, CampusActivitySignup::getActivityId, activityId)
                        .orderByDesc(CampusActivitySignup::getCreatedAt)
        )));
    }
}
