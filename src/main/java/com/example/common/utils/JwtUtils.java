package com.example.common.utils;

import com.example.config.JwtInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.Map;


/**
 * @author 18237
 */
public class JwtUtils {

    /**
     * @param username       用户名
     * @param audience       接收者
     * @param issuer         发行者
     * @param tTLMillis      过期时间(毫秒)
     * @param base64Security 密钥
     * @return
     */
    public static String createJWT(String username, Map<String, Object> claims, String audience, String issuer,
                                   long tTLMillis, String base64Security) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        // 生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Security);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder;
        // 添加构成JWT的参数
        // jwt签发者
        builder = Jwts.builder()
                .setId(username)
                .setIssuer(issuer)
                // 接收jwt的一方
                .setAudience(audience)
                .setClaims(claims)
                .signWith(signatureAlgorithm, signingKey);

        // 添加Token过期时间
        if (tTLMillis >= 0) {
            long expMillis = nowMillis + tTLMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp).setNotBefore(now);
        }
        // 生成JWT
        return builder.compact();
    }


    public static Claims parseJWT(String jsonWebToken, String base64Security) {
        try {
            Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(base64Security))
                    .parseClaimsJws(jsonWebToken).getBody();
            return claims;
        } catch (Exception ex) {
            return null;
        }
    }


    public static boolean validate(String jsonWebToken, JwtInfo jwtinfo) {
        try {
            Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(jwtinfo.getBase64Secret()))
                    .parseClaimsJws(jsonWebToken).getBody();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static String getUsername(String token,String base64Security){
        Claims claims = parseJWT(token, base64Security);
        return (String) claims.get("username");
    }

    /**
     * 判断过期
     * @param token
     * @return
     */
    public static boolean isExpire(String token, String base64Security){
        Claims claims = parseJWT(token, base64Security);
        Date expirationDate = claims.getExpiration();
        Date currentDate = new Date();
        return expirationDate.before(currentDate);
    }




}