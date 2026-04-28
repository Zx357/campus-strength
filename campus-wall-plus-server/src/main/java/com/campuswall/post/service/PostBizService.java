package com.campuswall.post.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campuswall.common.dto.PageQuery;
import com.campuswall.common.exception.BusinessException;
import com.campuswall.common.result.PageResult;
import com.campuswall.common.service.RateLimitService;
import com.campuswall.post.dto.PostPublishDTO;
import com.campuswall.post.entity.WallCollect;
import com.campuswall.post.entity.WallLike;
import com.campuswall.post.entity.WallPost;
import com.campuswall.post.mapper.WallCollectMapper;
import com.campuswall.post.mapper.WallLikeMapper;
import com.campuswall.post.mapper.WallPostMapper;
import com.campuswall.security.SecurityUtils;
import com.campuswall.system.service.SensitiveWordCheckService;
import com.campuswall.tenant.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostBizService {
    private final WallPostMapper postMapper;
    private final WallLikeMapper likeMapper;
    private final WallCollectMapper collectMapper;
    private final SensitiveWordCheckService sensitiveWordService;
    private final RateLimitService rateLimitService;

    public PostBizService(WallPostMapper postMapper, WallLikeMapper likeMapper, WallCollectMapper collectMapper,
                          SensitiveWordCheckService sensitiveWordService, RateLimitService rateLimitService) {
        this.postMapper = postMapper;
        this.likeMapper = likeMapper;
        this.collectMapper = collectMapper;
        this.sensitiveWordService = sensitiveWordService;
        this.rateLimitService = rateLimitService;
    }

    public PageResult<WallPost> page(PageQuery query, String sort) {
        LambdaQueryWrapper<WallPost> wrapper = new LambdaQueryWrapper<WallPost>()
                .eq(WallPost::getStatus, 1)
                .like(query.getKeyword() != null && !query.getKeyword().isBlank(), WallPost::getContent, query.getKeyword());
        if ("hot".equals(sort)) wrapper.orderByDesc(WallPost::getHotScore);
        else wrapper.orderByDesc(WallPost::getCreatedAt);
        return PageResult.of(postMapper.selectPage(new Page<>(query.getCurrent(), query.getSize()), wrapper));
    }

    public PageResult<WallPost> myPage(PageQuery query) {
        Long userId = SecurityUtils.userId();
        return PageResult.of(postMapper.selectPage(
                new Page<>(query.getCurrent(), query.getSize()),
                new LambdaQueryWrapper<WallPost>()
                        .eq(WallPost::getUserId, userId)
                        .like(query.getKeyword() != null && !query.getKeyword().isBlank(), WallPost::getContent, query.getKeyword())
                        .orderByDesc(WallPost::getCreatedAt)
        ));
    }

    public PageResult<WallPost> myCollectPage(PageQuery query) {
        Long userId = SecurityUtils.userId();
        Page<WallCollect> collectPage = collectMapper.selectPage(
                new Page<>(query.getCurrent(), query.getSize()),
                new LambdaQueryWrapper<WallCollect>()
                        .eq(WallCollect::getUserId, userId)
                        .eq(WallCollect::getTargetType, "post")
                        .orderByDesc(WallCollect::getCreatedAt)
        );
        List<Long> targetIds = collectPage.getRecords().stream().map(WallCollect::getTargetId).toList();
        if (targetIds.isEmpty()) {
            return new PageResult<>(Collections.emptyList(), collectPage.getTotal(), collectPage.getCurrent(), collectPage.getSize());
        }

        Map<Long, WallPost> postMap = postMapper.selectBatchIds(targetIds).stream()
                .collect(Collectors.toMap(WallPost::getId, item -> item));
        List<WallPost> posts = targetIds.stream().map(postMap::get).filter(Objects::nonNull).toList();
        return new PageResult<>(posts, collectPage.getTotal(), collectPage.getCurrent(), collectPage.getSize());
    }

    public WallPost detail(Long id) {
        WallPost post = postMapper.selectById(id);
        if (post == null) throw new BusinessException("帖子不存在");
        post.setViewCount((post.getViewCount() == null ? 0 : post.getViewCount()) + 1);
        postMapper.updateById(post);
        return post;
    }

    public void deleteMine(Long id) {
        Long userId = SecurityUtils.userId();
        WallPost post = postMapper.selectById(id);
        if (post == null) throw new BusinessException("帖子不存在");
        if (!Objects.equals(post.getUserId(), userId)) throw new BusinessException("只能删除自己的内容");
        post.setStatus(3);
        postMapper.updateById(post);
    }

    public void updateMine(Long id, PostPublishDTO dto) {
        Long userId = SecurityUtils.userId();
        WallPost post = postMapper.selectById(id);
        if (post == null) throw new BusinessException("帖子不存在");
        if (!Objects.equals(post.getUserId(), userId)) throw new BusinessException("只能修改自己的内容");
        int status = sensitiveWordService.check(dto.getContent());
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setImages(dto.getImages());
        post.setTopicTags(dto.getTopicTags());
        post.setLocation(dto.getLocation());
        post.setAnonymous(dto.getAnonymous() == null ? 0 : dto.getAnonymous());
        post.setStatus(status);
        postMapper.updateById(post);
    }

    public Long publish(PostPublishDTO dto) {
        Long userId = SecurityUtils.userId();
        Long tenantId = TenantContext.getTenantId();
        if (userId == null || tenantId == null) throw new BusinessException("请先登录并选择学校");
        rateLimitService.check("rate:post:" + userId, 3, Duration.ofMinutes(1), "发布太频繁，请稍后再试");
        int status = sensitiveWordService.check(dto.getContent());
        WallPost post = new WallPost();
        post.setTenantId(tenantId);
        post.setUserId(userId);
        post.setCategoryId(dto.getCategoryId());
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setImages(dto.getImages());
        post.setTopicTags(dto.getTopicTags());
        post.setLocation(dto.getLocation());
        post.setAnonymous(dto.getAnonymous() == null ? 0 : dto.getAnonymous());
        post.setStatus(status);
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setCollectCount(0);
        post.setViewCount(0);
        post.setHotScore(BigDecimal.ZERO);
        postMapper.insert(post);
        return post.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean toggleLike(Long id) {
        Long userId = SecurityUtils.userId();
        Long tenantId = TenantContext.getTenantId();
        WallLike exist = likeMapper.selectOne(new LambdaQueryWrapper<WallLike>().eq(WallLike::getUserId, userId).eq(WallLike::getTargetType, "post").eq(WallLike::getTargetId, id).last("limit 1"));
        WallPost post = postMapper.selectById(id);
        if (post == null) throw new BusinessException("帖子不存在");
        if (exist != null) {
            likeMapper.deleteById(exist.getId());
            post.setLikeCount(Math.max(0, post.getLikeCount() - 1));
            postMapper.updateById(post);
            return false;
        }
        WallLike like = new WallLike();
        like.setTenantId(tenantId);
        like.setUserId(userId);
        like.setTargetType("post");
        like.setTargetId(id);
        likeMapper.insert(like);
        post.setLikeCount(post.getLikeCount() + 1);
        postMapper.updateById(post);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean toggleCollect(Long id) {
        Long userId = SecurityUtils.userId();
        Long tenantId = TenantContext.getTenantId();
        WallCollect exist = collectMapper.selectOne(new LambdaQueryWrapper<WallCollect>().eq(WallCollect::getUserId, userId).eq(WallCollect::getTargetType, "post").eq(WallCollect::getTargetId, id).last("limit 1"));
        WallPost post = postMapper.selectById(id);
        if (post == null) throw new BusinessException("帖子不存在");
        if (exist != null) {
            collectMapper.deleteById(exist.getId());
            post.setCollectCount(Math.max(0, post.getCollectCount() - 1));
            postMapper.updateById(post);
            return false;
        }
        WallCollect collect = new WallCollect();
        collect.setTenantId(tenantId);
        collect.setUserId(userId);
        collect.setTargetType("post");
        collect.setTargetId(id);
        collectMapper.insert(collect);
        post.setCollectCount(post.getCollectCount() + 1);
        postMapper.updateById(post);
        return true;
    }
}
