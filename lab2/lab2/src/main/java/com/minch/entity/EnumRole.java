package com.minch.entity;

public enum EnumRole {

    ROLE_USER,
    ROLE_ADMIN;

    public String getRoleName() {
        return name(); // 或自定义逻辑获取角色名称
    }
}
