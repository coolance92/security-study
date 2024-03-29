package com.coolance.security.browser;


import com.coolance.security.core.authentication.AbstractChannelSecurityConfig;
import com.coolance.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.coolance.security.core.authorize.AuthorizeConfigManager;
import com.coolance.security.core.properties.SecurityProperties;
import com.coolance.security.core.validate.code.ValidateCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * @ClassName BrowserSecurityConfig
 * @Description 浏览器访问安全配置类
 * @Author Coolance
 * @Version
 * @Date 2019/8/19 12:48
 */
@Configuration
public class BrowserSecurityConfig extends AbstractChannelSecurityConfig {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private SpringSocialConfigurer coolanceSocialSecurityConfigurer;

    @Autowired
    private InvalidSessionStrategy coolanceInvalidSessionStrategy;

    @Autowired
    private SessionInformationExpiredStrategy coolanceExpiredSessionStrategy;

    @Autowired
    private LogoutSuccessHandler coolanceLogoutSuccessHandler;

    @Autowired
    private AuthorizeConfigManager authorizeConfigManager;

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        applyPasswordAuthenticationConfig(http);

        //使用表单登录
        http.apply(validateCodeSecurityConfig)
                .and()
                .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                .apply(coolanceSocialSecurityConfigurer)
                .and()
                .rememberMe()
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                .tokenRepository(persistentTokenRepository())
                .userDetailsService(userDetailsService)
                .and()
                .sessionManagement()
                .invalidSessionStrategy(coolanceInvalidSessionStrategy)
                .maximumSessions(securityProperties.getBrowser().getSession().getMaximumSessions())
                .maxSessionsPreventsLogin(securityProperties.getBrowser().getSession().isMaxSessionsPreventsLogin())
                .expiredSessionStrategy(coolanceExpiredSessionStrategy)
                .and()
                .and()
                .logout()
                .logoutUrl("/signOut")
                .logoutSuccessHandler(coolanceLogoutSuccessHandler)
                .deleteCookies("JSESSIONID")
                .and()
                //跨域请求伪造
                .csrf().disable();
        authorizeConfigManager.configure(http.authorizeRequests());
    }
}
