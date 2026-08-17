package com.jingxiang.component.user.model.user;

import com.jingxiang.commons.model.dto.BasePage;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户分页查询
 *
 * @author chenjw
 */
@Getter
@Setter
public class UserQuery extends BasePage {

    /**
     * 手机号
     */
    private String tel;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 是否禁用
     */
    private Integer forbidden;
}
