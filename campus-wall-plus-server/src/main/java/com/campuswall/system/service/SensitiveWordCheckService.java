package com.campuswall.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campuswall.common.exception.BusinessException;
import com.campuswall.security.SecurityUtils;
import com.campuswall.system.entity.SensitiveWord;
import com.campuswall.system.mapper.SensitiveWordMapper;
import com.campuswall.tenant.TenantContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensitiveWordCheckService {
    private final SensitiveWordMapper mapper;

    public SensitiveWordCheckService(SensitiveWordMapper mapper) {
        this.mapper = mapper;
    }

    public int check(String text) {
        if (text == null || text.isBlank()) {
            return 1;
        }
        TenantContext.setIgnore(true);
        Long tenantId = SecurityUtils.tenantId();
        List<SensitiveWord> words = mapper.selectList(new LambdaQueryWrapper<SensitiveWord>()
                .eq(SensitiveWord::getStatus, 1)
                .and(w -> w.isNull(SensitiveWord::getTenantId).or(tenantId != null, x -> x.eq(SensitiveWord::getTenantId, tenantId))));
        TenantContext.setIgnore(false);
        int result = 1;
        for (SensitiveWord word : words) {
            if (word.getWord() != null && text.contains(word.getWord())) {
                if (word.getLevel() == 2) {
                    throw new BusinessException("内容包含敏感词：" + word.getWord());
                }
                if (word.getLevel() == 3) {
                    result = 0;
                }
            }
        }
        return result;
    }
}
