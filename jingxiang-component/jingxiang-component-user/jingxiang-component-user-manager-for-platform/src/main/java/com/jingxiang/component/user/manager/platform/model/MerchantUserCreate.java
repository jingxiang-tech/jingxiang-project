package com.jingxiang.component.user.manager.platform.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 新增商户用户参数。
 */
@Getter
@Setter
public class MerchantUserCreate {

    /** 商户号 */
    @NotBlank(message = "请选择商户")
    @Size(max = 64)
    private String mctNo;

    /** 账户名 */
    @NotBlank(message = "账户名不能为空")
    @Size(max = 64)
    private String userName;

    /** 密码 */
    @Size(max = 128)
    private String pwd;

    /** 真实姓名 */
    @Size(max = 64)
    private String realName;

    /** 手机号 */
    @Size(max = 32)
    private String tel;

    /** 备注 */
    @Size(max = 512)
    private String remark;

    /** 可访问的业务空间 ID */
    private List<Integer> spaceIds;
}
