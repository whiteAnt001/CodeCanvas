package org.codeCanvas.controller;

import lombok.RequiredArgsConstructor;
import org.codeCanvas.domain.User;
import org.codeCanvas.repository.UserRepository;
import org.codeCanvas.service.EmailVerficationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailVerficationController {

    private final UserRepository userRepository;
    private final EmailVerficationService emailVerficationService;

    // 이메일 인증 화면
    @GetMapping("/verification")
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

    // 이메일 재전송 폼
    @GetMapping("/verification/resend")
    public String verifyForm() {
        return "email/verify";
    }

    // 이메일 인증 API
    @GetMapping("/api/verification")
    public String verify(@RequestParam("token") String token, Model model) {
        User user = userRepository.findByVerificationToken(token);

        if(user == null) {
            model.addAttribute("message", "유효하지 않은 인증 링크입니다.");
        } else {
            user.setEnabled(true); // 인증완료
            user.setVerificationToken(null);
            userRepository.save(user);
            model.addAttribute("message", "인증이 완료되었습니다.");
        }
        return "email/verified"; // 인증완료 폼
    }

    // 이메일 재전송 API
    @PostMapping("/api/resend-verification")
    public ResponseEntity<?> resendVerification(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        User user = userRepository.findByEmail(email);
        if(user == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "등록되지 않은 이메일입니다."));
        }

        if(user.isEnabled()) {
            return ResponseEntity.badRequest().body(Map.of("error", "이미 인증이 완료되었습니다."));
        }

        // 새 인증토큰 생성
        String newToken = UUID.randomUUID().toString();
        user.setVerificationToken(newToken);
        userRepository.save(user);

        // 이메일 재전송
        emailVerficationService.sendVerificationEmail(user);

        return ResponseEntity.ok().body(Map.of("message", "인증 메일이 재전송 되었습니다."));
    }
}
