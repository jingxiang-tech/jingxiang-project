package com.jingxiang.component.user.admin.merchant.controller;

import com.jingxiang.commons.model.dto.R;
import com.jingxiang.component.user.admin.merchant.model.SpaceUserBrief;
import com.jingxiang.component.user.admin.merchant.model.SpaceUserCreate;
import com.jingxiang.component.user.admin.merchant.model.SpaceUserPasswordUpdate;
import com.jingxiang.component.user.admin.merchant.model.SpaceUserStatusUpdate;
import com.jingxiang.component.user.admin.merchant.model.UsernameExistsBrief;
import com.jingxiang.component.user.admin.merchant.service.MerchantSpaceUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商户后台空间用户接口（仅管理员）。
 *
 * @author chenjw
 */
@RestController
@RequestMapping("merchant/space-user")
public class MerchantSpaceUserController {

    @Autowired
    private MerchantSpaceUserService merchantSpaceUserService;

    /**
     * 创建空间用户。
     */
    @PostMapping
    public R<String> create(@RequestBody @Valid SpaceUserCreate request) {
        return merchantSpaceUserService.create(request);
    }

    /**
     * 校验用户名是否已存在。
     */
    @GetMapping("username-exists")
    public R<UsernameExistsBrief> usernameExists(@RequestParam String username) {
        return merchantSpaceUserService.usernameExists(username);
    }

    /**
     * 查询当前管理员创建的空间用户列表。
     */
    @GetMapping
    public R<List<SpaceUserBrief>> list() {
        return merchantSpaceUserService.list();
    }

    /**
     * 启用/禁用空间用户。
     */
    @PutMapping("status")
    public R<String> updateStatus(@RequestBody @Valid SpaceUserStatusUpdate request) {
        return merchantSpaceUserService.updateStatus(request);
    }

    /**
     * 重置空间用户密码。
     */
    @PutMapping("password")
    public R<String> resetPassword(@RequestBody @Valid SpaceUserPasswordUpdate request) {
        return merchantSpaceUserService.resetPassword(request);
    }
}
