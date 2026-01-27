package com.inbyte.commons.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * Spring 环境工具
 *
 * @author chenjw
 */
@Configuration
@Lazy(false)
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    /**
     * 开发环境
     */
    public static final String Dev = "dev";
    /**
     * 测试环境
     */
    public static final String Test = "test";
    /**
     * 正式环境
     */
    public static final String Prod = "prod";

    @Override
    public void setApplicationContext(ApplicationContext paramApplicationContext)
            throws BeansException {
        context = paramApplicationContext;
    }

    public static <T> T getBean(String paramString, Class<T> paramClass) {
        return context.getBean(paramString, paramClass);
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static <T> T getBean(Class<T> paramClass) {
        return context.getBean(paramClass);
    }

    /**
     * 是否开发环境
     *
     * @return
     */
    public static boolean devEnv() {
        return Dev.equals(context.getEnvironment().getActiveProfiles()[0]);
    }

    /**
     * 是否开发环境
     *
     * @return
     */
    public static boolean testEnv() {
        return Test.equals(context.getEnvironment().getActiveProfiles()[0]);
    }

    /**
     * 是否正式环境
     *
     * @return
     */
    public static boolean prodEnv() {
        return Prod.equals(context.getEnvironment().getActiveProfiles()[0]);
    }

    /**
     * 获取应用名称
     *
     * @return
     */
    public static String getApplicationName() {
        return context.getEnvironment().getProperty("spring.application.name");
    }

    /**
     * 获取应用环境
     *
     * @return
     */
    public static String getActiveProfile() {
        return context.getEnvironment().getActiveProfiles()[0];
    }

    /**
     * 获取应用环境名称
     *
     * @return
     */
    public static String getActiveProfileName() {
        String activeProfile = getActiveProfile();
        if (Prod.equals(activeProfile)) {
            return "生产环境";
        } else if (Dev.equals(activeProfile)) {
            return "开发环境";
        } else if (Test.equals(activeProfile)) {
            return "测试环境";
        } else {
            return "未知环境";
        }
    }

}
