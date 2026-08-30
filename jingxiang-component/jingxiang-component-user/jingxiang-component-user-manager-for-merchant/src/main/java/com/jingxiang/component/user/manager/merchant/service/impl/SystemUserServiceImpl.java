package com.jingxiang.component.user.manager.merchant.service.impl;

import com.jingxiang.commons.model.dto.Dict;
import com.jingxiang.commons.model.dto.Page;
import com.jingxiang.commons.model.dto.R;
import com.jingxiang.commons.model.dto.ResultStatus;
import com.jingxiang.component.user.manager.common.dict.MemberRoleEnum;
import com.jingxiang.component.user.manager.common.dict.TenantTypeEnum;
import com.jingxiang.component.user.manager.common.model.member.*;
import com.jingxiang.component.user.manager.common.model.tenant.UserTenantBrief;
import com.jingxiang.component.user.manager.common.service.UserManagerService;
import com.jingxiang.component.user.manager.common.service.UserMemberService;
import com.jingxiang.component.user.manager.common.service.UserTenantService;
import com.jingxiang.component.user.manager.merchant.config.MerchantUserManagerProperties;
import com.jingxiang.component.user.manager.merchant.model.*;
import com.jingxiang.component.user.manager.merchant.port.MerchantDirectoryPort;
import com.jingxiang.component.user.manager.merchant.port.MerchantDirectoryPort.MerchantDirectory;
import com.jingxiang.component.user.manager.merchant.port.MerchantUserSpacePort;
import com.jingxiang.component.user.manager.merchant.port.MerchantUserSpacePort.MerchantSpace;
import com.jingxiang.component.user.manager.merchant.port.MerchantUserSpacePort.UserSpace;
import com.jingxiang.component.user.manager.merchant.service.SystemUserService;
import com.jingxiang.component.user.manager.merchant.session.MerchantSessionUser;
import com.jingxiang.component.user.jwt.SessionUtil;
import com.jingxiang.component.user.datasource.UserTransactional;
import com.jingxiang.component.user.model.user.UserCreate;
import com.jingxiang.component.user.model.user.UserPasswordUpdate;
import com.jingxiang.component.user.model.user.UserPo;
import com.jingxiang.component.user.model.user.UserUpdate;
import com.jingxiang.component.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 商户后台用户服务实现。
 * <p>
 * 统一用户库与宿主业务库之间不使用跨库事务；写操作采用固定顺序、幂等端口和失败补偿。
 *
 * @author chenjw
 */
@Service
public class SystemUserServiceImpl implements SystemUserService {

    private static final Logger log = LoggerFactory.getLogger(SystemUserServiceImpl.class);
    private static final String DEFAULT_AVATAR =
            "https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png";

    @Autowired
    private MerchantUserSpacePort merchantUserSpacePort;

    @Autowired
    private MerchantDirectoryPort merchantDirectoryPort;

    @Autowired
    private UserService userService;

    @Autowired
    private UserManagerService userManagerService;

    @Autowired
    private UserTenantService userTenantService;

    @Autowired
    private UserMemberService userMemberService;

    @Autowired
    private MerchantUserManagerProperties properties;

    @Override
    public R<SystemUserLoginDto> idPwdLogin(SystemUserLoginParam param) {
        UserPo user = userService.authenticate(param.getId(), param.getPwd());
        if (user == null) {
            return R.fail("账号或密码错误");
        }
        List<UserSpace> spaces = merchantUserSpacePort.listByUserId(user.getUserId().intValue());
        if (CollectionUtils.isEmpty(spaces)) {
            return R.fail("无可用业务空间，请联系管理员");
        }
        if (spaces.size() > 1) {
            MerchantSessionUser preSession = baseSession(user);
            String token = SessionUtil.getJwtToken(preSession);
            return R.ok(new SystemUserLoginDto(token, 1, convertSpaces(spaces)));
        }
        String token = SessionUtil.getJwtToken(buildSessionUser(user, spaces.get(0)));
        return R.ok(new SystemUserLoginDto(token, null, null));
    }

    @Override
    public R<SystemUserInfo> info() {
        MerchantSessionUser sessionUser = SessionUtil.getSessionUser();
        UserPo user = userService.getById(sessionUser.getUserId().longValue());
        if (user == null) {
            return R.set(ResultStatus.Unauthorized, "用户信息不存在");
        }
        MerchantDirectory merchant = merchantDirectoryPort.findByMerchantNo(sessionUser.getTenantCode());
        UserMemberPo member = userMemberService.getByUserAndTenant(user.getUserId(), sessionUser.getTenantId());
        List<String> roles = member != null && member.getRoleCodeList() != null
                ? member.getRoleCodeList() : Collections.emptyList();
        int initGuideDone = merchant != null && merchant.initGuideDone() != null
                ? merchant.initGuideDone() : 0;
        return R.ok(new SystemUserInfo(
                user.getUserId().intValue(),
                user.getUserName(),
                user.getAvatar(),
                0,
                roles,
                initGuideDone,
                sessionUser.getSpaceId(),
                sessionUser.getSpaceName(),
                sessionUser.getAdmin()));
    }

