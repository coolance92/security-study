/**
 * 
 */
package com.coolance.security.core.properties;

import lombok.Data;

/**
 * @ClassName SessionProperties
 * @Description session配置属性类
 * @Author Coolance
 * @Version
 * @Date 2019/9/3 10:11
 */
@Data
public class SessionProperties {
	
	/**
	 * 同一个用户在系统中的最大session数，默认1
	 */
	private int maximumSessions = 1;
	/**
	 * 达到最大session时是否阻止新的登录请求，默认为false，不阻止，新的登录会将老的登录失效掉
	 */
	private boolean maxSessionsPreventsLogin;
	/**
	 * session失效时跳转的地址
	 */
	private String sessionInvalidUrl = SecurityConstants.DEFAULT_SESSION_INVALID_URL;
}
