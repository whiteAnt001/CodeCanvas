package org.codeCanvas.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.codeCanvas.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "qkffhfksxmfmfwkfgkrhtlvdmsgmlsroal1ghqkffhfksxmfmfwkfgkrhtlvdmsgmlsroal1gh"; // 개발용 임시 키
    private final long ACCESS_TOKEN_EXPIRATION = 15 * 60 * 1000L; // 15분
    private final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000L; // 7일
    private final WebClient webClient;

    public JwtUtil(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8099").build();
    }

    private Key getsigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // 엑세스 토큰(인증용)
    public String generatedAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("id", user.getId())
                .claim("name", user.getUsername())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(getsigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 리프레시 토큰
    public String generatedRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(getsigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getsigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    // JWT를 쿠키에 추가
    public void addJwtToCookie(HttpServletResponse response, String jwt, String cookieName) {
        Cookie cookie = new Cookie(cookieName, jwt);  // "jwt"라는 이름의 쿠키에 토큰 저장
        cookie.setHttpOnly(true);  // JavaScript에서 접근할 수 없도록 설정
        cookie.setSecure(true);    // HTTPS 프로토콜에서만 전송되도록 설정
        cookie.setPath("/");       // 모든 경로에서 접근 가능
        cookie.setMaxAge(900);   // 15분 동안 쿠키 유효 (초 단위)
        response.addCookie(cookie);
    }

    // 쿠키 이름으로 토큰 가져오기
    public String getTokenFromCookieByName(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // 토큰에서 사용자 정보 추출
    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getsigningKey())
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰에서 이름 추출
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getsigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("username", Claims.class);
    }
}
