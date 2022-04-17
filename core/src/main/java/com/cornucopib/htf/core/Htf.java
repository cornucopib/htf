package com.cornucopib.htf;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;

import java.net.URI;

/**
 * http终结者.
 *
 * @author cornucopib
 * @since 2022/4/17
 */
public class Htf<T> {

    private URI uri;
    private String url;
    private HttpHeaders HttpHeaders;
    private AuthenticationEnum authenticationEnum;
    private Object body;
    private MultiValueMap<String, String> bodyMap;
    private HttpMethod method;
    private Class<T> responseType;


    public static class RequestEntityBuilder{





    }



}
