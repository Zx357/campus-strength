package com.campuswall.post.controller;

import com.campuswall.common.dto.IdDTO;
import com.campuswall.common.dto.PageQuery;
import com.campuswall.common.result.PageResult;
import com.campuswall.common.result.Result;
import com.campuswall.post.dto.PostPublishDTO;
import com.campuswall.post.entity.WallPost;
import com.campuswall.post.service.PostBizService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/app/post")
public class AppPostController {
    private final PostBizService postBizService;

    public AppPostController(PostBizService postBizService) {
        this.postBizService = postBizService;
    }

    @GetMapping("/page")
    public Result<PageResult<WallPost>> page(@Valid PageQuery query, String sort) {
        return Result.success(postBizService.page(query, sort));
    }

    @GetMapping("/my-page")
    public Result<PageResult<WallPost>> myPage(@Valid PageQuery query) {
        return Result.success(postBizService.myPage(query));
    }

    @GetMapping("/my-collect-page")
    public Result<PageResult<WallPost>> myCollectPage(@Valid PageQuery query) {
        return Result.success(postBizService.myCollectPage(query));
    }

    @GetMapping("/detail")
    public Result<WallPost> detail(@RequestParam Long id) {
        return Result.success(postBizService.detail(id));
    }

    @PostMapping("/publish")
    public Result<Long> publish(@Valid @RequestBody PostPublishDTO dto) {
        return Result.success(postBizService.publish(dto));
    }

    @PutMapping("/update")
    public Result<Void> update(@Valid @RequestBody PostPublishDTO dto) {
        postBizService.updateMine(dto.getId(), dto);
        return Result.success();
    }

    @PostMapping("/like")
    public Result<Boolean> like(@Valid @RequestBody IdDTO dto) {
        return Result.success(postBizService.toggleLike(dto.getId()));
    }

    @PostMapping("/collect")
    public Result<Boolean> collect(@Valid @RequestBody IdDTO dto) {
        return Result.success(postBizService.toggleCollect(dto.getId()));
    }

    @DeleteMapping("/delete")
    public Result<Void> delete(@RequestParam Long id) {
        postBizService.deleteMine(id);
        return Result.success();
    }
}
