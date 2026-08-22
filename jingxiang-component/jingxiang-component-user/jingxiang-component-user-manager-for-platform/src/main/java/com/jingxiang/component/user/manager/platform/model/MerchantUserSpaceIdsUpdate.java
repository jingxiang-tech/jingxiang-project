package com.jingxiang.component.user.manager.platform.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 用户空间权限保存参数。
 */
@Getter
@Setter
public class MerchantUserSpaceIdsUpdate {

    /** 业务空间 ID 列表 */
    private List<Integer> spaceIds;
}
