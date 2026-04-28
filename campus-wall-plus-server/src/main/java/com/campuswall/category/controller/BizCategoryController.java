package com.campuswall.category.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campuswall.common.controller.CrudController;
import com.campuswall.category.entity.BizCategory;
import com.campuswall.category.service.BizCategoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/category")
public class BizCategoryController extends CrudController<BizCategory> {
    private final BizCategoryService service;

    public BizCategoryController(BizCategoryService service) {
        this.service = service;
    }

    @Override
    protected IService<BizCategory> service() {
        return service;
    }
}
