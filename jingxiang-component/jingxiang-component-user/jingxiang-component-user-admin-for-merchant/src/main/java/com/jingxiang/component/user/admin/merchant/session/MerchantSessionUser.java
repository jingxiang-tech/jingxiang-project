package com.jingxiang.component.user.admin.merchant.session;

import com.jingxiang.component.user.jwt.AdminSession;
import com.jingxiang.component.user.jwt.BaseSessionUser;
import com.jingxiang.component.user.jwt.SpaceSession;

/**
 * 商户后台会话。
 *
 * @author chenjw
 */
public class MerchantSessionUser extends BaseSessionUser implements SpaceSession, AdminSession {

    /**
     * 是否当前空间管理员。
     */
    private Integer admin;

    /**
     * 业务空间 ID。
     */
    private Integer spaceId;

    /**
     * 业务空间名称。
     */
    private String spaceName;

    @Override
    public Integer getAdmin() {
        return admin;
    }

    public void setAdmin(Integer admin) {
        this.admin = admin;
    }

    @Override
    public Integer getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Integer spaceId) {
        this.spaceId = spaceId;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }
}
