package org.codeCanvas.service;

import lombok.RequiredArgsConstructor;
import org.codeCanvas.domain.User;
import org.codeCanvas.dto.AuthDTO;
import org.codeCanvas.repository.UserRepository;
import org.codeCanvas.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final EmailVerficationService emailVerficationService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 회원가입 로직
    public boolean register(AuthDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()) != null) {
            return false;
        }
        // 프론트에서 받아온 정보 넣어주기
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole("일반");
        user.setEnabled(false);
        user.setCreatedAt(LocalDateTime.now());
        // 토큰 랜덤 생성
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        // 정보 저장
        userRepository.save(user);
        emailVerficationService.sendVerificationEmail(user);

        return true;
    }

    // 로그인 로직
    public String login(AuthDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail());

        if(user == null) {
            return null;
        }
        if(!user.isEnabled()) {
            return null;
        }
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return null;
        }
        return jwtUtil.generatedAccessToken(user);
    }
}
