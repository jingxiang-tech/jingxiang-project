package com.jingxiang.component.user.admin.merchant.model;

import java.util.List;

/**
 * 商户后台登录结果。
 *
 * @param userToken 用户令牌
 * @param needChooseSpace 是否需要选择空间
 * @param spaceList 可选空间
 */
public record SystemUserLoginDto(
        String userToken,
        Integer needChooseSpace,
        List<SpaceOption> spaceList) {
}
