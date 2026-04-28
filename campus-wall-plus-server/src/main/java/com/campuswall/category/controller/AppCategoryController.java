package com.campuswall.category.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campuswall.category.entity.BizCategory;
import com.campuswall.category.mapper.BizCategoryMapper;
import com.campuswall.common.result.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/app/category")
public class AppCategoryController {
    private final BizCategoryMapper mapper;
    public AppCategoryController(BizCategoryMapper mapper) { this.mapper = mapper; }
    @GetMapping("/list")
    public Result<List<BizCategory>> list(@RequestParam String moduleCode) {
        return Result.success(mapper.selectList(new LambdaQueryWrapper<BizCategory>().eq(BizCategory::getModuleCode, moduleCode).eq(BizCategory::getStatus, 1).orderByAsc(BizCategory::getSort)));
    }
}
