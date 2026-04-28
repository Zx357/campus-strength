package com.campuswall.file.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.campuswall.common.exception.BusinessException;
import com.campuswall.common.result.Result;
import com.campuswall.file.entity.FileRecord;
import com.campuswall.file.mapper.FileRecordMapper;
import com.campuswall.security.SecurityUtils;
import com.campuswall.tenant.TenantContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/common/file")
public class FileController {
    private static final Set<String> ALLOW = Set.of("jpg", "jpeg", "png", "webp");
    private final FileRecordMapper mapper;
    @Value("${campus-wall.upload.local-dir:uploads}") private String localDir;
    @Value("${campus-wall.upload.url-prefix:/uploads/}") private String urlPrefix;
    @Value("${campus-wall.upload.max-size:5242880}") private long maxSize;

    public FileController(FileRecordMapper mapper) { this.mapper = mapper; }

    @PostMapping("/upload")
    public Result<Map<String, String>> upload(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) throw new BusinessException("文件不能为空");
        if (file.getSize() > maxSize) throw new BusinessException("单张图片不能超过5MB");
        String ext = FileUtil.extName(file.getOriginalFilename()).toLowerCase();
        if (!ALLOW.contains(ext)) throw new BusinessException("仅支持 jpg/png/jpeg/webp 格式");
        String name = IdUtil.fastSimpleUUID() + "." + ext;
        File dir = new File(localDir);
        if (!dir.exists()) dir.mkdirs();
        file.transferTo(new File(dir, name));
        String url = urlPrefix + name;
        FileRecord record = new FileRecord();
        record.setTenantId(TenantContext.getTenantId());
        record.setUserId(SecurityUtils.userId());
        record.setFileName(name);
        record.setOriginalName(file.getOriginalFilename());
        record.setFileUrl(url);
        record.setFileType(ext);
        record.setFileSize(file.getSize());
        record.setStorageType("local");
        mapper.insert(record);
        return Result.success(Map.of("fileUrl", url, "url", url));
    }
}
