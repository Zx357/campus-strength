package com.campuswall.comment.controller;

import com.campuswall.comment.dto.CommentCreateDTO;
import com.campuswall.comment.entity.WallComment;
import com.campuswall.comment.service.CommentBizService;
import com.campuswall.common.dto.PageQuery;
import com.campuswall.common.result.PageResult;
import com.campuswall.common.result.Result;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/app/comment")
public class AppCommentController {
    private final CommentBizService commentBizService;

    public AppCommentController(CommentBizService commentBizService) {
        this.commentBizService = commentBizService;
    }

    @GetMapping("/page")
    public Result<PageResult<WallComment>> page(@Valid PageQuery query, @RequestParam Long postId) {
        return Result.success(commentBizService.page(query, postId));
    }

    @GetMapping("/my-page")
    public Result<PageResult<WallComment>> myPage(@Valid PageQuery query) {
        return Result.success(commentBizService.myPage(query));
    }

    @PostMapping("/create")
    public Result<Long> create(@Valid @RequestBody CommentCreateDTO dto) {
        return Result.success(commentBizService.create(dto));
    }

    @DeleteMapping("/delete")
    public Result<Void> delete(@RequestParam Long id) {
        commentBizService.deleteMine(id);
        return Result.success();
    }
}
