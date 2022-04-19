package com.cornucopib.htf.core;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * http终结者.
 *
 * @author cornucopib
 * @since 2022/4/17
 */
public class Htf<T> {

    @Resource
    private RestTemplate restTemplate;

    public ResponseEntity<T> execute(RequestEntity requestEntity, Class<T> responseType) {
        return  this.restTemplate.exchange(requestEntity,responseType);
    }


}
