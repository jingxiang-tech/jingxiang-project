package com.inbyte.component.common.dict;

import com.inbyte.commons.model.dto.Dict;
import com.inbyte.commons.util.SpringContextUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 字典服务
 *
 * @author chenjw
 * @date 2023-12-17 22:12:01
 **/
public class DictUtil {

    /**
     * 获取字典信息
     *
     * @param dictName
     * @return
     */
    public static Map<String, String> getDict(String dictName) {
        return SpringContextUtil.getBean(DictService.class).getDict(dictName);
    }

    /**
     * 获取字典
     *
     * @param dictClass
     * @return
     */
    public static Map<String, String> getDict(Class dictClass) {
        return getDict(dictClass.getSimpleName().toLowerCase().replace("dict", ""));
    }

    /**
     * 获取字典名称
     *
     * @param dictName
     * @param code
     * @return
     */
    public static String getName(String dictName, Serializable code) {
        return getDict(dictName).get(String.valueOf(code));
    }

    /**
     * 获取字典名称
     * 多元素时, 以逗号分隔
     *
     * @param dictName
     * @param codes
     * @return
     */
    public static String getName(String dictName, Collection codes) {
        if (codes == null || codes.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object code : codes) {
            sb.append(getDict(dictName).get(String.valueOf(code))).append(",");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }
    /**
     * 获取字典名称
     *
     * @param dictName
     * @param code
     * @return
     */
    public static String getName(Class dictName, Serializable code) {
        return getDict(dictName).get(String.valueOf(code));
    }

    /**
     * 获取字典列表
     *
     * @param clz
     * @return
     */
    public static List<Dict> getListDict(Class clz) {
        return SpringContextUtil.getBean(DictService.class).getListDict(clz);
    }
}