    @Override
    public R<?> markInitGuideDone() {
        merchantDirectoryPort.markInitGuideDone(SessionUtil.getTenantCode(), SessionUtil.getUserName());
        return R.ok("操作成功");
    }

    @Override
    public R<List<Dict>> dict(String keyword) {
        UserMemberQuery query = new UserMemberQuery();
        query.setTenantId(SessionUtil.getTenantId());
        query.setKeyword(keyword);
        query.setPageSize(200);
        R<Page<UserMemberBrief>> result = userMemberService.list(query);
        if (result.failed() || result.getData() == null || result.getData().getList() == null) {
            return R.ok(Collections.emptyList());
        }
        List<Dict> dicts = result.getData().getList().stream().map(member -> {
            Dict dict = new Dict();
            dict.setCode(String.valueOf(member.getUserId()));
            String label = StringUtils.hasText(member.getUserName())
                    ? member.getUserName() : member.getNickname();
            dict.setName(label != null ? label : String.valueOf(member.getUserId()));
            return dict;
        }).toList();
        return R.ok(dicts);
    }

    @Override
    public R<?> insert(SystemUserInsert insert) {
        if (!SessionUtil.isSpaceAdmin()) {
            return R.fail("仅管理员可添加员工");
        }
        String merchantNo = SessionUtil.getTenantCode();
        Long tenantId = SessionUtil.getTenantId();
        List<Integer> spaceIds = CollectionUtils.isEmpty(insert.getSpaceIds())
                ? List.of(SessionUtil.requireSpaceId()) : insert.getSpaceIds().stream().distinct().toList();
        List<MerchantSpace> spaces = merchantUserSpacePort.listValidSpaces(merchantNo, spaceIds);
        if (spaces.size() != spaceIds.size()) {
            return R.fail("所选业务空间不合法");
        }

        MerchantDirectory merchant = merchantDirectoryPort.findByMerchantNo(merchantNo);
        UserPo user = findExistingUser(insert);
        boolean newUser = user == null;
        boolean alreadyMember = !newUser && userMemberService.belongsTo(user.getUserId(), tenantId);
        if (!alreadyMember && employeeLimitExceeded(merchantNo, merchant)) {
            return R.fail("该商户员工数量已达上限");
        }

        if (newUser) {
            R<Long> createResult = userService.create(toUserCreate(insert));
            if (createResult.failed()) {
                return R.fail(createResult.getMsg());
            }
            user = userService.getById(createResult.getData());
            if (user == null) {
                compensateNewUser(true, createResult.getData());
                return R.fail("新增用户后读取用户信息失败");
            }
        }

        List<String> roleCodeList = resolveRoleCodes(insert.getRole());
        Long addedMemberId = null;
        List<UserSpace> addedSpaces = Collections.emptyList();
        try {
            if (!alreadyMember) {
                R<Long> addResult = addMember(user.getUserId(), tenantId, roleCodeList);
                if (addResult.failed()) {
                    compensateNewUser(newUser, user.getUserId());
                    return R.fail(addResult.getMsg());
                }
                addedMemberId = addResult.getData();
            }
            String merchantName = merchant != null && merchant.merchantName() != null
                    ? merchant.merchantName() : "";
            addedSpaces = merchantUserSpacePort.bindSpaces(
                    user.getUserId().intValue(),
                    merchantNo,
                    merchantName,
                    spaces,
                    SessionUtil.getUserName());
            if (!newUser) {
                R<?> passwordResult = changePassword(user.getUserId(), properties.getExistingUserPassword());
                if (passwordResult.failed()) {
                    throw new IllegalStateException(passwordResult.getMsg());
                }
            }
            return R.ok("新增成功");
        } catch (RuntimeException e) {
            compensateInsert(user.getUserId(), merchantNo, addedSpaces, addedMemberId, newUser);
            log.error("商户用户新增编排失败，已执行补偿，userId={}, merchantNo={}",
                    user.getUserId(), merchantNo, e);
            return R.fail("新增失败，请稍后重试");
        }
    }

