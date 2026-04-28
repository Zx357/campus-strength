package com.campuswall.comment.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campuswall.comment.entity.WallComment;
import com.campuswall.comment.service.WallCommentService;
import com.campuswall.common.controller.CrudController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/comment")
public class AdminCommentController extends CrudController<WallComment> {
    private final WallCommentService service;

    public AdminCommentController(WallCommentService service) {
        this.service = service;
    }

    @Override
    protected IService<WallComment> service() {
        return service;
    }
}
