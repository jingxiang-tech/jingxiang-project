package com.jingxiang.component.user.manager.merchant.port;

/**
 * 商户目录端口，由宿主系统的业务库适配器实现。
 *
 * @author chenjw
 */
public interface MerchantDirectoryPort {

    /**
     * 查询商户目录信息。
     */
    MerchantDirectory findByMerchantNo(String merchantNo);

    /**
     * 幂等标记商户已完成初始化引导。
     */
    void markInitGuideDone(String merchantNo, String operator);

    /**
     * 商户目录快照。
     *
     * @param merchantNo 商户号
     * @param merchantName 商户名称
     * @param initGuideDone 是否完成初始化引导
     * @param maxEmployeeCount 员工数量上限
     */
    record MerchantDirectory(
            String merchantNo,
            String merchantName,
            Integer initGuideDone,
            Integer maxEmployeeCount) {
    }
}