    @Override
    public R<?> delete(Integer userId) {
        if (!SessionUtil.isSpaceAdmin()) {
            return R.fail("仅管理员可删除员工");
        }
        Long tenantId = SessionUtil.getTenantId();
        String merchantNo = SessionUtil.getTenantCode();
        UserMemberPo member = userMemberService.getByUserAndTenant(userId.longValue(), tenantId);
        if (member == null) {
            return R.fail("无权限操作该用户");
        }
        List<UserSpace> removed = merchantUserSpacePort.removeByUserAndMerchant(userId, merchantNo);
        try {
            R<?> result = userMemberService.remove(member.getMemberId());
            if (result.failed()) {
                merchantUserSpacePort.restoreSpaces(userId, removed, SessionUtil.getUserName());
                return R.fail(result.getMsg());
            }
            return R.ok("删除成功");
        } catch (RuntimeException e) {
            merchantUserSpacePort.restoreSpaces(userId, removed, SessionUtil.getUserName());
            throw e;
        }
    }

    @Override
    @UserTransactional
    public R<?> update(SystemUserUpdate update) {
        UserMemberPo member = userMemberService.getByUserAndTenant(
                update.getUserId().longValue(), SessionUtil.getTenantId());
        if (member == null) {
            return R.fail("无权限操作该用户");
        }
        if (!CollectionUtils.isEmpty(update.getRole())
                && update.getRole().contains(MemberRoleEnum.OWNER.code)
                && !MemberRoleEnum.contains(member.getRoleCodeList(), MemberRoleEnum.OWNER)) {
            return R.fail("管理员角色不允许修改");
        }
        R<?> userResult = userService.update(toUserUpdate(update));
        if (userResult.failed()) {
            return R.fail(userResult.getMsg());
        }
        if (!CollectionUtils.isEmpty(update.getRole())) {
            UserMemberUpdate memberUpdate = new UserMemberUpdate();
            memberUpdate.setMemberId(member.getMemberId());
            memberUpdate.setRoleCodeList(resolveRoleCodes(update.getRole()));
            return userMemberService.update(memberUpdate);
        }
        return R.ok("修改成功");
    }

    @Override
    public R<UserMemberBrief> detail(Integer userId) {
        UserMemberPo member = userMemberService.getByUserAndTenant(
                userId.longValue(), SessionUtil.getTenantId());
        return member == null ? R.fail("用户不存在") : userMemberService.detail(member.getMemberId());
    }

    @Override
    public R<Page<UserMemberBrief>> list(MerchantUserQuery query) {
        if (!SessionUtil.isSpaceAdmin()) {
            return R.fail("仅管理员可查看员工列表");
        }
        UserMemberQuery memberQuery = new UserMemberQuery();
        memberQuery.setTenantId(SessionUtil.getTenantId());
        memberQuery.setKeyword(query.getKeyword());
        memberQuery.setPageNum(query.getPageNum());
        memberQuery.setPageSize(query.getPageSize());
        return userMemberService.list(memberQuery);
    }

    @Override
    public R<?> updatePwd(SystemUserPwdUpdate update) {
        return changePassword(SessionUtil.getUserId().longValue(), update.getPwd());
    }

    @Override
    public R<?> resetPwd(Integer userId) {
        if (!SessionUtil.isSpaceAdmin()) {
            return R.fail("只有超管可以重置用户密码");
        }
        if (!userMemberService.belongsTo(userId.longValue(), SessionUtil.getTenantId())) {
            return R.fail("无权限操作该用户");
        }
        return changePassword(userId.longValue(), properties.getInitialPassword());
    }

    @Override
    public R<String> switchSpace(Integer spaceId) {
        MerchantSessionUser sessionUser = SessionUtil.getSessionUser();
        UserSpace chosen = merchantUserSpacePort.findByUserAndSpace(sessionUser.getUserId(), spaceId);
        if (chosen == null) {
            return R.fail("当前用户没有该业务空间权限，切换失败");
        }
        UserPo user = userService.getById(sessionUser.getUserId().longValue());
        return R.ok("切换成功", SessionUtil.getJwtToken(buildSessionUser(user, chosen)));
    }

    @Override
    public R<List<SpaceOption>> spaceList() {
        MerchantSessionUser sessionUser = SessionUtil.getSessionUser();
        return R.ok(convertSpaces(merchantUserSpacePort.listByUserId(sessionUser.getUserId())));
    }

    private MerchantSessionUser buildSessionUser(UserPo user, UserSpace space) {
        String fallbackName = space.merchantName() != null ? space.merchantName() : space.merchantNo();
        Long tenantId = userTenantService.ensureByCode(
                TenantTypeEnum.MERCHANT, space.merchantNo(), fallbackName);
        UserTenantBrief tenant = userTenantService.getByCode(space.merchantNo());
        MerchantSessionUser session = baseSession(user);
        session.setSpaceId(space.spaceId());
        session.setSpaceName(space.spaceName());
        session.setTenantId(tenant != null ? tenant.getTenantId() : tenantId);
        session.setTenantCode(space.merchantNo());
        session.setTenantName(tenant != null && tenant.getTenantName() != null
                ? tenant.getTenantName() : fallbackName);
        session.setAdmin(space.admin());
        return session;
    }

