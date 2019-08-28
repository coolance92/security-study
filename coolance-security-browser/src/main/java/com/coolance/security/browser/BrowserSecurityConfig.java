package com.coolance.security.browser;


import com.coolance.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.coolance.core.properties.SecurityProperties;
import com.coolance.core.validate.code.ValidateCodeFilter;
import com.coolance.core.validate.code.sms.SmsCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * @ClassName BrowserSecurityConfig
 * @Description 浏览器访问安全配置类
 * @Author Coolance
 * @Version
 * @Date 2019/8/19 12:48
 */
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private AuthenticationSuccessHandler coolanceAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler coolanceAuthenticationFailureHandler;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
//        tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
        validateCodeFilter.setAuthenticationFailureHandler(coolanceAuthenticationFailureHandler);
        validateCodeFilter.setSecurityProperties(securityProperties);
        validateCodeFilter.afterPropertiesSet();

        SmsCodeFilter smsCodeFilter = new SmsCodeFilter();
        smsCodeFilter.setAuthenticationFailureHandler(coolanceAuthenticationFailureHandler);
        smsCodeFilter.setSecurityProperties(securityProperties);
        smsCodeFilter.afterPropertiesSet();
        //使用默认方式登录
        //http.httpBasic();
        //使用表单登录
        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                //自定义登录页面
                .loginPage("/authentication/require")
                //自定义表单提交请求
                .loginProcessingUrl("/authentication/form")
                .successHandler(coolanceAuthenticationSuccessHandler)
                .failureHandler(coolanceAuthenticationFailureHandler)
                .and()
                .rememberMe()
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                .tokenRepository(persistentTokenRepository())
                .userDetailsService(userDetailsService)
                .and()
                //授权相关配置
                .authorizeRequests()
                //设置白名单
                .antMatchers("/authentication/require",
                        securityProperties.getBrowser().getLoginPage(),
                        "/code/*").permitAll()
                //任何请求
                .anyRequest()
                //都需要认证
                .authenticated()
                .and()
                //跨域请求伪造
                .csrf().disable()
        .apply(smsCodeAuthenticationSecurityConfig);
    }
}
