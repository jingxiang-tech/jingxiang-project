package com.inbyte.component.common.basic.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 商户实体
 *
 * @author chenjw
 * @date 2024-03-11 17:14:03
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@TableName("inbyte_merchant")
public class InbyteMerchantPo {

    /**
     * 商户号
     */
    @TableId(value = "mct_no", type = IdType.INPUT)
    private String mctNo;

    /**
     * 商户名
     */
    private String mctName;

    /**
     * 营业执照编号 
     */
    private String creditCode;

    /**
     * 企业代码类型 1：统一社会信用代码（18 位） 2：组织机构代码（9 位 xxxxxxxx-x） 3：营业执照注册号(15 位)
     */
    private CreditCodeTypeEnum creditCodeType;

    /**
     * 法人
     */
    private String legalPerson;

    /**
     * 法人微信号
     */
    private String legalPersonWeixinId;

    /**
     * 拼音名字
     */
    private String pinyinName;

    /**
     * 文件数
     */
    private Integer fileCount;

    /**
     * 文件总大小
     */
    private Integer fileSizeCount;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 手机号
     */
    private String tel;

    /**
     * Logo
     */
    private String logo;

    /**
     * 已删除
     */
    private Integer deleted;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 修改人
     */
    private String modifier;
    /**
     * 文件存储上限
     */
    private Integer fileSizeLimit;

    /**
     * 营业执照 OCR 详情
     */
    private String businessLicenceInfo;

    /**
     * 营业执照图片
     */
    private String businessLicenceImg;

    /**
     * 有效截止日期
     */
    private LocalDate expireDate;

}
