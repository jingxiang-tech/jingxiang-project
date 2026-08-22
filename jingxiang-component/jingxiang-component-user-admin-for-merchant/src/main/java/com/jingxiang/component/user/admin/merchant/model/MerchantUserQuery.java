package com.jingxiang.component.user.admin.merchant.model;

import com.jingxiang.commons.model.dto.BasePage;
import lombok.Getter;
import lombok.Setter;

/**
 * 商户成员分页查询参数。
 *
 * @author chenjw
 */
@Getter
@Setter
public class MerchantUserQuery extends BasePage {

    /** 查询关键字。 */
    private String keyword;
}
