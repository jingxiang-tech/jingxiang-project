package com.jingxiang.component.user.manager.merchant.controller;

import com.jingxiang.commons.model.dto.R;
import com.jingxiang.component.user.manager.merchant.model.member.UserMemberBrief;
import com.jingxiang.component.user.manager.merchant.model.member.UserMemberCreate;
import com.jingxiang.component.user.manager.merchant.model.member.UserMemberDisabledUpdate;
import com.jingxiang.component.user.manager.merchant.model.member.UserMemberPasswordUpdate;
import com.jingxiang.component.user.manager.merchant.model.member.UserMemberUsernameExistsBrief;
import com.jingxiang.component.user.manager.merchant.service.UserMemberManageService;
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
 * 组织成员接口（user_member，仅管理员）
 *
 * @author chenjw
 */
@RestController
@RequestMapping("user-member")
public class UserMemberController {

    @Autowired
    private UserMemberManageService userMemberManageService;

    /**
     * 创建组织成员
     */
    @PostMapping
    public R<String> create(@RequestBody @Valid UserMemberCreate request) {
        return userMemberManageService.create(request);
    }

    /**
     * 校验用户名是否已存在
     */
    @GetMapping("username-exists")
    public R<UserMemberUsernameExistsBrief> usernameExists(@RequestParam String userName) {
        return userMemberManageService.usernameExists(userName);
    }

    /**
     * 查询当前租户运营成员列表
     */
    @GetMapping
    public R<List<UserMemberBrief>> list() {
        return userMemberManageService.list();
    }

    /**
     * 启用/禁用组织成员
     */
    @PutMapping("disabled")
    public R<String> updateDisabled(@RequestBody @Valid UserMemberDisabledUpdate request) {
        return userMemberManageService.updateDisabled(request);
    }

    /**
     * 重置组织成员密码
     */
    @PutMapping("password")
    public R<String> resetPassword(@RequestBody @Valid UserMemberPasswordUpdate request) {
        return userMemberManageService.resetPassword(request);
    }
}
