package com.jingxiang.component.user.admin.core.model.member;

import com.jingxiang.component.user.admin.core.dict.MemberTypeEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 修改成员
 *
 * @author chenjw
 */
@Getter
@Setter
public class UserMemberUpdate {

    /**
     * 成员ID
     */
    @NotNull(message = "memberId不能为空")
    private Long memberId;

    /**
     * 成员类型
     */
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
