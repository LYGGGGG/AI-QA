package com.example.AI_QA.util;

import io.jsonwebtoken.*;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET_KEY = "your-secret-key"; // 建议放配置文件中
    private static final long EXPIRATION = 1000 * 60 * 60 * 24; // 1天

    // 生成 Token
    public static String generateToken(Long userId, String username) {
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // 解析 Token
    public static Claims parseToken(String token) throws ExpiredJwtException {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
