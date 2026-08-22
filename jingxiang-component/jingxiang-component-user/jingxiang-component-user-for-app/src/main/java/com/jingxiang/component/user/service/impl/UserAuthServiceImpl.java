package com.jingxiang.component.user.service.impl;

import com.jingxiang.commons.exception.InbyteException;
import com.jingxiang.commons.model.dict.WhetherDict;
import com.jingxiang.commons.model.dto.R;
import com.jingxiang.component.user.exception.MobileAlreadyBoundException;
import com.jingxiang.component.user.model.identity.BindMobileParam;
import com.jingxiang.component.user.model.identity.IdentityLoginParam;
import com.jingxiang.component.user.model.identity.UserIdentityCreate;
import com.jingxiang.component.user.model.identity.UserIdentityPo;
import com.jingxiang.component.user.model.identity.UserIdentityUpdate;
import com.jingxiang.component.user.model.user.UserBrief;
import com.jingxiang.component.user.model.user.UserCreate;
import com.jingxiang.component.user.model.user.UserDetail;
import com.jingxiang.component.user.model.user.UserPo;
import com.jingxiang.component.user.model.user.UserUpdate;
import com.jingxiang.component.user.datasource.UserTransactional;
import com.jingxiang.component.user.service.MobileVerifyPort;
import com.jingxiang.component.user.service.UserAuthService;
import com.jingxiang.component.user.service.UserIdentityService;
import com.jingxiang.component.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 第三方登录注册与手机号绑定
 *
 * @author chenjw
 */
@Service
public class UserAuthServiceImpl implements UserAuthService {

    @Autowired
    private UserIdentityService userIdentityService;

    @Autowired
    private UserService userService;

    @Autowired(required = false)
    private MobileVerifyPort mobileVerifyPort;

    @Override
    @UserTransactional
    public R<UserDetail> loginOrRegisterByIdentity(IdentityLoginParam param) {
        UserIdentityPo identity = userIdentityService.findByIdentity(
                param.getIdentityType(), param.getAppKey(), param.getIdentifier());

        if (identity != null) {
            return loginExisting(identity, param);
        }

        try {
            return registerNew(param);
        } catch (DuplicateKeyException e) {
            UserIdentityPo again = userIdentityService.findByIdentity(
                    param.getIdentityType(), param.getAppKey(), param.getIdentifier());
            if (again == null) {
                throw InbyteException.fail("登录失败，请重试");
            }
            return loginExisting(again, param);
        }
    }

    @Override
    @UserTransactional
    public R<?> bindMobile(BindMobileParam param) {
        UserPo current = userService.getById(param.getUserId());
        if (current == null) {
            return R.fail("用户不存在");
        }
        if (WhetherDict.Yes.code == current.getForbidden()) {
            return R.fail("用户已禁用");
        }

        if (mobileVerifyPort != null) {
            mobileVerifyPort.verify(param.getTel(), param.getVerifyCode());
        }

        UserBrief occupied = userService.getByTel(param.getTel());
        if (occupied != null && !occupied.getUserId().equals(param.getUserId())) {
            throw new MobileAlreadyBoundException(occupied.getUserId());
        }

        if (param.getTel().equals(current.getTel())) {
            return R.ok("手机号已绑定");
        }

        UserUpdate update = new UserUpdate();
        update.setUserId(param.getUserId());
        update.setTel(param.getTel());
        try {
            R<?> result = userService.update(update);
            if (result.failed()) {
                return result;
            }
        } catch (DuplicateKeyException e) {
            UserBrief again = userService.getByTel(param.getTel());
            if (again != null && !again.getUserId().equals(param.getUserId())) {
                throw new MobileAlreadyBoundException(again.getUserId());
            }
            throw InbyteException.fail("手机号绑定失败，请重试");
        }
        return R.ok("绑定成功");
    }

    private R<UserDetail> loginExisting(UserIdentityPo identity, IdentityLoginParam param) {
        if (WhetherDict.Yes.code == identity.getForbidden()) {
            return R.fail("该第三方身份已禁用");
        }
        UserPo user = userService.getById(identity.getUserId());
        if (user == null) {
            return R.fail("用户不存在");
        }
        if (WhetherDict.Yes.code == user.getForbidden()) {
            return R.fail("账号已禁用");
        }

        UserIdentityUpdate profile = new UserIdentityUpdate();
        profile.setIdentityId(identity.getIdentityId());
        if (StringUtils.hasText(param.getUnionId())) {
            profile.setUnionId(param.getUnionId());
        }
        if (StringUtils.hasText(param.getNickname())) {
            profile.setNickname(param.getNickname());
        }
        if (StringUtils.hasText(param.getAvatar())) {
            profile.setAvatar(param.getAvatar());
        }
        userIdentityService.updateProfile(profile);
        userIdentityService.touchLatestLogin(identity.getIdentityId());

        return userService.detail(user.getUserId());
    }

    private R<UserDetail> registerNew(IdentityLoginParam param) {
        UserCreate create = new UserCreate();
        create.setNickname(param.getNickname());
        create.setAvatar(param.getAvatar());
        R<Long> createResult = userService.create(create);
        if (createResult.failed()) {
            throw InbyteException.fail(createResult.getMsg());
        }

        UserIdentityCreate identityCreate = new UserIdentityCreate();
        identityCreate.setUserId(createResult.getData());
        identityCreate.setIdentityType(param.getIdentityType());
        identityCreate.setAppKey(param.getAppKey() == null ? "" : param.getAppKey());
        identityCreate.setIdentifier(param.getIdentifier());
        identityCreate.setUnionId(param.getUnionId());
        identityCreate.setNickname(param.getNickname());
        identityCreate.setAvatar(param.getAvatar());
        R<Long> bindResult = userIdentityService.bind(identityCreate);
        if (bindResult.failed()) {
            throw InbyteException.fail(bindResult.getMsg());
        }
        userIdentityService.touchLatestLogin(bindResult.getData());

        return userService.detail(createResult.getData());
    }
}
