package com.cornucopib.htf.support;

import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.Map;

/**
 * Cookie验证策略.
 *
 * @author cornucopib
 * @since 2022/4/17
 */
public class CookieAuthenticationStrategy implements AuthenticationStrategy {
    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> httpHeaders = new HashMap<>();
        httpHeaders.put("Cookie", "cookie");
        return httpHeaders;
    }

    @Override
    public String strategyId() {
        return AuthenticationEnum.COOKIE.name();
    }
}
