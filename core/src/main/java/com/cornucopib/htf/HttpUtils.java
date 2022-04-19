package com.cornucopib.htf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cornucopib.htf.core.Htf;
import com.cornucopib.htf.core.RequestHolder;
import com.cornucopib.htf.support.AuthenticationEnum;
import com.sun.jndi.toolkit.url.Uri;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author cornucopib
 * @since 2022/4/16
 */
public class HttpUtils {

    public static void main(String[] args) {
        get();
    }


    public static class MyResponseErrorHandler extends DefaultResponseErrorHandler {
        @Override
        protected boolean hasError(HttpStatus statusCode) {
            return super.hasError(statusCode);
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            HttpStatus statusCode = HttpStatus.resolve(response.getRawStatusCode());
            if (statusCode == null) {
                throw new UnknownHttpStatusCodeException(response.getRawStatusCode(), response.getStatusText(),
                        response.getHeaders(), getResponseBody(response), getCharset(response));
            }
            handleError(response, statusCode);
        }

        @Override
        protected void handleError(ClientHttpResponse response, HttpStatus statusCode) throws IOException {
            switch (statusCode.series()) {
                case CLIENT_ERROR:
                    HttpClientErrorException exp1 = new HttpClientErrorException(statusCode, response.getStatusText(), response.getHeaders(), getResponseBody(response), getCharset(response));
                    System.out.println("客户端调用异常" + exp1);
                    throw exp1;
                case SERVER_ERROR:
                    HttpServerErrorException exp2 = new HttpServerErrorException(statusCode, response.getStatusText(),
                            response.getHeaders(), getResponseBody(response), getCharset(response));
                    System.out.println("服务端调用异常" + exp2);
                    throw exp2;
                default:
                    UnknownHttpStatusCodeException exp3 = new UnknownHttpStatusCodeException(statusCode.value(), response.getStatusText(),
                            response.getHeaders(), getResponseBody(response), getCharset(response));
                    System.out.println("服务端调用异常" + exp3);
                    throw exp3;
            }
        }
    }

    public static void request() {
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory());
        restTemplate.setErrorHandler(new MyResponseErrorHandler());
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("z", "z");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("xx", "xx");
        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("b", "b");
        URI uri = UriComponentsBuilder.fromHttpUrl("http://www.baidu.com").queryParams(params).build().toUri();
        RequestEntity<Object> requestEntity = RequestEntity.method(HttpMethod.POST, uri)
                .headers(httpHeaders)
                .body(bodyMap);
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(requestEntity, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(responseEntity.getBody());
    }

    public static void get() {
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory());
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("url", "123.cn");
//        URI uri = UriComponentsBuilder.fromHttpUrl("https://tenapi.cn/whois/").queryParams(params).build().toUri();
//        RequestEntity<Void> requestEntity = RequestEntity.method(HttpMethod.GET, uri).build();
//        ResponseEntity<String> responseEntity=null;

        Map<String, String> params = new HashMap<>();
        params.put("url", "123.cn");

        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", "123.cn");
        RequestEntity requestEntity = RequestHolder.method(HttpMethod.GET, "https://tenapi.cn/whois/", params)
                .authentication(AuthenticationEnum.COOKIE)
                .buildHeader(headers)
                .build();
        ResponseEntity<Object> responseEntity = new Htf<Object>().execute(requestEntity, Object.class);
    }


    public static ClientHttpRequestFactory httpRequestFactory() {
        return new OkHttp3ClientHttpRequestFactory(okHttpConfigClient());
    }

    public static OkHttpClient okHttpConfigClient() {
        return new OkHttpClient().newBuilder()
                .connectionPool(pool())
                .connectTimeout(1, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .hostnameVerifier((hostname, session) -> true)
                .build();
    }

    public static ConnectionPool pool() {
        return new ConnectionPool(200, 300, TimeUnit.SECONDS);
    }

}
