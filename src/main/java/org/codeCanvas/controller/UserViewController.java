package org.codeCanvas.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.codeCanvas.domain.Board;
import org.codeCanvas.domain.User;
import org.codeCanvas.dto.BoardDTO;
import org.codeCanvas.repository.BoardRepository;
import org.codeCanvas.repository.UserRepository;
import org.codeCanvas.service.BoardService;
import org.codeCanvas.util.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
public class UserViewController {
    private final UserRepository userRepository;
    private final BoardService boardService;
    private final JwtUtil jwtUtil;
    // 메인화면
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("free", boardService.getListPostByCategory("FREE"));
        model.addAttribute("recruit", boardService.getListPostByCategory("RECRUIT"));
        model.addAttribute("back", boardService.getListPostByCategory("BACK"));
        model.addAttribute("front", boardService.getListPostByCategory("FRONT"));

        return "index";
    }

    // 로그인 화면
    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    // 회원가입 화면
    @GetMapping("/join")
    public String join() {
        return "user/register";
    }

    // 소셜 로그인 닉네임 폼
    @GetMapping("/check-nickname")
    public String checkNickname(Model model, @AuthenticationPrincipal OAuth2User oAuth2User) {
        model.addAttribute("providerId", oAuth2User.getAttribute("sub")); // 구글용
        return "user/nickName";
    }

    @PostMapping("/check-nickname")
    public String saveNickname(@RequestParam String username, @AuthenticationPrincipal OAuth2User oAuth2User,  HttpServletResponse response, Authentication authentication) {
        String providerId = oAuth2User.getAttribute("sub");
        User user = userRepository.findByProviderId(providerId).orElseThrow(() ->
            new RuntimeException("사용자 없음"));
        user.setUsername(username);
        userRepository.save(user);
        // JWT 발급
        String accessToken = jwtUtil.generatedAccessToken(user);
        String refreshToken = jwtUtil.generatedRefreshToken(user);

        // 쿠키에 추가
        jwtUtil.addJwtToCookie(response, accessToken, "accessToken");
        jwtUtil.addJwtToCookie(response, refreshToken, "refreshToken");
        return "redirect:/";
    }
}
