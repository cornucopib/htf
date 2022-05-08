package com.cornucopib.htf.support;

import org.springframework.http.HttpHeaders;

import java.util.Map;

/**
 * 验证方式策略.
 *
 * @author cornucopib
 * @since 2022/4/17
 */
public interface AuthenticationStrategy {
    /**
     * 获取headers.
     *
     * @return 请求头
     */
    Map<String, String> getHeaders();

    /**
     * 策略标识.
     *
     * @return 策略id
     */
    String strategyId();

}
