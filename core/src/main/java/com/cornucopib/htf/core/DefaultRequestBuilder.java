package com.cornucopib.htf.core;

import com.cornucopib.htf.support.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * 默认的请求构建.
 *
 * @author cornucopib
 * @since 2022/4/17
 */
public class DefaultRequestBuilder implements RequestBuilder {

    private Request requestHolder;
    private RequestEntity requestEntity;


    public DefaultRequestBuilder(HttpMethod method, URI uri) {
        requestHolder = new Request(method, uri);
    }

    public DefaultRequestBuilder(HttpMethod method, String url) {
        requestHolder = new Request(method, wrapURI(url, null, null));
    }

    public DefaultRequestBuilder(HttpMethod method, String url, Map<String, String> queryParams, Object[] paths) {
        requestHolder = new Request(method, wrapURI(url, queryParams, paths));
    }

    public DefaultRequestBuilder(HttpMethod method, String url, Map<String, String> queryParams) {
        requestHolder = new Request(method, wrapURI(url, queryParams, null));
    }

    public DefaultRequestBuilder(HttpMethod method, String url, Object[] paths) {
        requestHolder = new Request(method, wrapURI(url, null, paths));
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
        return authentication(AuthenticationFactory.getStrategy(authenticationEnum));
    }


    @Override
    public RequestBuilder authentication(AuthenticationStrategy authenticationStrategy) {
        Map<String, String> httpHeaders = authenticationStrategy.getHeaders();
        mergeHeaders(requestHolder, httpHeaders);
        return this;
    }

    private void mergeHeaders(Request requestHolder, Map<String, String> headers) {
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
    public RequestBuilder build() {
        RequestEntity requestEntity;
        HttpMethod method = this.requestHolder.getMethod();
        if (HttpMethod.GET == method) {
            requestEntity = RequestEntity.method(this.requestHolder.getMethod(), this.requestHolder.getUri())
                    .headers(mapToHttpHeaders(this.requestHolder.getHttpHeaders()))
                    .build();
        }
        requestEntity = RequestEntity.method(this.requestHolder.getMethod(), this.requestHolder.getUri())
                .headers(mapToHttpHeaders(this.requestHolder.getHttpHeaders()))
                .body(this.requestHolder.getBody());
        return this;
    }

    @Override
    public <T> ResponseEntity<T> send(Class<T> responseType) {
        RestTemplate restTemplate = RestTemplateHolder.INSTANCE.getRestTemplate();
        ResponseEntity<T> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(this.requestEntity, responseType);
        } catch (RestClientException restClientException) {
            restClientException.printStackTrace();
        }
        return responseEntity;
    }

}
