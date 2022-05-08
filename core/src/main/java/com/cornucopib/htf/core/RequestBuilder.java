package com.cornucopib.htf.core;

import com.cornucopib.htf.support.AuthenticationEnum;
import com.cornucopib.htf.support.AuthenticationStrategy;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.util.Map;

/**
 * @author cornucopib
 * @since 2022/4/17
 */
public interface RequestBuilder {

    RequestBuilder authentication(AuthenticationEnum authenticationEnum);

    RequestBuilder authentication(AuthenticationStrategy authenticationStrategy);

    RequestBuilder buildHeader(Map<String, String> headersMap);

    RequestBuilder buildBody(Object object);

    RequestBuilder buildBody(MultiValueMap<String, String> bodyMap);

    RequestBuilder build();

     <T> ResponseEntity<T> send(Class<T> responseType);
}
