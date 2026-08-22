package com.jingxiang.component.user.admin.merchant.controller;

import com.jingxiang.commons.model.dto.R;
import com.jingxiang.component.user.admin.merchant.model.MerchantSpaceUserBrief;
import com.jingxiang.component.user.admin.merchant.model.MerchantSpaceUserCreate;
import com.jingxiang.component.user.admin.merchant.model.MerchantSpaceUserDisabledUpdate;
import com.jingxiang.component.user.admin.merchant.model.MerchantSpaceUserPasswordUpdate;
import com.jingxiang.component.user.admin.merchant.model.MerchantSpaceUserUsernameExistsBrief;
import com.jingxiang.component.user.admin.merchant.service.MerchantSpaceUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public R<String> create(@RequestBody @Valid MerchantSpaceUserCreate request) {
        return merchantSpaceUserService.create(request);
    }

    /**
     * 校验用户名是否已存在。
     */
    @GetMapping("username-exists")
    public R<MerchantSpaceUserUsernameExistsBrief> usernameExists(@RequestParam String userName) {
        return merchantSpaceUserService.usernameExists(userName);
    }

    /**
     * 查询当前管理员创建的空间用户列表。
     */
    @GetMapping
    public R<List<MerchantSpaceUserBrief>> list() {
        return merchantSpaceUserService.list();
    }

    /**
     * 启用/禁用空间用户。
     */
    @PutMapping("disabled")
    public R<String> updateDisabled(@RequestBody @Valid MerchantSpaceUserDisabledUpdate request) {
        return merchantSpaceUserService.updateDisabled(request);
    }

    /**
     * 重置空间用户密码。
     */
    @PutMapping("password")
    public R<String> resetPassword(@RequestBody @Valid MerchantSpaceUserPasswordUpdate request) {
        return merchantSpaceUserService.resetPassword(request);
    }
}
