package com.campuswall.comment.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campuswall.comment.dto.CommentCreateDTO;
import com.campuswall.comment.entity.WallComment;
import com.campuswall.comment.mapper.WallCommentMapper;
import com.campuswall.common.dto.PageQuery;
import com.campuswall.common.exception.BusinessException;
import com.campuswall.common.result.PageResult;
import com.campuswall.common.service.RateLimitService;
import com.campuswall.post.entity.WallPost;
import com.campuswall.post.mapper.WallPostMapper;
import com.campuswall.security.SecurityUtils;
import com.campuswall.system.service.SensitiveWordCheckService;
import com.campuswall.tenant.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Objects;

@Service
public class CommentBizService {
    private final WallCommentMapper commentMapper;
    private final WallPostMapper postMapper;
    private final RateLimitService rateLimitService;
    private final SensitiveWordCheckService sensitiveWordService;

    public CommentBizService(WallCommentMapper commentMapper, WallPostMapper postMapper, RateLimitService rateLimitService, SensitiveWordCheckService sensitiveWordService) {
        this.commentMapper = commentMapper;
        this.postMapper = postMapper;
        this.rateLimitService = rateLimitService;
        this.sensitiveWordService = sensitiveWordService;
    }

    public PageResult<WallComment> page(PageQuery query, Long postId) {
        return PageResult.of(commentMapper.selectPage(new Page<>(query.getCurrent(), query.getSize()),
                new LambdaQueryWrapper<WallComment>().eq(WallComment::getPostId, postId).eq(WallComment::getStatus, 1).orderByAsc(WallComment::getCreatedAt)));
    }

    public PageResult<WallComment> myPage(PageQuery query) {
        Long userId = SecurityUtils.userId();
        return PageResult.of(commentMapper.selectPage(new Page<>(query.getCurrent(), query.getSize()),
                new LambdaQueryWrapper<WallComment>()
                        .eq(WallComment::getUserId, userId)
                        .like(query.getKeyword() != null && !query.getKeyword().isBlank(), WallComment::getContent, query.getKeyword())
                        .orderByDesc(WallComment::getCreatedAt)));
    }

    @Transactional(rollbackFor = Exception.class)
    public Long create(CommentCreateDTO dto) {
        Long userId = SecurityUtils.userId();
        rateLimitService.check("rate:comment:" + userId, 10, Duration.ofMinutes(1), "评论太频繁，请稍后再试");
        int status = sensitiveWordService.check(dto.getContent());
        WallPost post = postMapper.selectById(dto.getPostId());
        if (post == null) throw new BusinessException("帖子不存在");
        WallComment comment = new WallComment();
        comment.setTenantId(TenantContext.getTenantId());
        comment.setPostId(dto.getPostId());
        comment.setUserId(userId);
        comment.setParentId(dto.getParentId());
        comment.setReplyUserId(dto.getReplyUserId());
        comment.setContent(dto.getContent());
        comment.setStatus(status == 1 ? 1 : 0);
        comment.setLikeCount(0);
        commentMapper.insert(comment);
        post.setCommentCount(post.getCommentCount() + 1);
        postMapper.updateById(post);
        return comment.getId();
    }

    public void deleteMine(Long id) {
        Long userId = SecurityUtils.userId();
        WallComment comment = commentMapper.selectById(id);
        if (comment == null) throw new BusinessException("评论不存在");
        if (!Objects.equals(comment.getUserId(), userId)) throw new BusinessException("只能删除自己的评论");
        comment.setStatus(3);
        commentMapper.updateById(comment);

        WallPost post = postMapper.selectById(comment.getPostId());
        if (post != null && post.getCommentCount() != null) {
            post.setCommentCount(Math.max(0, post.getCommentCount() - 1));
            postMapper.updateById(post);
        }
    }
}
