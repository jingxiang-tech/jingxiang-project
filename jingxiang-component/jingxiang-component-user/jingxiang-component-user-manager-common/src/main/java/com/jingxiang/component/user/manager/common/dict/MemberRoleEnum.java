package com.jingxiang.component.user.manager.common.dict;

import com.baomidou.mybatisplus.annotation.EnumValue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 组织成员角色
 *
 * @author chenjw
 */
public enum MemberRoleEnum {

    OWNER("OWNER", "所有者"),
    ADMIN("ADMIN", "管理员"),
    OPERATOR("OPERATOR", "运营"),
    FINANCE("FINANCE", "财务"),
    MEMBER("MEMBER", "普通成员"),
    REVIEWER("REVIEWER", "视频审核"),
    DEV("DEV", "技术开发"),
    VIEWER("VIEWER", "只读观察");

    @EnumValue
    public final String code;
    public final String name;

    MemberRoleEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 按编码解析角色，无法识别时返回 null
     */
    public static MemberRoleEnum ofCode(String code) {
        if (code == null || code.isBlank()) {
            return null;
        }
        for (MemberRoleEnum item : values()) {
            if (item.code.equals(code) || item.name().equals(code)) {
                return item;
            }
        }
        return null;
    }

    /**
     * 角色编码列表转中文名称描述
     */
    public static String namesOf(List<String> codes) {
        if (codes == null || codes.isEmpty()) {
            return null;
        }
        return codes.stream()
                .map(MemberRoleEnum::ofCode)
                .filter(item -> item != null)
                .map(item -> item.name)
                .distinct()
                .collect(Collectors.joining("、"));
    }

    /**
     * 过滤出已定义的角色编码
     */
    public static List<String> normalizeCodes(List<String> codes) {
        if (codes == null || codes.isEmpty()) {
            return List.of();
        }
        List<String> result = new ArrayList<>();
        for (String code : codes) {
            MemberRoleEnum item = ofCode(code);
            if (item != null && !result.contains(item.code)) {
                result.add(item.code);
            }
        }
        return result;
    }

    /**
     * 角色编码列表是否包含指定角色
     */
    public static boolean contains(List<String> codes, MemberRoleEnum role) {
        return codes != null && role != null && codes.contains(role.code);
    }
}
