package com.inbyte.component.common.basic.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inbyte.component.common.basic.model.InbyteSystemConfigPo;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.cache.decorators.FifoCache;

/**
 * 系统配置
 * <p>
 * 表名 inbyte_system_config
 *
 * @author chenjw
 * @date 2024-03-11 11:07:53
 */
@CacheNamespace(
        eviction = FifoCache.class,
        flushInterval = 60000, // 1分钟刷新
        size = 1024,
        readWrite = false
)
public interface CommonSystemConfigMapper extends BaseMapper<InbyteSystemConfigPo> {

    @Select("SELECT `value` " +
            "  FROM inbyte_system_config" +
            " WHERE `key` = #{key}")
    String getValue(@Param("key") String key);
}
