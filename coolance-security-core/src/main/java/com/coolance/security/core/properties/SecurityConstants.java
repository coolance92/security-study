/**
 * 
 */
package com.coolance.security.core.properties;

/**
 * @ClassName SecurityProperties
 * @Description 常量类
 * @Author Coolance
 * @Version
 * @Date 2019/8/27 22:27
 */
public interface SecurityConstants {
	
	/**
	 * 默认的处理验证码的url前缀
	 */
	String DEFAULT_VALIDATE_CODE_URL_PREFIX = "/code/";
	/**
	 * 当请求需要身份认证时，默认跳转的url
	 * 
	 */
	String DEFAULT_AUTHENTICATION_URL = "/authentication/require";
	/**
	 * 默认的用户名密码登录请求处理url
	 */
	String DEFAULT_LOGIN_PROCESSING_URL_FORM = "/authentication/form";
	/**
	 * 默认的手机验证码登录请求处理url
	 */
	String DEFAULT_LOGIN_PROCESSING_URL_MOBILE = "/authentication/mobile";
	/**
	 * 默认的OPENID登录请求处理url
	 */
	String DEFAULT_SIGN_IN_PROCESSING_URL_OPENID = "/authentication/openid";
	/**
	 * 默认登录页面
	 */
	String DEFAULT_LOGIN_PAGE_URL = "/coolance-signIn.html";
	/**
	 * 验证图片验证码时，http请求中默认的携带图片验证码信息的参数的名称
	 */
	String DEFAULT_PARAMETER_NAME_CODE_IMAGE = "imageCode";
	/**
	 * 验证短信验证码时，http请求中默认的携带短信验证码信息的参数的名称
	 */
	String DEFAULT_PARAMETER_NAME_CODE_SMS = "smsCode";
	/**
	 * 发送短信验证码 或 验证短信验证码时，传递手机号的参数的名称
	 */
	String DEFAULT_PARAMETER_NAME_MOBILE = "mobile";
	/**
	 * openid参数名
	 */
	String DEFAULT_PARAMETER_NAME_OPEN_ID = "openId";
	/**
	 * providerId参数名
	 */
	String DEFAULT_PARAMETER_NAME_PROVIDER_ID = "providerId";
	/**
	 * session失效默认的跳转地址
	 */
	String DEFAULT_SESSION_INVALID_URL = "/session/invalid";
	/**
	 * 获取第三方用户信息的url
	 */
	String DEFAULT_SOCIAL_USER_INFO_URL = "/social/user";

}
