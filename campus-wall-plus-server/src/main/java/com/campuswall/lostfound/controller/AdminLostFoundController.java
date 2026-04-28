package com.campuswall.lostfound.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campuswall.common.controller.CrudController;
import com.campuswall.lostfound.entity.LostFound;
import com.campuswall.lostfound.service.LostFoundService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/lost-found")
public class AdminLostFoundController extends CrudController<LostFound> {
    private final LostFoundService service;

    public AdminLostFoundController(LostFoundService service) {
        this.service = service;
    }

    @Override
    protected IService<LostFound> service() {
        return service;
    }
}
