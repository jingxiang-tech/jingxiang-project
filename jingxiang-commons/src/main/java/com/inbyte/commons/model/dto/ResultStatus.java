package com.inbyte.commons.model.dto;

/**
 * 接口响应状态
 * @author chenjw
 * @date 2016/10/20
 */
public enum ResultStatus {

    // 业务处理成功, 付款与退款结果表示处理完成
    Success(200, "业务处理成功"),
    Created(201, "业务处理成功, 不需要客户端做出响应"),
    Accepted(202, "业务处理成功, 等待服务器通知处理"),
    Refunding(220, "退款处理中"),
    // 拉起设置支付密码页面
    Payment_Code_Not_Set(230, "未设置支付密码"),
    // 提示message消息内容
    Failure(400, "业务处理失败"),
    // 用户身份验证不通过, 拉起登录窗口
    Unauthorized(401, "用户未登录或会话过期, 请重新登录"),
    Unregistered(4010, "未注册绑定手机号"),
    Authentication(4011, "实名认证"),
    App_Token_Unavailable(4012, "AppToken失效"),
    Did_Not_Choose_Venue(4014, "未选择门店错误"),
    Forbidden(403, "无权限访问"),
    Not_Found(404, "无效指令, 或接口不存在"),
    Method_Not_Allowed(405, "程序版本可能过旧, 请更新版本或强制刷新页面试下"),
    Precondition_Failed(412, "前提条件失败"),
    Payload_Too_Large(413, "请求数据过大, 请处理后重新提交"),
    Upgrade_Required(426, "需要升级版本"),
    Precondition_Required(428, "需要条件验证"),
    // 显示系统异常占位图
    Internal_Server_Error(500, "服务器内部异常"),
    // 由于临时的服务器维护或者过载, 服务器当前无法处理请求。这个状况是暂时的, 并且将在一段时间以后恢复。
    Service_Unavailable(503, "服务器繁忙, 已通知管理员, 请5秒后重试"),

    VersionNotSupported(505, "App版本过期"),

    ;

    public final int code;
    public final String name;

    ResultStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
