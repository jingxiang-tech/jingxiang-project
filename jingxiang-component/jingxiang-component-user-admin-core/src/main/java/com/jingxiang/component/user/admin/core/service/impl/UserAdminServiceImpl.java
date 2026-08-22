package com.jingxiang.component.user.admin.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.jingxiang.commons.exception.InbyteException;
import com.jingxiang.commons.model.dict.WhetherDict;
import com.jingxiang.commons.model.dto.Page;
import com.jingxiang.commons.model.dto.R;
import com.jingxiang.commons.util.PageUtil;
import com.jingxiang.component.user.admin.core.service.UserAdminService;
import com.jingxiang.component.user.dao.UserMapper;
import com.jingxiang.component.user.model.user.UserBrief;
import com.jingxiang.component.user.model.user.UserPo;
import com.jingxiang.component.user.model.user.UserQuery;
import com.jingxiang.component.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 用户管理端服务实现
 *
 * @author chenjw
 */
@Service
public class UserAdminServiceImpl implements UserAdminService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Override
    public R<Page<UserBrief>> list(UserQuery query) {
        PageUtil.startPage(query);
        return R.page(userMapper.list(query));
    }

    @Override
    public R<?> delete(Long userId) {
        requireExists(userId);
        userMapper.update(null, new LambdaUpdateWrapper<UserPo>()
                .eq(UserPo::getUserId, userId)
                .set(UserPo::getDeleted, WhetherDict.Yes.code)
                .set(UserPo::getUpdatedAt, LocalDateTime.now()));
        return R.ok("删除成功");
    }

    @Override
    public R<?> setForbidden(Long userId, boolean forbidden) {
        requireExists(userId);
        userMapper.update(null, new LambdaUpdateWrapper<UserPo>()
                .eq(UserPo::getUserId, userId)
                .set(UserPo::getForbidden, forbidden ? WhetherDict.Yes.code : WhetherDict.No.code)
                .set(UserPo::getUpdatedAt, LocalDateTime.now()));
        return R.ok(forbidden ? "已禁用" : "已启用");
    }

    private void requireExists(Long userId) {
        if (userService.getById(userId) == null) {
            throw InbyteException.fail("用户不存在");
        }
    }
}
