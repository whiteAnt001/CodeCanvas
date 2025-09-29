package org.codeCanvas.repository;

import lombok.RequiredArgsConstructor;
import org.codeCanvas.domain.User;
import org.codeCanvas.dto.AuthDTO;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;

    // 회원가입 로직
    public boolean register(AuthDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()) != null) {
            return false;
        }
        // 프론트에서 받아온 정보 넣어주기
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRole("일반");
        user.setEnabled(false);
        user.setCreatedAt(LocalDateTime.now());
        // 토큰 랜덤 생성
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        // 정보 저장
        userRepository.save(user);
        sendVerificationEmail(user);

        return false;
    }

    // 이메일 전송 로직
    public void sendVerificationEmail(User user) {
        String link ="http://localhost:8099/verify?token=" + user.getVerificationToken();
        String subject = "CodeCanvas 이메일 인증";
        String body = "CodeCanvas 이메일 인증입니다. \n\n"
                + "이메일 인증을 완료하려면 아래 링크를 클릭해주세요.: \n"
                + link;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}
