package com.campuswall.post.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campuswall.common.controller.CrudController;
import com.campuswall.post.entity.WallPost;
import com.campuswall.post.service.WallPostService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/post")
public class AdminPostController extends CrudController<WallPost> {
    private final WallPostService service;

    public AdminPostController(WallPostService service) {
        this.service = service;
    }

    @Override
    protected IService<WallPost> service() {
        return service;
    }
}
