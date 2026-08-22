package com.jingxiang.component.user.admin.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.jingxiang.commons.exception.InbyteException;
import com.jingxiang.commons.model.dict.WhetherDict;
import com.jingxiang.commons.model.dto.Page;
import com.jingxiang.commons.model.dto.R;
import com.jingxiang.commons.util.PageUtil;
import com.jingxiang.component.user.admin.common.dao.UserMemberMapper;
import com.jingxiang.component.user.admin.common.dict.MemberTypeEnum;
import com.jingxiang.component.user.admin.common.model.member.*;
import com.jingxiang.component.user.admin.common.model.tenant.UserTenantPo;
import com.jingxiang.component.user.model.user.UserPo;
import com.jingxiang.component.user.admin.common.service.UserMemberService;
import com.jingxiang.component.user.datasource.UserTransactional;
import com.jingxiang.component.user.service.UserService;
import com.jingxiang.component.user.admin.common.service.UserTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户组织成员服务实现
 *
 * @author chenjw
 */
@Service
public class UserMemberServiceImpl implements UserMemberService {

    @Autowired
    private UserMemberMapper userMemberMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserTenantService userTenantService;

    @Override
    @UserTransactional
    public R<Long> add(UserMemberCreate create) {
        UserPo user = userService.getById(create.getUserId());
        if (user == null) {
            return R.fail("用户不存在");
        }
        if (WhetherDict.Yes.code == user.getForbidden()) {
            return R.fail("用户已禁用");
        }
        UserTenantPo tenant = userTenantService.getAvailableById(create.getTenantId());
        if (tenant == null) {
            return R.fail("组织不存在或已禁用");
        }

        UserMemberPo exist = userMemberMapper.findByUserAndTenant(create.getUserId(), create.getTenantId());
        if (exist != null) {
            return R.fail("该用户已是组织成员");
        }

        List<String> roleCodeList = MemberTypeEnum.normalizeCodes(create.getRoleCodeList());
        if (roleCodeList.isEmpty()) {
            return R.fail("角色编码不能为空");
        }
        LocalDateTime now = LocalDateTime.now();
        UserMemberPo po = UserMemberPo.builder()
                .userId(create.getUserId())
                .tenantId(create.getTenantId())
                .roleCodeList(roleCodeList)
                .roleNameDesc(resolveRoleNameDesc(roleCodeList, create.getRoleNameDesc()))
                .forbidden(WhetherDict.No.code)
                .deleted(WhetherDict.No.code)
                .remark(create.getRemark())
                .createdAt(now)
                .updatedAt(now)
                .build();
        try {
            userMemberMapper.insert(po);
        } catch (DuplicateKeyException e) {
            return R.fail("该用户已是组织成员");
        }
        return R.ok(po.getMemberId());
    }

    @Override
    public R<?> remove(Long memberId) {
        requireMember(memberId);
        userMemberMapper.update(null, new LambdaUpdateWrapper<UserMemberPo>()
                .eq(UserMemberPo::getMemberId, memberId)
                .set(UserMemberPo::getDeleted, WhetherDict.Yes.code)
                .set(UserMemberPo::getUpdatedAt, LocalDateTime.now()));
        return R.ok("移除成功");
    }

    @Override
    public R<?> setForbidden(Long memberId, boolean forbidden) {
        requireMember(memberId);
        userMemberMapper.update(null, new LambdaUpdateWrapper<UserMemberPo>()
                .eq(UserMemberPo::getMemberId, memberId)
                .set(UserMemberPo::getForbidden, forbidden ? WhetherDict.Yes.code : WhetherDict.No.code)
                .set(UserMemberPo::getUpdatedAt, LocalDateTime.now()));
        return R.ok(forbidden ? "已禁用" : "已启用");
    }

    @Override
    public R<?> update(UserMemberUpdate update) {
        requireMember(update.getMemberId());
        List<String> roleCodeList = update.getRoleCodeList() == null
                ? null : MemberTypeEnum.normalizeCodes(update.getRoleCodeList());
        if (update.getRoleCodeList() != null && roleCodeList.isEmpty()) {
            return R.fail("角色编码不能为空");
        }
        String roleNameDesc = update.getRoleNameDesc();
        if (roleNameDesc == null && roleCodeList != null) {
            roleNameDesc = MemberTypeEnum.namesOf(roleCodeList);
        }
        userMemberMapper.update(null, new LambdaUpdateWrapper<UserMemberPo>()
                .eq(UserMemberPo::getMemberId, update.getMemberId())
                .set(roleCodeList != null, UserMemberPo::getRoleCodeList, roleCodeList)
                .set(roleNameDesc != null, UserMemberPo::getRoleNameDesc, roleNameDesc)
                .set(update.getRemark() != null, UserMemberPo::getRemark, update.getRemark())
                .set(UserMemberPo::getUpdatedAt, LocalDateTime.now()));
        return R.ok("修改成功");
    }

    @Override
    public R<UserMemberBrief> detail(Long memberId) {
        UserMemberBrief brief = userMemberMapper.detail(memberId);
        if (brief == null) {
            return R.fail("成员不存在");
        }
        return R.ok(brief);
    }

    @Override
    public R<Page<UserMemberBrief>> list(UserMemberQuery query) {
        PageUtil.startPage(query);
        return R.page(userMemberMapper.list(query));
    }

    @Override
    public R<List<UserMemberBrief>> listAll(UserMemberQuery query) {
        return R.ok(userMemberMapper.list(query));
    }

    @Override
    public R<List<UserMemberBrief>> listTenantsByUserId(Long userId) {
        return R.ok(userMemberMapper.listTenantsByUserId(userId));
    }

    @Override
    public UserMemberPo getByUserAndTenant(Long userId, Long tenantId) {
        return userMemberMapper.findByUserAndTenant(userId, tenantId);
    }

    @Override
    public boolean belongsTo(Long userId, Long tenantId) {
        UserMemberPo member = getByUserAndTenant(userId, tenantId);
        return member != null && WhetherDict.No.code == member.getForbidden();
    }

    private UserMemberPo requireMember(Long memberId) {
        UserMemberPo po = userMemberMapper.selectById(memberId);
        if (po == null || WhetherDict.Yes.code == po.getDeleted()) {
            throw InbyteException.fail("成员不存在");
        }
        return po;
    }

    private String resolveRoleNameDesc(List<String> roleCodeList, String roleNameDesc) {
        if (roleNameDesc != null && !roleNameDesc.isBlank()) {
            return roleNameDesc.trim();
        }
        return MemberTypeEnum.namesOf(roleCodeList);
    }
}
