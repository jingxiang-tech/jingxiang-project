package com.jingxiang.component.user.manager.common.model.member;

import com.jingxiang.commons.model.dto.BasePage;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 成员分页查询
 *
 * @author chenjw
 */
@Getter
@Setter
public class UserMemberQuery extends BasePage {

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 用户ID
     */
    private Long memberUserId;

    /**
     * 用户ID列表
     */
    private List<Long> userIds;

    /**
     * 角色编码（JSON 包含匹配）
     */
    private String roleCode;

    /**
     * 是否禁用
     */
    private Integer forbidden;

    /**
     * 关键词（用户名/手机/昵称/角色名称）
     */
    private String keyword;
}
