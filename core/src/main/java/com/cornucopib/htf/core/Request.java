package com.cornucopib.htf.core;

import com.cornucopib.htf.support.AuthenticationEnum;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.util.Map;

/**
 * 请求主持类.
 *
 * @author cornucopib
 * @since 2022/4/17
 */
public class Request {
    private URI uri;
    private String url;
    private Map<String, String> queryParams;
    private Object[] paths;
    private Map<String, String> httpHeaders;
    private AuthenticationEnum authenticationEnum;
    private Object body;
    private MultiValueMap<String, String> bodyMap;
    private HttpMethod method;

    public Request(HttpMethod method, URI uri) {
        this.method = method;
        this.uri = uri;
    }

    public Request(HttpMethod method, String url) {
        this.method = method;
        this.url = url;
    }

    public Request(HttpMethod method, String url, Map<String, String> queryParams) {
        this(method, url, queryParams, null);
    }

    public Request(HttpMethod method, String url, Object[] paths) {
        this(method, url, null, paths);
    }

    public Request(HttpMethod method, String url, Map<String, String> queryParams, Object[] paths) {
        this.method = method;
        this.url = url;
        this.queryParams = queryParams;
        this.paths = paths;
    }

    public static  RequestBuilder method(HttpMethod method, URI uri) {
        return new DefaultRequestBuilder(method, uri);
    }

    public static  RequestBuilder method(HttpMethod method, String url) {
        return new DefaultRequestBuilder(method, url);
    }

    public static  RequestBuilder method(HttpMethod method, String url, Map<String, String> queryParams) {
        return new DefaultRequestBuilder(method, url, queryParams);
    }

    public static  RequestBuilder method(HttpMethod method, String url, Object[] paths) {
        return new DefaultRequestBuilder(method, url, paths);
    }

    public static  RequestBuilder method(HttpMethod method, String url, Map<String, String> queryParams, Object[] paths) {
        return new DefaultRequestBuilder(method, url, queryParams, paths);
    }

    public URI getUri() {
        return uri;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public Object[] getPaths() {
        return paths;
    }

    public Map<String, String> getHttpHeaders() {
        return httpHeaders;
    }

    public void setHttpHeaders(Map<String, String> httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    public AuthenticationEnum getAuthenticationEnum() {
        return authenticationEnum;
    }

    public Object getBody() {
        return body;
    }

    public MultiValueMap<String, String> getBodyMap() {
        return bodyMap;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public void setBodyMap(MultiValueMap<String, String> bodyMap) {
        this.bodyMap = bodyMap;
    }

    public HttpMethod getMethod() {
        return method;
    }


}
