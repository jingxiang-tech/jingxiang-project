package com.jingxiang.component.user.manager.common.dict;

import com.baomidou.mybatisplus.annotation.EnumValue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 组织角色编码
 *
 * @author chenjw
 */
public enum MemberTypeEnum {

    OWNER("OWNER", "所有者"),
    ADMIN("ADMIN", "管理员"),
    OPERATOR("OPERATOR", "运营"),
    FINANCE("FINANCE", "财务"),
    MEMBER("MEMBER", "普通成员");

    @EnumValue
    public final String code;
    public final String name;

    MemberTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 按编码解析角色，无法识别时返回 null
     */
    public static MemberTypeEnum ofCode(String code) {
        if (code == null || code.isBlank()) {
            return null;
        }
        for (MemberTypeEnum item : values()) {
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
                .map(MemberTypeEnum::ofCode)
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
            MemberTypeEnum item = ofCode(code);
            if (item != null && !result.contains(item.code)) {
                result.add(item.code);
            }
        }
        return result;
    }

    /**
     * 角色编码列表是否包含指定角色
     */
    public static boolean contains(List<String> codes, MemberTypeEnum type) {
        return codes != null && type != null && codes.contains(type.code);
    }
}
