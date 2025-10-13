package org.codeCanvas.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.codeCanvas.util.JwtUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final JwtUtil jwtUtil;

    @ModelAttribute
    public void addGlobalAttributes(HttpServletRequest request, Model model) {
        // 쿠키에서 accessToken 가져오기
        String token = jwtUtil.getTokenFromCookieByName(request, "accessToken");

        // 로그인 여부 기본값
        boolean isLoggedIn = false;
        Map<String, Object> loginUser = new HashMap<>();

        if (token != null && jwtUtil.validateToken(token)) {
            try {
                Claims claims = jwtUtil.getClaims(token);

                String username = claims.get("name", String.class);
                String email = claims.getSubject();
                String role = claims.get("role", String.class);
                Long id = claims.get("id", Long.class);

                loginUser.put("id", id);
                loginUser.put("username", username);
                loginUser.put("email", email);
                loginUser.put("role", role);

                isLoggedIn = true;
            } catch (Exception e) {
                System.out.println("JWT 파싱 오류: " + e.getMessage());
            }
        }

        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("loginUser", loginUser.isEmpty() ? null : loginUser);
    }
}
