package com.campuswall.system.controller;

import com.campuswall.common.result.Result;
import com.campuswall.system.entity.SysRoleMenu;
import com.campuswall.system.mapper.SysRoleMenuMapper;
import com.campuswall.system.service.SysRoleMenuService;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/role-menu")
public class RoleMenuController {
    private final SysRoleMenuService service;
    private final SysRoleMenuMapper mapper;

    public RoleMenuController(SysRoleMenuService service, SysRoleMenuMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/{roleId}/menus")
    public Result<List<Long>> menus(@PathVariable Long roleId) {
        List<Long> menuIds = mapper.selectList(com.baomidou.mybatisplus.core.toolkit.Wrappers.<SysRoleMenu>lambdaQuery()
                .eq(SysRoleMenu::getRoleId, roleId))
            .stream()
            .map(SysRoleMenu::getMenuId)
            .toList();

        return Result.success(menuIds);
    }

    @PutMapping("/{roleId}/menus")
    public Result<Void> updateMenus(@PathVariable Long roleId, @RequestBody RoleMenuDTO dto) {
        mapper.deleteByRoleIdPhysically(roleId);

        if (dto.getMenuIds() != null && !dto.getMenuIds().isEmpty()) {
            List<SysRoleMenu> relations = dto.getMenuIds().stream()
                .distinct()
                .map(menuId -> {
                    SysRoleMenu relation = new SysRoleMenu();
                    relation.setRoleId(roleId);
                    relation.setMenuId(menuId);
                    return relation;
                })
                .toList();
            service.saveBatch(relations);
        }

        return Result.success();
    }

    @Data
    public static class RoleMenuDTO {
        private List<Long> menuIds;
    }
}
