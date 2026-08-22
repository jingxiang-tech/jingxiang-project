package com.jingxiang.component.user.admin.common.model.member;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 添加成员
 *
 * @author chenjw
 */
@Getter
@Setter
public class UserMemberCreate {

    /**
     * 用户ID
     */
    @NotNull(message = "userId不能为空")
    private Long userId;

    /**
     * 租户ID
     */
    @NotNull(message = "tenantId不能为空")
    private Long tenantId;

    /**
     * 角色编码列表
     */
    @NotEmpty(message = "roleCodeList不能为空")
    private List<String> roleCodeList;

    /**
     * 角色名称
     */
    @Size(max = 255)
    private String roleNameDesc;

    /**
     * 备注
     */
    @Size(max = 255)
    private String remark;
}
