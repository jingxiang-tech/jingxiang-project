package com.jingxiang.component.user.manager.merchant.controller;

import com.jingxiang.commons.model.dto.Dict;
import com.jingxiang.commons.model.dto.R;
import com.jingxiang.component.user.manager.common.dict.MemberRoleEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 商户后台角色接口。
 *
 * @author chenjw
 */
@RestController
@RequestMapping("system/role")
public class SystemRoleController {

    @GetMapping("dict")
    public R<List<Dict>> dict() {
        List<Dict> dicts = Arrays.stream(MemberRoleEnum.values())
                .map(role -> new Dict(role.code, role.name))
                .toList();
        return R.ok(dicts);
    }
}
