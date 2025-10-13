package org.codeCanvas.service;

import lombok.RequiredArgsConstructor;
import org.codeCanvas.domain.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailVerficationService {

    private final JavaMailSender mailSender;

    // 이메일 인증 전송 로직
    public void sendVerificationEmail(User user) {
        String link ="http://localhost:8099/email/api/verification?token=" + user.getVerificationToken();
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
