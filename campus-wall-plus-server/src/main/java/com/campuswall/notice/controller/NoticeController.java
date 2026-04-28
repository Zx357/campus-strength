package com.campuswall.notice.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campuswall.common.controller.CrudController;
import com.campuswall.notice.entity.Notice;
import com.campuswall.notice.service.NoticeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/notice")
public class NoticeController extends CrudController<Notice> {
    private final NoticeService service;

    public NoticeController(NoticeService service) {
        this.service = service;
    }

    @Override
    protected IService<Notice> service() {
        return service;
    }
}
