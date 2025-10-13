package org.codeCanvas.repository;

import org.codeCanvas.domain.User;
import org.codeCanvas.dto.AuthDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 유저 이름 찾기
    Optional<User> findByUsername(String username);
    // 유저 이름 중복검사
    boolean existsByUsername(String username);
    // 이메일 인증 토큰 찾기
    User findByVerificationToken(String token);
    // 이메일 중복확인
    User findByEmail(String email);
    // 소셜 로그인 위치(구글, 카카오 등), 소셜로그인 아이디 찾기
    Optional<User> findByProviderAndProviderId(String provider, String providerId);
    // 소셜로그인 아이디 찾기
    Optional<User> findByProviderId(String providerId);
}
