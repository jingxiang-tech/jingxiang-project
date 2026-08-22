package com.jingxiang.component.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.jingxiang.commons.exception.InbyteException;
import com.jingxiang.commons.model.dict.WhetherDict;
import com.jingxiang.commons.model.dto.R;
import com.jingxiang.component.user.dao.UserIdentityMapper;
import com.jingxiang.component.user.dict.IdentityTypeEnum;
import com.jingxiang.component.user.model.identity.UserIdentityBrief;
import com.jingxiang.component.user.model.identity.UserIdentityCreate;
import com.jingxiang.component.user.model.identity.UserIdentityPo;
import com.jingxiang.component.user.model.identity.UserIdentityUpdate;
import com.jingxiang.component.user.model.user.UserPo;
import com.jingxiang.component.user.service.UserIdentityService;
import com.jingxiang.component.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户第三方身份服务实现
 *
 * @author chenjw
 */
@Service
public class UserIdentityServiceImpl implements UserIdentityService {

    @Autowired
    private UserIdentityMapper userIdentityMapper;

    @Autowired
    private UserService userService;

    @Override
    public R<UserIdentityBrief> detail(Long identityId) {
        UserIdentityBrief brief = userIdentityMapper.detail(identityId);
        if (brief == null) {
            return R.fail("第三方身份不存在");
        }
        return R.ok(brief);
    }

    @Override
    public R<List<UserIdentityBrief>> listByUserId(Long userId) {
        return R.ok(userIdentityMapper.listByUserId(userId));
    }

    @Override
    public UserIdentityPo findByIdentity(IdentityTypeEnum identityType, String appKey, String identifier) {
        if (identityType == null || appKey == null || !StringUtils.hasText(identifier)) {
            return null;
        }
        return userIdentityMapper.findByIdentity(identityType, appKey, identifier);
    }

    @Override
    public R<Long> bind(UserIdentityCreate create) {
        if (create.getUserId() == null) {
            return R.fail("用户ID不能为空");
        }
        UserPo user = userService.getById(create.getUserId());
        if (user == null) {
            return R.fail("用户不存在");
        }
        if (WhetherDict.Yes.code == user.getForbidden()) {
            return R.fail("用户已禁用");
        }

        UserIdentityPo exist = findByIdentity(create.getIdentityType(), create.getAppKey(), create.getIdentifier());
        if (exist != null) {
            if (exist.getUserId().equals(create.getUserId())) {
                return R.ok(exist.getIdentityId());
            }
            return R.fail("该第三方身份已绑定其他用户");
        }

        LocalDateTime now = LocalDateTime.now();
        UserIdentityPo po = UserIdentityPo.builder()
                .userId(create.getUserId())
                .identityType(create.getIdentityType())
                .appKey(create.getAppKey() == null ? "" : create.getAppKey())
                .identifier(create.getIdentifier())
                .unionId(create.getUnionId())
                .nickname(create.getNickname())
                .avatar(create.getAvatar())
                .forbidden(WhetherDict.No.code)
                .deleted(WhetherDict.No.code)
                .remark(create.getRemark())
                .createdAt(now)
                .updatedAt(now)
                .build();
        try {
            userIdentityMapper.insert(po);
        } catch (DuplicateKeyException e) {
            UserIdentityPo again = findByIdentity(create.getIdentityType(), create.getAppKey(), create.getIdentifier());
            if (again != null && again.getUserId().equals(create.getUserId())) {
                return R.ok(again.getIdentityId());
            }
            return R.fail("该第三方身份已绑定其他用户");
        }
        return R.ok(po.getIdentityId());
    }

    @Override
    public R<?> unbind(Long identityId) {
        requireIdentity(identityId);
        userIdentityMapper.update(null, new LambdaUpdateWrapper<UserIdentityPo>()
                .eq(UserIdentityPo::getIdentityId, identityId)
                .set(UserIdentityPo::getDeleted, WhetherDict.Yes.code)
                .set(UserIdentityPo::getUpdatedAt, LocalDateTime.now()));
        return R.ok("解绑成功");
    }

    @Override
    public R<?> setForbidden(Long identityId, boolean forbidden) {
        requireIdentity(identityId);
        userIdentityMapper.update(null, new LambdaUpdateWrapper<UserIdentityPo>()
                .eq(UserIdentityPo::getIdentityId, identityId)
                .set(UserIdentityPo::getForbidden, forbidden ? WhetherDict.Yes.code : WhetherDict.No.code)
                .set(UserIdentityPo::getUpdatedAt, LocalDateTime.now()));
        return R.ok(forbidden ? "已禁用" : "已启用");
    }

    @Override
    public R<?> updateProfile(UserIdentityUpdate update) {
        requireIdentity(update.getIdentityId());
        userIdentityMapper.update(null, new LambdaUpdateWrapper<UserIdentityPo>()
                .eq(UserIdentityPo::getIdentityId, update.getIdentityId())
                .set(update.getUnionId() != null, UserIdentityPo::getUnionId, update.getUnionId())
                .set(update.getNickname() != null, UserIdentityPo::getNickname, update.getNickname())
                .set(update.getAvatar() != null, UserIdentityPo::getAvatar, update.getAvatar())
                .set(update.getRemark() != null, UserIdentityPo::getRemark, update.getRemark())
                .set(UserIdentityPo::getUpdatedAt, LocalDateTime.now()));
        return R.ok("更新成功");
    }

    @Override
    public R<?> touchLatestLogin(Long identityId) {
        requireIdentity(identityId);
        LocalDateTime now = LocalDateTime.now();
        userIdentityMapper.update(null, new LambdaUpdateWrapper<UserIdentityPo>()
                .eq(UserIdentityPo::getIdentityId, identityId)
                .set(UserIdentityPo::getLatestLoginAt, now)
                .set(UserIdentityPo::getUpdatedAt, now));
        return R.ok();
    }

    private UserIdentityPo requireIdentity(Long identityId) {
        UserIdentityPo po = userIdentityMapper.selectById(identityId);
        if (po == null || WhetherDict.Yes.code == po.getDeleted()) {
            throw InbyteException.fail("第三方身份不存在");
        }
        return po;
    }
}
