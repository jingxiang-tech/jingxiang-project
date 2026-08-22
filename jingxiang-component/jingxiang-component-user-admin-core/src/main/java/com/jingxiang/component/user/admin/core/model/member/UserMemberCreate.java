package com.jingxiang.component.user.admin.core.model.member;

import com.jingxiang.component.user.admin.core.dict.MemberTypeEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

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
     * 成员类型
     */
    @NotNull(message = "memberType不能为空")
    private MemberTypeEnum memberType;

    /**
     * 组织内显示名称
     */
    @Size(max = 64)
    private String memberName;

    /**
     * 备注
     */
    @Size(max = 255)
    private String remark;
}
