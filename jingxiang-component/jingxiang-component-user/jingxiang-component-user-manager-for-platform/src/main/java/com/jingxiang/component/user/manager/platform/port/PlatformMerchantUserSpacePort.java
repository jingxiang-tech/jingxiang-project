package com.jingxiang.component.user.manager.platform.port;

import java.util.List;
import java.util.Optional;

/**
 * 平台用户空间关系端口。
 */
public interface PlatformMerchantUserSpacePort {

    List<Integer> listSpaceIds(Integer userId);

    List<Integer> listUserIds(Integer spaceId);

    List<UserSpace> listByUserId(Integer userId);

    Optional<String> findMerchantNo(Integer userId);

    void replace(Integer userId, Merchant merchant, List<MerchantSpace> spaces, String operator, boolean administrator);

    void ensure(Integer userId, Merchant merchant, MerchantSpace space, String operator, boolean administrator);

    void remove(Integer userId);

    void restore(Integer userId, List<UserSpace> spaces, String operator);

    /**
     * 中立商户快照。
     */
    record Merchant(String merchantNo, String merchantName) {
    }

    /**
     * 中立空间快照。
     */
    record MerchantSpace(Integer spaceId, String spaceName) {
    }

    /**
     * 用户空间关系快照。
     */
    record UserSpace(
            Integer spaceId,
            String spaceName,
            String merchantNo,
            String merchantName,
            boolean administrator) {
    }
}
