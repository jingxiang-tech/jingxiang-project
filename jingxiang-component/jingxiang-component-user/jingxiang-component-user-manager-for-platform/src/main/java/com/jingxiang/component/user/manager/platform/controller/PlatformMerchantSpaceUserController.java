package com.jingxiang.component.user.manager.platform.controller;

import com.jingxiang.commons.model.dto.R;
import com.jingxiang.component.user.manager.platform.model.MerchantUserBrief;
import com.jingxiang.component.user.manager.platform.service.PlatformMerchantUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 平台端空间用户查询入口。
 */
@RestController
@RequestMapping("platform/merchant-space")
public class PlatformMerchantSpaceUserController {

    @Autowired
    private PlatformMerchantUserService merchantUserService;

    @GetMapping("{spaceId}/users")
    public R<List<MerchantUserBrief>> listUsers(@PathVariable Integer spaceId) {
        return merchantUserService.listBySpaceId(spaceId);
    }
}
