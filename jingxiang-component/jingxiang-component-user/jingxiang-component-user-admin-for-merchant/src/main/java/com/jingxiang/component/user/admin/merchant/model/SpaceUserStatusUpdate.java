package com.jingxiang.component.user.admin.merchant.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 更新空间用户状态。
 *
 * @author chenjw
 */
@Getter
@Setter
public class SpaceUserStatusUpdate {

    /** 空间用户ID。 */
    @NotNull(message = "userId不能为空")
    private Integer userId;

    /** 状态：0禁用 1启用。 */
    @NotNull(message = "status不能为空")
    private Integer status;
}
