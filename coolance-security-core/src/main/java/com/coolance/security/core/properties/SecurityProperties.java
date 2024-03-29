package com.coolance.security.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName SecurityProperties
 * @Description 系统配置属性类
 * @Author Coolance
 * @Version
 * @Date 2019/8/20 10:31
 */
@Data
@ConfigurationProperties(prefix = "coolance.security")
public class SecurityProperties {

    private BrowserProperties browser = new BrowserProperties();

    private ValidateCodeProperties code = new ValidateCodeProperties();

    private SocialProperties social = new SocialProperties();

    private OAuth2Properties oauth2 = new OAuth2Properties();


}
