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

    Map<String,String> getHeaders();

}
