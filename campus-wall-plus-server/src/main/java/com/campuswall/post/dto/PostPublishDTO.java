package com.campuswall.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostPublishDTO {
    private Long id;
    private Long categoryId;
    private String title;
    @NotBlank(message = "内容不能为空")
    private String content;
    private String images;
    private String topicTags;
    private String location;
    private Integer anonymous = 0;
}
