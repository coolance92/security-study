/**
 *
 */
package com.coolance.security.app.validate.code.impl;

import com.coolance.security.core.validate.code.ValidateCode;
import com.coolance.security.core.validate.code.ValidateCodeException;
import com.coolance.security.core.validate.code.ValidateCodeRepository;
import com.coolance.security.core.validate.code.ValidateCodeType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedisValidateCodeRepository
 * @Description 基于redis的验证码存取器，避免由于没有session导致无法存取验证码的问题
 * @Author Coolance
 * @Version
 * @Date 2019/9/4 10:10
 */
@Component
public class RedisValidateCodeRepository implements ValidateCodeRepository {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * (non-Javadoc)
     *
     * @see ValidateCodeRepository
     * #save(org.springframework.web.context.request.ServletWebRequest,
     * com.coolance.security.core.validate.code.ValidateCode,
     * com.coolance.security.core.validate.code.ValidateCodeType)
     */
    @Override
    public void save(ServletWebRequest request, ValidateCode code, ValidateCodeType type) {
        redisTemplate.opsForValue().set(buildKey(request, type), code, 30, TimeUnit.MINUTES);
    }

    /**
     * (non-Javadoc)
     *
     * @see ValidateCodeRepository
     * #get(org.springframework.web.context.request.ServletWebRequest,
     * com.coolance.security.core.validate.code.ValidateCodeType)
     */
    @Override
    public ValidateCode get(ServletWebRequest request, ValidateCodeType type) {
        Object value = redisTemplate.opsForValue().get(buildKey(request, type));
        if (value == null) {
            return null;
        }
        return (ValidateCode) value;
    }

    /**
     * (non-Javadoc)
     *
     * @see ValidateCodeRepository
     * #remove(org.springframework.web.context.request.ServletWebRequest,
     * com.coolance.security.core.validate.code.ValidateCodeType)
     */
    @Override
    public void remove(ServletWebRequest request, ValidateCodeType type) {
        redisTemplate.delete(buildKey(request, type));
    }

    /**
     * @param request
     * @param type
     * @return
     */
    private String buildKey(ServletWebRequest request, ValidateCodeType type) {
        String deviceId = request.getHeader("deviceId");
        if (StringUtils.isBlank(deviceId)) {
            throw new ValidateCodeException("请在请求头中携带deviceId参数");
        }
        return "code:" + type.toString().toLowerCase() + ":" + deviceId;
    }

}
