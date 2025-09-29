package org.codeCanvas.controller;

import lombok.RequiredArgsConstructor;
import org.codeCanvas.domain.Board;
import org.codeCanvas.domain.User;
import org.codeCanvas.dto.BoardDTO;
import org.codeCanvas.repository.BoardRepository;
import org.codeCanvas.repository.UserRepository;
import org.codeCanvas.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
public class UserViewController {
    private final UserRepository userRepository;
    private final BoardService boardService;

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
        return "user/join";
    }

    // 이메일 인증 화면
    @GetMapping("/verify")
    public String verifyEmail(@RequestParam("token") String token) {
        User user = userRepository.findByVerificationToken(token);

        if(user == null || user.getTokenExpiration().isBefore(LocalDateTime.now())) {
            return "verify-fail";
        }

        user.setEnabled(true);
        user.setVerificationToken(null);
        userRepository.save(user);

        return "verify-success";
    }
}
