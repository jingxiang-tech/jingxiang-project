package com.jingxiang.component.user.manager.common.model.member;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
     * 角色编码列表
     */
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
