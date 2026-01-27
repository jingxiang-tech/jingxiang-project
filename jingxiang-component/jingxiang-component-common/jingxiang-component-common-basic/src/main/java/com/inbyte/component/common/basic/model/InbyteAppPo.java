package com.inbyte.component.common.basic.model;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 应用实体
 *
 * @author chenjw
 * @date 2024-04-11 15:53:23
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@TableName("inbyte_app")
public class InbyteAppPo {

    /**
      * appId
      */
    @TableId(value = "app_id", type = IdType.AUTO)
    private String appId;

    /**
      * app名称
      */
    private String appName;

    /**
      * app类型
      */
    private String appType;

    /**
      * app密钥
      */
    private String secret;

    /**
      * 商户号
      */
    private String mctNo;

    /**
     * 扩展信息
     */
    private JSONObject extJson;

    /**
      * 备注
      */
    private String remark;

    /**
     * app类型
     */
    private Long maAuditId;

    /**
     * 小程序审核中
     */
    private Integer maUnderAudit;

    /**
      * 创建时间
      */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 营位订单订阅通知
     * 出行前订通知:[{"subscribeMsgType":"PRE_TRIP_NOTICE", "templateId":"xx"},
     *             {"subscribeMsgType":"CHECK_IN_NOTICE", "templateId":"xx"},
     *             {"subscribeMsgType":"CHECK_OUT_NOTICE", "templateId":"xx"}]
     */
    private List<SubscribeMsgConfig> orderSubscribeMsgSite;

    /**
     * 门票订单订阅通知
     */
    private List<SubscribeMsgConfig> orderSubscribeMsgTicket;
}