    private MerchantSessionUser baseSession(UserPo user) {
        MerchantSessionUser session = new MerchantSessionUser();
        session.setUserId(user.getUserId().intValue());
        session.setUserName(user.getUserName());
        session.setTel(user.getTel());
        session.setLoginTime(LocalDateTime.now());
        session.setAdmin(0);
        session.setLoginWay("id-pwd");
        return session;
    }

    private List<SpaceOption> convertSpaces(List<UserSpace> spaces) {
        if (CollectionUtils.isEmpty(spaces)) {
            return Collections.emptyList();
        }
        boolean singleMerchant = spaces.stream().map(UserSpace::merchantNo).distinct().count() <= 1;
        return spaces.stream().map(space -> {
            String name = singleMerchant
                    ? space.spaceName()
                    : (space.merchantName() != null ? space.merchantName() : "") + " - " + space.spaceName();
            return new SpaceOption(space.spaceId(), name);
        }).toList();
    }

    private UserPo findExistingUser(SystemUserInsert insert) {
        var byName = userService.getByUserName(insert.getUserName());
        if (byName != null) {
            return userService.getById(byName.getUserId());
        }
        var byTel = userService.getByTel(insert.getTel());
        return byTel == null ? null : userService.getById(byTel.getUserId());
    }

    private boolean employeeLimitExceeded(String merchantNo, MerchantDirectory merchant) {
        return merchant != null
                && merchant.maxEmployeeCount() != null
                && merchantUserSpacePort.countDistinctUsers(merchantNo) + 1 > merchant.maxEmployeeCount();
    }

    private R<Long> addMember(Long userId, Long tenantId, List<String> roleCodeList) {
        UserMemberCreate create = new UserMemberCreate();
        create.setUserId(userId);
        create.setTenantId(tenantId);
        create.setRoleCodeList(roleCodeList);
        return userMemberService.add(create);
    }

    private R<?> changePassword(Long userId, String password) {
        UserPasswordUpdate update = new UserPasswordUpdate();
        update.setUserId(userId);
        update.setNewPassword(password);
        return userService.changePassword(update);
    }

    private UserCreate toUserCreate(SystemUserInsert insert) {
        UserCreate create = new UserCreate();
        create.setUserName(insert.getUserName());
        create.setTel(insert.getTel());
        create.setPassword(insert.getPwd());
        create.setNickname(insert.getUserName());
        create.setRealName(insert.getRealName());
        create.setEmail(insert.getEmail());
        create.setAvatar(DEFAULT_AVATAR);
        return create;
    }

    private UserUpdate toUserUpdate(SystemUserUpdate update) {
        UserUpdate userUpdate = new UserUpdate();
        userUpdate.setUserId(update.getUserId().longValue());
        userUpdate.setUserName(update.getUserName());
        userUpdate.setNickname(update.getNickname());
        userUpdate.setRealName(update.getRealName());
        userUpdate.setTel(update.getTel());
        userUpdate.setAvatar(update.getAvatar());
        userUpdate.setEmail(update.getEmail());
        userUpdate.setRemark(update.getRemark());
        return userUpdate;
    }

    private List<String> resolveRoleCodes(List<String> roles) {
        List<String> codes = MemberRoleEnum.normalizeCodes(roles);
        return codes.isEmpty() ? List.of(MemberRoleEnum.MEMBER.code) : codes;
    }

    private void compensateInsert(Long userId, String merchantNo, List<UserSpace> addedSpaces,
                                  Long addedMemberId, boolean newUser) {
        try {
            List<Integer> spaceIds = addedSpaces.stream().map(UserSpace::spaceId).toList();
            if (!spaceIds.isEmpty()) {
                merchantUserSpacePort.removeSpaces(userId.intValue(), merchantNo, spaceIds);
            }
        } catch (RuntimeException e) {
            log.error("补偿商户用户空间关系失败，userId={}, merchantNo={}", userId, merchantNo, e);
        }
        if (addedMemberId != null) {
            try {
                userMemberService.remove(addedMemberId);
            } catch (RuntimeException e) {
                log.error("补偿用户成员关系失败，userId={}, memberId={}", userId, addedMemberId, e);
            }
        }
        compensateNewUser(newUser, userId);
    }

    private void compensateNewUser(boolean newUser, Long userId) {
        if (!newUser) {
            return;
        }
        try {
            userManagerService.delete(userId);
        } catch (RuntimeException e) {
            log.error("补偿新建统一用户失败，userId={}", userId, e);
        }
    }
}
