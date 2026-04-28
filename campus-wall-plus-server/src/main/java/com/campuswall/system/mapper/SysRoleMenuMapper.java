package com.campuswall.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campuswall.system.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {
    @Delete("DELETE FROM sys_role_menu WHERE role_id = #{roleId}")
    void deleteByRoleIdPhysically(@Param("roleId") Long roleId);
}
