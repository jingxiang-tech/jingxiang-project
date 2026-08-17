package com.jingxiang.component.user.service;

import com.jingxiang.commons.model.dto.R;
import com.jingxiang.component.user.model.identity.BindMobileParam;
import com.jingxiang.component.user.model.identity.IdentityLoginParam;
import com.jingxiang.component.user.model.user.UserDetail;

/**
 * 认证相关：第三方登录注册、手机号绑定
 *
 * @author chenjw
 */
public interface UserAuthService {

    /**
     * 第三方登录或首次自动注册
     */
    R<UserDetail> loginOrRegisterByIdentity(IdentityLoginParam param);

    /**
     * 绑定手机号（禁止静默合并账号）
     */
    R<?> bindMobile(BindMobileParam param);
}
