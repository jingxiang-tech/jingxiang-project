package com.jingxiang.component.user.manager.platform.controller;

import com.jingxiang.commons.model.dto.R;
import com.jingxiang.component.user.manager.platform.model.*;
import com.jingxiang.component.user.manager.platform.service.PlatformMerchantUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 平台端商户用户管理入口。
 */
@RestController
@RequestMapping("platform/merchant-user")
public class PlatformMerchantUserController {

    @Autowired
    private PlatformMerchantUserService merchantUserService;

    @GetMapping
    public R<List<MerchantUserBrief>> list(@Valid MerchantUserQuery query) {
        return merchantUserService.list(query);
    }

    @GetMapping("{userId}")
    public R<MerchantUserDetail> get(@PathVariable Integer userId) {
        return merchantUserService.get(userId);
    }

    @PostMapping
    public R<Void> create(@RequestBody @Valid MerchantUserCreate create) {
        return merchantUserService.create(create);
    }

    @PutMapping
    public R<Void> update(@RequestBody @Valid MerchantUserUpdate update) {
        return merchantUserService.update(update);
    }

    @DeleteMapping("{userId}")
    public R<Void> delete(@PathVariable Integer userId) {
        return merchantUserService.delete(userId);
    }

    @PostMapping("{userId}/reset-pwd")
    public R<Void> resetPassword(@PathVariable Integer userId) {
        return merchantUserService.resetPassword(userId);
    }

    @PutMapping("{userId}/disabled")
    public R<Void> setDisabled(@PathVariable Integer userId, @RequestParam Integer disabled) {
        return merchantUserService.setDisabled(userId, disabled);
    }

    @GetMapping("{userId}/space-ids")
    public R<List<Integer>> getSpaceIds(@PathVariable Integer userId) {
        return merchantUserService.getSpaceIds(userId);
    }

    @PutMapping("{userId}/space-ids")
    public R<Void> putSpaceIds(
            @PathVariable Integer userId,
            @RequestBody MerchantUserSpaceIdsUpdate update) {
        return merchantUserService.putSpaceIds(userId, update == null ? null : update.getSpaceIds());
    }
}
