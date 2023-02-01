package com.example.config.shiro;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author 18237
 */
@Data
public class Token implements AuthenticationToken {

    /**
     * 用户密钥
     */
    private String token;

    public Token(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}