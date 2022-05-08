package com.cornucopib.htf.core;

import org.springframework.web.client.RestTemplate;

/**
 * @author cornucopib
 * @since 2022/5/8
 */
public enum RestTemplateHolder {
    INSTANCE;


    private RestTemplateHolder(){
    }

    public  RestTemplate getRestTemplate(){
        return new RestTemplate();
    }


}
