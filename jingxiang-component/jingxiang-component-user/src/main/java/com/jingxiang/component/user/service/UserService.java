package com.jingxiang.component.user.service;

import com.jingxiang.commons.model.dto.R;
import com.jingxiang.component.user.model.user.*;

import java.util.List;

/**
 * 统一用户基础服务（登录/注册/个人信息，不含管理端列表与禁用删除）
 *
 * @author chenjw
 */
public interface UserService {

    /**
     * 注册/创建用户
     */
    R<Long> create(UserCreate create);

    /**
     * 修改个人资料（不含密码/禁用/删除）
     */
    R<?> update(UserUpdate update);

    /**
     * 用户详情
     */
    R<UserDetail> detail(Long userId);

    /**
     * 修改密码
     */
    R<?> changePassword(UserPasswordUpdate update);

    /**
     * 按用户ID查询
     */
    UserPo getById(Long userId);

    /**
     * 按用户ID批量查询
     */
    List<UserBrief> listByIds(List<Long> userIds);

    /**
     * 按手机号查询
     */
    UserBrief getByTel(String tel);

    /**
     * 按用户名查询
     */
    UserBrief getByUserName(String userName);

    /**
     * 账号密码登录校验（用户名或手机号 + BCrypt）
     *
     * @return 校验通过的用户，失败返回 null
     */
    UserPo authenticate(String account, String rawPassword);
}
