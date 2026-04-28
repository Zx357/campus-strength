package com.campuswall.auth.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campuswall.auth.dto.LoginDTO;
import com.campuswall.auth.dto.WxLoginDTO;
import com.campuswall.auth.vo.LoginVO;
import com.campuswall.common.constant.RoleCodes;
import com.campuswall.common.exception.BusinessException;
import com.campuswall.system.entity.SysTenant;
import com.campuswall.system.mapper.SysTenantMapper;
import com.campuswall.user.entity.SysUser;
import com.campuswall.user.mapper.SysUserMapper;
import com.campuswall.tenant.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {
    private final SysUserMapper userMapper;
    private final SysTenantMapper tenantMapper;

    public AuthService(SysUserMapper userMapper, SysTenantMapper tenantMapper) {
        this.userMapper = userMapper;
        this.tenantMapper = tenantMapper;
    }

    public LoginVO adminLogin(LoginDTO dto) {
        TenantContext.setIgnore(true);
        SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, dto.getUsername()).last("limit 1"));
        TenantContext.setIgnore(false);
        if (user == null || user.getStatus() == 0 || !matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        if (RoleCodes.STUDENT.equals(user.getRoleCode()) || RoleCodes.GUEST.equals(user.getRoleCode())) {
            throw new BusinessException(403, "当前账号不能登录后台");
        }
        user.setLastLoginTime(LocalDateTime.now());
        userMapper.updateById(user);
        StpUtil.login(user.getId());
        fillSession(user);
        return buildLoginVO(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public LoginVO wxLogin(WxLoginDTO dto) {
        SysTenant tenant = findTenant(dto.getTenantCode());
        TenantContext.setTenantId(tenant.getId());
        String mockOpenId = "mock_" + (dto.getCode() == null || dto.getCode().isBlank() ? UUID.randomUUID() : dto.getCode());
        SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getOpenid, mockOpenId).last("limit 1"));
        if (user == null) {
            user = new SysUser();
            user.setTenantId(tenant.getId());
            user.setUsername("wx_" + UUID.randomUUID().toString().substring(0, 8));
            user.setPassword("");
            user.setOpenid(mockOpenId);
            user.setNickname(dto.getNickname() == null ? "校园同学" : dto.getNickname());
            user.setAvatarUrl(dto.getAvatarUrl());
            user.setRoleCode(RoleCodes.STUDENT);
            user.setAuthStatus(0);
            user.setStatus(1);
            userMapper.insert(user);
        }
        StpUtil.login(user.getId());
        fillSession(user);
        return buildLoginVO(user);
    }

    public LoginVO guest(String tenantCode) {
        SysTenant tenant = findTenant(tenantCode);
        SysUser guest = new SysUser();
        guest.setId(-System.currentTimeMillis());
        guest.setTenantId(tenant.getId());
        guest.setUsername("guest");
        guest.setNickname("游客");
        guest.setRoleCode(RoleCodes.GUEST);
        StpUtil.login(guest.getId());
        fillSession(guest);
        return buildLoginVO(guest);
    }

    public SysUser profile() {
        Long id = StpUtil.getLoginIdAsLong();
        if (id < 0) {
            SysUser user = new SysUser();
            user.setId(id);
            user.setNickname("游客");
            user.setRoleCode(RoleCodes.GUEST);
            user.setTenantId((Long) StpUtil.getSession().get("tenantId"));
            return user;
        }
        return userMapper.selectById(id);
    }

    private void fillSession(SysUser user) {
        if (user.getTenantId() != null) {
            StpUtil.getSession().set("tenantId", user.getTenantId());
        }
        StpUtil.getSession().set("roleCode", user.getRoleCode());
        StpUtil.getSession().set("username", user.getUsername());
    }

    private LoginVO buildLoginVO(SysUser user) {
        return LoginVO.builder()
                .token(StpUtil.getTokenValue())
                .userId(user.getId())
                .tenantId(user.getTenantId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .roleCode(user.getRoleCode())
                .build();
    }

    private SysTenant findTenant(String tenantCode) {
        TenantContext.setIgnore(true);
        SysTenant tenant = tenantMapper.selectOne(new LambdaQueryWrapper<SysTenant>().eq(SysTenant::getTenantCode, tenantCode).eq(SysTenant::getStatus, 1).last("limit 1"));
        TenantContext.setIgnore(false);
        if (tenant == null) {
            throw new BusinessException("学校不存在或已停用");
        }
        return tenant;
    }

    private boolean matches(String raw, String encoded) {
        if (encoded == null) return false;
        if (encoded.startsWith("$2")) return BCrypt.checkpw(raw, encoded);
        return raw.equals(encoded);
    }
}
