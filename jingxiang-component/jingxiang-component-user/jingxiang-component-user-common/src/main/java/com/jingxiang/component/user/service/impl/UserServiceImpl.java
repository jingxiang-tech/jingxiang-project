package com.jingxiang.component.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.jingxiang.commons.exception.InbyteException;
import com.jingxiang.commons.model.dict.WhetherDict;
import com.jingxiang.commons.model.dto.R;
import com.jingxiang.component.user.dao.UserMapper;
import com.jingxiang.component.user.dict.GenderEnum;
import com.jingxiang.component.user.model.user.*;
import com.jingxiang.component.user.service.UserService;
import com.jingxiang.component.user.util.PasswordUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 统一用户基础服务实现
 *
 * @author chenjw
 */
@Service("unifiedUserService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public R<Long> create(UserCreate create) {
        assertUnique(create.getTel(), create.getUserName(), create.getEmail(), null);

        LocalDateTime now = LocalDateTime.now();
        UserPo po = UserPo.builder()
                .tel(blankToNull(create.getTel()))
                .userName(blankToNull(create.getUserName()))
                .nickname(create.getNickname())
                .realName(create.getRealName())
                .gender(create.getGender() != null ? create.getGender() : GenderEnum.UNKNOWN)
                .avatar(create.getAvatar())
                .email(blankToNull(create.getEmail()))
                .forbidden(WhetherDict.No.code)
                .deleted(WhetherDict.No.code)
                .remark(create.getRemark())
                .createdAt(now)
                .updatedAt(now)
                .build();
        if (StringUtils.hasText(create.getPassword())) {
            po.setPwd(PasswordUtil.encode(create.getPassword()));
        }
        try {
            userMapper.insert(po);
        } catch (DuplicateKeyException e) {
            return R.fail("手机号、用户名或邮箱已存在");
        }
        return R.ok(po.getUserId());
    }

    @Override
    public R<?> update(UserUpdate update) {
        requireUser(update.getUserId());
        assertUnique(update.getTel(), update.getUserName(), update.getEmail(), update.getUserId());

        UserPo po = new UserPo();
        BeanUtils.copyProperties(update, po);
        po.setTel(blankToNull(update.getTel()));
        po.setUserName(blankToNull(update.getUserName()));
        po.setEmail(blankToNull(update.getEmail()));
        po.setPwd(null);
        po.setForbidden(null);
        po.setDeleted(null);
        po.setUpdatedAt(LocalDateTime.now());
        try {
            userMapper.updateById(po);
        } catch (DuplicateKeyException e) {
            return R.fail("手机号、用户名或邮箱已存在");
        }
        return R.ok("修改成功");
    }

    @Override
    public R<UserDetail> detail(Long userId) {
        UserDetail detail = userMapper.detail(userId);
        if (detail == null) {
            return R.fail("用户不存在");
        }
        return R.ok(detail);
    }

    @Override
    public R<?> changePassword(UserPasswordUpdate update) {
        UserPo user = requireUser(update.getUserId());
        if (StringUtils.hasText(update.getOldPassword())) {
            if (!PasswordUtil.matches(update.getOldPassword(), user.getPwd())) {
                return R.fail("原密码错误");
            }
        }
        userMapper.update(null, new LambdaUpdateWrapper<UserPo>()
                .eq(UserPo::getUserId, update.getUserId())
                .set(UserPo::getPwd, PasswordUtil.encode(update.getNewPassword()))
                .set(UserPo::getUpdatedAt, LocalDateTime.now()));
        return R.ok("密码修改成功");
    }

    @Override
    public UserPo getById(Long userId) {
        if (userId == null) {
            return null;
        }
        return userMapper.selectOne(new LambdaQueryWrapper<UserPo>()
                .eq(UserPo::getUserId, userId)
                .eq(UserPo::getDeleted, WhetherDict.No.code)
                .last("LIMIT 1"));
    }

    @Override
    public List<UserBrief> listByIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> distinctUserIds = userIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (distinctUserIds.isEmpty()) {
            return Collections.emptyList();
        }
        return userMapper.listByIds(distinctUserIds);
    }

    @Override
    public UserBrief getByTel(String tel) {
        if (!StringUtils.hasText(tel)) {
            return null;
        }
        return userMapper.findBriefByTel(tel);
    }

    @Override
    public UserBrief getByUserName(String userName) {
        if (!StringUtils.hasText(userName)) {
            return null;
        }
        return userMapper.findBriefByUserName(userName);
    }

    @Override
    public UserPo authenticate(String account, String rawPassword) {
        if (!StringUtils.hasText(account) || !StringUtils.hasText(rawPassword)) {
            return null;
        }
        UserPo user = userMapper.findByAccount(account.trim());
        if (user == null) {
            return null;
        }
        if (WhetherDict.Yes.code == user.getForbidden()) {
            throw InbyteException.fail("账号已禁用");
        }
        if (!PasswordUtil.matches(rawPassword, user.getPwd())) {
            return null;
        }
        return user;
    }

    private UserPo requireUser(Long userId) {
        UserPo user = getById(userId);
        if (user == null) {
            throw InbyteException.fail("用户不存在");
        }
        return user;
    }

    private void assertUnique(String tel, String userName, String email, Long excludeUserId) {
        if (StringUtils.hasText(tel)) {
            UserBrief exist = userMapper.findBriefByTel(tel);
            if (exist != null && !exist.getUserId().equals(excludeUserId)) {
                throw InbyteException.fail("手机号已存在");
            }
        }
        if (StringUtils.hasText(userName)) {
            UserBrief exist = userMapper.findBriefByUserName(userName);
            if (exist != null && !exist.getUserId().equals(excludeUserId)) {
                throw InbyteException.fail("用户名已存在");
            }
        }
        if (StringUtils.hasText(email)) {
            UserBrief exist = userMapper.findBriefByEmail(email);
            if (exist != null && !exist.getUserId().equals(excludeUserId)) {
                throw InbyteException.fail("邮箱已存在");
            }
        }
    }

    private static String blankToNull(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }
}
