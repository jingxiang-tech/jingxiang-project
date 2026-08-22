package com.jingxiang.component.user.admin.platform.model;

import com.jingxiang.commons.model.dto.BaseQuery;
import lombok.Getter;
import lombok.Setter;

/**
 * 商户用户列表查询参数。
 */
@Getter
@Setter
public class MerchantUserQuery extends BaseQuery {

    /** 商户号 */
    private String mctNo;

    /** 账户、姓名或手机号关键词 */
    private String keyword;
}
