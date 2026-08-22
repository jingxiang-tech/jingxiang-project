package com.jingxiang.component.user.admin.merchant.model;

import java.util.List;

/**
 * 当前商户后台用户信息。
 *
 * @param userId 用户 ID
 * @param userName 用户名
 * @param avatar 头像
 * @param needUpdatePwd 是否需要修改密码
 * @param role 角色编码
 * @param initGuideDone 是否完成初始化引导
 * @param spaceId 当前空间 ID
 * @param spaceName 当前空间名称
 * @param admin 是否当前空间管理员
 */
public record SystemUserInfo(
        Integer userId,
        String userName,
        String avatar,
        Integer needUpdatePwd,
        List<String> role,
        Integer initGuideDone,
        Integer spaceId,
        String spaceName,
        Integer admin) {
}
