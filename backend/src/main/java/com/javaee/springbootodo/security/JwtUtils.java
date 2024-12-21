package com.javaee.springbootodo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    private static String SECRET_KEY = "mySecretKey";

    private static long EXPIRATION_TIME = 2 * 24 * 60 * 60 * 1000;

    private static final KeyPair keyPair; // 如果使用RSA等非对称加密，需要生成密钥对

    static {
        // 初始化密钥对（仅当使用非对称加密时需要）
        // 这里使用RSA作为示例，你可以根据需要选择其他算法
        keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
    }

    /**
     * 非对称加密生成 token
     *
     * @param username
     * @return
     */
    public static String generateTokenAsymmetric (String username) {
        return Jwts.builder()
                .claim("sub", username)
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.RS256, keyPair.getPrivate())
                .compact();
    }

    /**
     * 对称加密生成 token
     *
     * @param username
     * @return
     */
    public static String generateTokenSymmetric(String username) {
        return Jwts.builder()
                .claim("sub", username)
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.ES512, SECRET_KEY)
                .compact();
    }

    /**
     * 对称加密校验token
     *
     * @param token
     * @return
     */
    public static boolean validateTokenSymmetric (String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            // 处理解析或验证错误，可以根据需要抛出异常或返回 null
            throw new RuntimeException("JWT validation failed: " + e.getMessage(), e);
        }
    }

    /**
     * 非对称加密校验token
     *
     * @param token
     * @return
     */
    public static boolean validateTokenAsymmetric(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(keyPair.getPublic())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            // 处理解析或验证错误，可以根据需要抛出异常或返回 null
            throw new RuntimeException("JWT validation failed: " + e.getMessage(), e);
        }
    }

    /**
     * 对称加密根据token获取用户名
     *
     * @param token
     * @return
     */
    public static String getUsernameFromTokenSymmetric (String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(keyPair.getPublic())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return (String) claims.get("sub");
    }

    /**
     * 非对称加密根据token获取用户名
     *
     * @param token
     * @return
     */
    public static String getUsernameFromTokenAsymmetric (String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(keyPair.getPublic())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return (String) claims.get("sub");
    }
}
