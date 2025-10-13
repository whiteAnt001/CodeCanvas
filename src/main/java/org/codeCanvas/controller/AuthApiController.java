package org.codeCanvas.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.codeCanvas.domain.User;
import org.codeCanvas.dto.AuthDTO;
import org.codeCanvas.repository.AuthService;
import org.codeCanvas.repository.UserRepository;
import org.codeCanvas.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApiController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthDTO dto) {
        boolean result = authService.register(dto);
        if(result) {
            return ResponseEntity.ok().body(Map.of("message", "회원가입 성공"));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "이미 존재하는 이메일입니다."));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO dto, HttpServletResponse response) {
        User user = userRepository.findByEmail(dto.getEmail()); // 사용자 조회

        // 사용자가 존재하지 않거나 비밀번호가 틀린 경우
        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(Map.of("error", "계정 또는 비밀번호가 일치하지 않습니다."));
        }
        
        // 이메일 인증을 하지 않은 경우
        if(!user.isEnabled()) {
            return ResponseEntity.badRequest().body(Map.of("error", "이메일 인증을 완료해주세요."));
        }

        // 쿠키에 토큰을 저장
        String accessToken = jwtUtil.generatedAccessToken(user);
        String refreshToken = jwtUtil.generatedRefreshToken(user);
        jwtUtil.addJwtToCookie(response, accessToken, "accessToken");
        jwtUtil.addJwtToCookie(response, refreshToken, "refreshToken");

        return ResponseEntity.ok().body(Map.of("message", "로그인 성공!"));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> login(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        
        return ResponseEntity.ok("로그아웃 완료");
    }
}
