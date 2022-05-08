package com.cornucopib.htf.support;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 验证工厂.
 *
 * @author cornucopib
 * @since 2022/5/8
 */
public class AuthenticationFactory {

    private static final List<AuthenticationStrategy> AUTHENTICATION_STRATEGY_LIST=new ArrayList<>();

    static{
        AUTHENTICATION_STRATEGY_LIST.add(new CookieAuthenticationStrategy());
        AUTHENTICATION_STRATEGY_LIST.add(new SoaAuthenticationStrategy());
    }

    public static AuthenticationStrategy getStrategy(AuthenticationEnum authenticationEnum){
        List<AuthenticationStrategy>  authenticationStrategyList = AUTHENTICATION_STRATEGY_LIST.stream().filter(item -> {
            return hitStrategy(item, authenticationEnum);
        }).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(authenticationStrategyList)){
            return authenticationStrategyList.get(0);
        }else{
            throw new IllegalArgumentException("illegal strategyId in AUTHENTICATION_STRATEGY_LIST");
        }
    }

    private static boolean hitStrategy(AuthenticationStrategy authenticationStrategy,AuthenticationEnum authenticationEnum){
        return authenticationEnum.name().equals(authenticationStrategy.strategyId());
    }

}
