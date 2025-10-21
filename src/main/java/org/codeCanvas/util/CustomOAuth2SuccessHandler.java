package org.codeCanvas.util;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.codeCanvas.domain.User;
import org.codeCanvas.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = (String) oAuth2User.getAttribute("email");
        if(email == null) {
            Object responseObj = oAuth2User.getAttribute("response");
            if(responseObj instanceof Map<?,?> map) {
                email = (String) map.get("email");
            }
        }

        if(email == null) {
            throw new RuntimeException("OAuth2 로그인 : 이메일 정보를 찾을 수 없습니다.");
        }

        User user = userRepository.findByEmail(email);

        if(user == null) {
            throw new RuntimeException("해당 사용자가 없습니다.");
        }

        // 닉네임이 없으면 닉네임 입력 창으로
        if(user.getUsername() == null) {
            getRedirectStrategy().sendRedirect(request, response, "/check-nickname");
            return;
        }

        // JWT 발급
        String accessToken = jwtUtil.generatedAccessToken(user);
        String refreshToken = jwtUtil.generatedRefreshToken(user);

        // 쿠키에 추가
        jwtUtil.addJwtToCookie(response, accessToken, "accessToken");
        jwtUtil.addJwtToCookie(response, refreshToken, "refreshToken");
        // 로그인 성공 후 리다이렉트
        getRedirectStrategy().sendRedirect(request, response, "/");
    }
}
