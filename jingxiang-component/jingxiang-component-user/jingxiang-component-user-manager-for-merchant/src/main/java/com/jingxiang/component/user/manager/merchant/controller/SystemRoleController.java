package com.jingxiang.component.user.manager.merchant.controller;

import com.jingxiang.commons.model.dto.Dict;
import com.jingxiang.commons.model.dto.R;
import com.jingxiang.component.user.manager.common.dict.MemberTypeEnum;
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
        List<Dict> dicts = Arrays.stream(MemberTypeEnum.values()).map(memberType -> {
            Dict dict = new Dict();
            dict.setCode(memberType.code);
            dict.setName(memberType.name);
            return dict;
        }).toList();
        return R.ok(dicts);
    }
}
