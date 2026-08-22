package com.jingxiang.component.user.admin.merchant.controller;

import com.jingxiang.commons.model.dto.Dict;
import com.jingxiang.commons.model.dto.Page;
import com.jingxiang.commons.model.dto.R;
import com.jingxiang.component.user.admin.core.model.member.UserMemberBrief;
import com.jingxiang.component.user.admin.merchant.model.*;
import com.jingxiang.component.user.admin.merchant.service.SystemUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商户后台用户接口。
 *
 * @author chenjw
 */
@RestController
@RequestMapping("system/user")
public class SystemUserController {

    @Autowired
    private SystemUserService systemUserService;

    @PostMapping("login/id-pwd")
    public R<SystemUserLoginDto> idPwdLogin(@RequestBody @Valid SystemUserLoginParam param) {
        return systemUserService.idPwdLogin(param);
    }

    @GetMapping("info")
    public R<SystemUserInfo> info() {
        return systemUserService.info();
    }

    @PutMapping("init-guide-done")
    public R<?> initGuideDone() {
        return systemUserService.markInitGuideDone();
    }

    @GetMapping("dict")
    public R<List<Dict>> dict(@RequestParam(value = "keyword", required = false) String keyword) {
        return systemUserService.dict(keyword);
    }

    @PostMapping
    public R<?> insert(@RequestBody @Valid SystemUserInsert insert) {
        return systemUserService.insert(insert);
    }

    @DeleteMapping("{userId}")
    public R<?> delete(@PathVariable Integer userId) {
        return systemUserService.delete(userId);
    }

    @PutMapping
    public R<?> update(@RequestBody @Valid SystemUserUpdate update) {
        return systemUserService.update(update);
    }

    @PutMapping("{userId}/reset-pwd")
    public R<?> resetPwd(@PathVariable Integer userId) {
        return systemUserService.resetPwd(userId);
    }

    @PutMapping("pwd")
    public R<?> updatePwd(@RequestBody @Valid SystemUserPwdUpdate update) {
        return systemUserService.updatePwd(update);
    }

    @GetMapping("{userId}")
    public R<UserMemberBrief> detail(@PathVariable Integer userId) {
        return systemUserService.detail(userId);
    }

    @GetMapping
    public R<Page<UserMemberBrief>> list(@ModelAttribute @Valid MerchantUserQuery query) {
        return systemUserService.list(query);
    }

    @GetMapping("space/{spaceId}/switch")
    public R<String> switchSpace(@PathVariable Integer spaceId) {
        return systemUserService.switchSpace(spaceId);
    }

    @GetMapping("space")
    public R<List<SpaceOption>> spaceList() {
        return systemUserService.spaceList();
    }
}
