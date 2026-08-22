package com.jingxiang.component.user.jwt;

import java.util.List;

/**
 * 带角色列表的会话（wanyu 等）
 *
 * @author chenjw
 */
public interface RoleSession {

    List<String> getRoleCodes();
}
