package com.jingxiang.component.user.jwt;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * JWT 自动装配：绑定 yml → JwtProperties，并提供默认 JwtCodec
 * <p>
 * 各端只需再声明 {@link SessionSupport}（及可选 {@link SessionValidator}），
 * 业务侧统一使用 {@link SessionUtil}。
 *
 * @author chenjw
 */
@AutoConfiguration
@EnableConfigurationProperties(JwtProperties.class)
@ConditionalOnProperty(prefix = "jingxiang.component.user.jwt", name = "secret")
public class JwtAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JwtCodec jwtCodec(JwtProperties jwtProperties) {
        return new JwtCodec(jwtProperties);
    }
}
