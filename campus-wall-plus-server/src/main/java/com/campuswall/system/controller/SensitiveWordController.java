package com.campuswall.system.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campuswall.common.controller.CrudController;
import com.campuswall.system.entity.SensitiveWord;
import com.campuswall.system.service.SensitiveWordService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/sensitive-word")
public class SensitiveWordController extends CrudController<SensitiveWord> {
    private final SensitiveWordService service;

    public SensitiveWordController(SensitiveWordService service) {
        this.service = service;
    }

    @Override
    protected IService<SensitiveWord> service() {
        return service;
    }
}
