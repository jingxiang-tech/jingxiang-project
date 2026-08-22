package com.jingxiang.component.user.admin.merchant.port;

import java.util.List;

/**
 * 商户用户与业务空间关系端口。
 * <p>
 * 单个方法内的写操作由宿主业务库本地事务保证，不跨统一用户库开启事务。
 *
 * @author chenjw
 */
public interface MerchantUserSpacePort {

    /**
     * 查询用户可用空间。
     */
    List<UserSpace> listByUserId(Integer userId);

    /**
     * 查询并校验指定商户下的业务空间。
     */
    List<MerchantSpace> listValidSpaces(String merchantNo, List<Integer> spaceIds);

    /**
     * 统计商户员工数。
     */
    int countDistinctUsers(String merchantNo);

    /**
     * 幂等绑定空间。
     */
    List<UserSpace> bindSpaces(Integer userId, String merchantNo, String merchantName,
                               List<MerchantSpace> spaces, String operator);

    /**
     * 删除指定空间关系，用于新增编排失败后的精确补偿。
     */
    void removeSpaces(Integer userId, String merchantNo, List<Integer> spaceIds);

    /**
     * 删除用户在商户下的全部空间关系，并返回删除前快照以便补偿。
     */
    List<UserSpace> removeByUserAndMerchant(Integer userId, String merchantNo);

    /**
     * 幂等恢复空间关系，用于跨库编排失败后的补偿。
     */
    void restoreSpaces(Integer userId, List<UserSpace> spaces, String operator);

    /**
     * 查询用户指定空间关系。
     */
    UserSpace findByUserAndSpace(Integer userId, Integer spaceId);

    /**
     * 业务空间。
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
            Integer admin) {
    }
}
