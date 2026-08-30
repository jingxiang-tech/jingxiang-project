package com.jingxiang.component.user.manager.merchant.model.member;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 组织成员角色更新参数
 *
 * @author chenjw
 */
@Getter
@Setter
public class UserMemberRolesUpdate {

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Integer userId;

    /**
     * 角色编码列表
     */
    @NotEmpty(message = "角色不能为空")
    private List<String> roleCodeList;
}
