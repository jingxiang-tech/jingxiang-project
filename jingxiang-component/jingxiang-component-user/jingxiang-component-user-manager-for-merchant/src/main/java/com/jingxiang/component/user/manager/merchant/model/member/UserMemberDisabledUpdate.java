package com.jingxiang.component.user.manager.merchant.model.member;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 组织成员禁用状态更新参数
 *
 * @author chenjw
 */
@Getter
@Setter
public class UserMemberDisabledUpdate {

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Integer userId;

    /**
     * 是否禁用
     */
    @NotNull(message = "禁用状态不能为空")
    @Min(value = 0, message = "禁用状态仅支持0或1")
    @Max(value = 1, message = "禁用状态仅支持0或1")
    private Integer disabled;
}
