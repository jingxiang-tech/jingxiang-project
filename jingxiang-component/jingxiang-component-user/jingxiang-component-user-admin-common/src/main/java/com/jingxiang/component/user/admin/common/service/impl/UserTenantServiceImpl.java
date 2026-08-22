package com.jingxiang.component.user.admin.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.jingxiang.commons.exception.InbyteException;
import com.jingxiang.commons.model.dict.WhetherDict;
import com.jingxiang.commons.model.dto.Page;
import com.jingxiang.commons.model.dto.R;
import com.jingxiang.commons.util.PageUtil;
import com.jingxiang.component.user.admin.common.dao.UserTenantMapper;
import com.jingxiang.component.user.admin.common.dict.TenantTypeEnum;
import com.jingxiang.component.user.admin.common.model.tenant.*;
import com.jingxiang.component.user.admin.common.service.UserTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 租户/组织服务实现
 *
 * @author chenjw
 */
@Service
public class UserTenantServiceImpl implements UserTenantService {

    @Autowired
    private UserTenantMapper userTenantMapper;

    @Override
    public R<Long> create(UserTenantCreate create) {
        if (getByCode(create.getTenantCode()) != null) {
            return R.fail("组织编码已存在");
        }
        validateParent(create.getParentId(), null);

        LocalDateTime now = LocalDateTime.now();
        UserTenantPo po = UserTenantPo.builder()
                .tenantType(create.getTenantType())
                .tenantCode(create.getTenantCode().trim())
                .tenantName(create.getTenantName().trim())
                .parentId(create.getParentId())
                .logo(create.getLogo())
                .forbidden(WhetherDict.No.code)
                .deleted(WhetherDict.No.code)
                .remark(create.getRemark())
                .createdAt(now)
                .updatedAt(now)
                .build();
        try {
            userTenantMapper.insert(po);
        } catch (DuplicateKeyException e) {
            return R.fail("组织编码已存在");
        }
        return R.ok(po.getTenantId());
    }

    @Override
    public Long ensureByCode(TenantTypeEnum tenantType, String tenantCode, String tenantName) {
        UserTenantBrief exist = getByCode(tenantCode);
        if (exist != null) {
            return exist.getTenantId();
        }
        UserTenantCreate create = new UserTenantCreate();
        create.setTenantType(tenantType);
        create.setTenantCode(tenantCode);
        create.setTenantName(StringUtils.hasText(tenantName) ? tenantName : tenantCode);
        R<Long> result = create(create);
        if (result.succeeded()) {
            return result.getData();
        }
        UserTenantBrief again = getByCode(tenantCode);
        if (again != null) {
            return again.getTenantId();
        }
        throw InbyteException.fail(result.getMsg());
    }

    @Override
    public R<?> update(UserTenantUpdate update) {
        requireTenant(update.getTenantId());
        if (update.getParentId() != null) {
            validateParent(update.getParentId(), update.getTenantId());
        }

        userTenantMapper.update(null, new LambdaUpdateWrapper<UserTenantPo>()
                .eq(UserTenantPo::getTenantId, update.getTenantId())
                .set(StringUtils.hasText(update.getTenantName()), UserTenantPo::getTenantName, update.getTenantName())
                .set(update.getParentId() != null, UserTenantPo::getParentId, update.getParentId())
                .set(update.getLogo() != null, UserTenantPo::getLogo, update.getLogo())
                .set(update.getRemark() != null, UserTenantPo::getRemark, update.getRemark())
                .set(UserTenantPo::getUpdatedAt, LocalDateTime.now()));
        return R.ok("修改成功");
    }

    @Override
    public R<UserTenantBrief> detail(Long tenantId) {
        UserTenantBrief brief = userTenantMapper.detail(tenantId);
        if (brief == null) {
            return R.fail("组织不存在");
        }
        return R.ok(brief);
    }

