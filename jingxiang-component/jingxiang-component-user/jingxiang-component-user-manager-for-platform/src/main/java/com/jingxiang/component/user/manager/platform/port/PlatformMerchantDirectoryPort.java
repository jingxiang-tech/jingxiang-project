package com.jingxiang.component.user.manager.platform.port;

import java.util.List;
import java.util.Optional;

/**
 * 平台商户与空间目录端口。
 */
public interface PlatformMerchantDirectoryPort {

    Optional<Merchant> findMerchant(String merchantNo);

    Optional<MerchantSpace> findSpace(Integer spaceId);

    List<MerchantSpace> listSpaces(String merchantNo);

    /**
     * 中立商户快照。
     */
    record Merchant(String merchantNo, String merchantName) {
    }

    /**
     * 中立业务空间快照。
     */
    record MerchantSpace(Integer spaceId, String merchantNo, String spaceName) {
    }
}
