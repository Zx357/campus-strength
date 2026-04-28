package com.campuswall.file.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("file_record")
public class FileRecord extends com.campuswall.common.model.TenantEntity {
    private Long userId;
    private String fileName;
    private String originalName;
    private String fileUrl;
    private String fileType;
    private Long fileSize;
    private String storageType;
}