    @Override
    public R<Page<UserTenantBrief>> list(UserTenantQuery query) {
        PageUtil.startPage(query);
        return R.page(userTenantMapper.list(query));
    }

    @Override
    public R<?> setForbidden(Long tenantId, boolean forbidden) {
        requireTenant(tenantId);
        userTenantMapper.update(null, new LambdaUpdateWrapper<UserTenantPo>()
                .eq(UserTenantPo::getTenantId, tenantId)
                .set(UserTenantPo::getForbidden, forbidden ? WhetherDict.Yes.code : WhetherDict.No.code)
                .set(UserTenantPo::getUpdatedAt, LocalDateTime.now()));
        return R.ok(forbidden ? "已禁用" : "已启用");
    }

    @Override
    public R<?> delete(Long tenantId) {
        requireTenant(tenantId);
        userTenantMapper.update(null, new LambdaUpdateWrapper<UserTenantPo>()
                .eq(UserTenantPo::getTenantId, tenantId)
                .set(UserTenantPo::getDeleted, WhetherDict.Yes.code)
                .set(UserTenantPo::getUpdatedAt, LocalDateTime.now()));
        return R.ok("删除成功");
    }

    @Override
    public UserTenantBrief getByCode(String tenantCode) {
        if (!StringUtils.hasText(tenantCode)) {
            return null;
        }
        return userTenantMapper.findByCode(tenantCode);
    }

    @Override
    public UserTenantPo getAvailableById(Long tenantId) {
        if (tenantId == null) {
            return null;
        }
        UserTenantPo po = userTenantMapper.selectOne(new LambdaQueryWrapper<UserTenantPo>()
                .eq(UserTenantPo::getTenantId, tenantId)
                .eq(UserTenantPo::getDeleted, WhetherDict.No.code)
                .last("LIMIT 1"));
        if (po == null || WhetherDict.Yes.code == po.getForbidden()) {
            return null;
        }
        return po;
    }

    @Override
    public R<List<UserTenantBrief>> listChildren(Long parentId) {
        return R.ok(userTenantMapper.listByParentId(parentId));
    }

    private UserTenantPo requireTenant(Long tenantId) {
        UserTenantPo po = userTenantMapper.selectOne(new LambdaQueryWrapper<UserTenantPo>()
                .eq(UserTenantPo::getTenantId, tenantId)
                .eq(UserTenantPo::getDeleted, WhetherDict.No.code)
                .last("LIMIT 1"));
        if (po == null) {
            throw InbyteException.fail("组织不存在");
        }
        return po;
    }

    private void validateParent(Long parentId, Long selfId) {
        if (parentId == null) {
            return;
        }
        if (selfId != null && parentId.equals(selfId)) {
            throw InbyteException.fail("上级组织不能是自己");
        }
        UserTenantPo parent = userTenantMapper.selectOne(new LambdaQueryWrapper<UserTenantPo>()
                .eq(UserTenantPo::getTenantId, parentId)
                .eq(UserTenantPo::getDeleted, WhetherDict.No.code)
                .last("LIMIT 1"));
        if (parent == null) {
            throw InbyteException.fail("上级组织不存在");
        }
        if (WhetherDict.Yes.code == parent.getForbidden()) {
            throw InbyteException.fail("上级组织已禁用");
        }
        if (selfId != null && wouldCreateCycle(selfId, parentId)) {
            throw InbyteException.fail("不能形成循环的父子关系");
        }
    }

    private boolean wouldCreateCycle(Long selfId, Long parentId) {
        Set<Long> visited = new HashSet<>();
        Long current = parentId;
        int guard = 0;
        while (current != null && guard++ < 64) {
            if (current.equals(selfId)) {
                return true;
            }
            if (!visited.add(current)) {
                return true;
            }
            UserTenantPo node = userTenantMapper.selectById(current);
            if (node == null || WhetherDict.Yes.code == node.getDeleted()) {
                break;
            }
            current = node.getParentId();
        }
        return false;
    }
}
