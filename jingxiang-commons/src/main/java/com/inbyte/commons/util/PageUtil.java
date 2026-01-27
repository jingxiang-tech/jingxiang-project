package com.inbyte.commons.util;

import com.inbyte.commons.model.dto.BasePage;
import com.github.pagehelper.PageHelper;

/**
 * 分页工具
 *
 * @author chenjw
 * @date 2017/9/4 下午6:25
 */
public class PageUtil {

    /**
     * 分页方法,默认不count查询
     *
     * @param pageNum  当前页
     * @param pageSize 页数大小
     */
    public static void startPage(int pageNum, int pageSize) {
        PageUtil.startPage(pageNum, pageSize, false);
    }

    /**
     * 分页方法
     *
     * @param pageNum  当前页
     * @param pageSize 页数大小
     * @param count    是否count查询
     */
    public static void startPage(int pageNum, int pageSize, boolean count) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize > 100 ? 100 : pageSize;
        PageHelper.startPage(pageNum, pageSize, count);
    }

    /**
     * 分页方法, 默认不count查询
     *
     * @param obj 参数对象, 必须继承BasePage类, 否则默认查询第一页, 页数10行
     */
    public static void startPage(BasePage obj) {
        PageUtil.startPage(obj, true);
    }

    /**
     * 分页方法
     *
     * @param obj   参数对象, 必须继承BasePage类, 否则默认查询第一页, 页数10行
     * @param count 是否count查询
     */
    public static void startPage(BasePage obj, boolean count) {

        if (obj == null || obj == null) {
            PageUtil.startPage(1, 10);
            return;
        }

        PageUtil.startPage(obj.getPageNum(), obj.getPageSize(), count);
//        try {
//            if (obj == null || obj == null) {
//                PageUtil.startPage(1, 10);
//                return;
//            }
//
//            PageUtil.startPage(obj.getPageNum(), obj.getPageSize());
//            Class<?> clz = obj.getClass();
//            boolean assignableFrom = BasePage.class.isAssignableFrom(clz);
//            if (!assignableFrom) {
//                PageUtil.startPage(1, 10, count);
//                return;
//            }
//
//            Method getMethod = clz.getMethod("getPageNum");
//            Object invokePageNum = getMethod.invoke(obj);
//            int pageNum, pageSize;
//            if (invokePageNum == null) {
//                pageNum = 1;
//            } else {
//                pageNum = Integer.parseInt(getMethod.invoke(obj).toString());
//            }
//            getMethod = clz.getMethod("getPageSize");
//            Object invokePageSize = getMethod.invoke(obj);
//            if (invokePageSize == null) {
//                pageSize = 10;
//            } else {
//                pageSize = Integer.parseInt(getMethod.invoke(obj).toString());
//            }
//            PageUtil.startPage(pageNum, pageSize, count);
//        } catch (Exception e) {
//            // 异常情况默认第一页访问10行数据
//            e.printStackTrace();
//            PageUtil.startPage(1, 10);
//        }
    }
}
