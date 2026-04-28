package com.campuswall.help.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campuswall.common.controller.CrudController;
import com.campuswall.help.entity.CampusHelp;
import com.campuswall.help.service.CampusHelpService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/help")
public class CampusHelpController extends CrudController<CampusHelp> {
    private final CampusHelpService service;

    public CampusHelpController(CampusHelpService service) {
        this.service = service;
    }

    @Override
    protected IService<CampusHelp> service() {
        return service;
    }
}
