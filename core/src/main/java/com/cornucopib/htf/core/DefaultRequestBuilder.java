package com.cornucopib.htf.core;

import com.cornucopib.htf.support.AuthenticationEnum;
import com.cornucopib.htf.support.AuthenticationStrategy;
import com.cornucopib.htf.support.CookieAuthenticationStrategy;
import com.cornucopib.htf.support.SoaAuthenticationStrategy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cornucopib
 * @since 2022/4/17
 */
public class DefaultRequestBuilder implements RequestBuilder {

    private RequestHolder requestHolder;


    public DefaultRequestBuilder(HttpMethod method, URI uri) {
        requestHolder = new RequestHolder(method, uri);
    }

    public DefaultRequestBuilder(HttpMethod method, String url) {
        requestHolder = new RequestHolder(method, wrapURI(url, null, null));
    }

    public DefaultRequestBuilder(HttpMethod method, String url, Map<String, String> queryParams, Object[] paths) {
        requestHolder = new RequestHolder(method, wrapURI(url, queryParams, paths));
    }

    public DefaultRequestBuilder(HttpMethod method, String url, Map<String, String> queryParams) {
        requestHolder = new RequestHolder(method, wrapURI(url, queryParams, null));
    }

    public DefaultRequestBuilder(HttpMethod method, String url, Object[] paths) {
        requestHolder = new RequestHolder(method, wrapURI(url, null, paths));
    }

    private URI wrapURI(String url, Map<String, String> queryParams, Object[] paths) {
        if (paths != null) {
            return UriComponentsBuilder.fromHttpUrl(url)
                    .queryParams(mapToMultiValueMap(queryParams))
                    .encode()
                    .build(paths);
        } else if (queryParams != null) {
            return UriComponentsBuilder.fromHttpUrl(url)
                    .queryParams(mapToMultiValueMap(queryParams))
                    .encode()
                    .build()
                    .toUri();
        } else {
            return UriComponentsBuilder.fromHttpUrl(url).encode().build().toUri();
        }
    }

    private MultiValueMap<String, String> mapToMultiValueMap(Map<String, String> map) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                multiValueMap.add(key, value);
            }
        }
        return multiValueMap;
    }

    private HttpHeaders mapToHttpHeaders(Map<String, String> map) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                httpHeaders.set(key, value);
            }
        }
        return httpHeaders;
    }

    @Override
    public RequestBuilder authentication(AuthenticationEnum authenticationEnum) {
        if (AuthenticationEnum.COOKIE == authenticationEnum) {
            return authentication(new CookieAuthenticationStrategy());
        } else if (AuthenticationEnum.SOA == authenticationEnum) {
            return authentication(new SoaAuthenticationStrategy());
        }
        return this;
    }


    @Override
    public RequestBuilder authentication(AuthenticationStrategy authenticationStrategy) {
        Map<String, String> httpHeaders = authenticationStrategy.getHeaders();
        mergeHeaders(requestHolder, httpHeaders);
        return this;
    }

    private void mergeHeaders(RequestHolder requestHolder, Map<String, String> headers) {
        Map<String, String> sourceHeaders = requestHolder.getHttpHeaders();
        if (sourceHeaders != null) {
            Map<String, String> targetHeaders = new HashMap<>();
            for (Map.Entry<String, String> sourceEntry : sourceHeaders.entrySet()) {
                String sourceKey = sourceEntry.getKey();
                String sourceValue = sourceEntry.getValue();
                targetHeaders.put(sourceKey, sourceValue);
            }
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    targetHeaders.put(key, value);
                }
            }
            requestHolder.setHttpHeaders(targetHeaders);
        } else {
            requestHolder.setHttpHeaders(headers);
        }
    }

    @Override
    public RequestBuilder buildHeader(Map<String, String> headersMap) {
        mergeHeaders(this.requestHolder, headersMap);
        return this;
    }

    @Override
    public RequestBuilder buildBody(Object object) {
        this.requestHolder.setBody(object);
        return this;
    }


    @Override
    public RequestBuilder buildBody(MultiValueMap<String, String> bodyMap) {
        this.requestHolder.setBodyMap(bodyMap);
        return this;
    }


    @Override
    public RequestEntity build() {
        HttpMethod method = this.requestHolder.getMethod();
        if (HttpMethod.GET == method) {
            return RequestEntity.method(this.requestHolder.getMethod(), this.requestHolder.getUri())
                    .headers(mapToHttpHeaders(this.requestHolder.getHttpHeaders()))
                    .build();
        }
        return RequestEntity.method(this.requestHolder.getMethod(), this.requestHolder.getUri())
                .headers(mapToHttpHeaders(this.requestHolder.getHttpHeaders()))
                .body(this.requestHolder.getBody());
    }
}
