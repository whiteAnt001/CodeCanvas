package org.codeCanvas.service;

import lombok.RequiredArgsConstructor;
import org.codeCanvas.domain.User;
import org.codeCanvas.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // google, github
        String providerId;
        String email;
        String username;

        // OAuth 공급자별 처리
        if ("github".equalsIgnoreCase(registrationId)) {
            providerId = oAuth2User.getAttribute("id").toString();
            username = oAuth2User.getAttribute("login"); // 깃허브 로그인 ID
            email = oAuth2User.getAttribute("email"); // 이메일이 없을 수도 있음
        } else { // 구글
            providerId = oAuth2User.getAttribute("sub");
            username = oAuth2User.getAttribute("name");
            email = oAuth2User.getAttribute("email");
        }

        final String finalProvider = registrationId.toUpperCase();
        final String finalProviderId = providerId;
        final String finalEmail = email;

        // 기존 유저 조회
        User user = userRepository.findByProviderAndProviderId(finalProvider, finalProviderId)
                .orElseGet(() -> createUser(finalProvider, finalProviderId, finalEmail));

        // username이 없는 경우 -> 닉네임 입력 유도
        if (user.getUsername() == null) {
            // 여기서 로그인 후 닉네임 입력 페이지로 리다이렉트하도록 컨트롤러에서 처리
            // 예: /oauth/nickname
        }

        return oAuth2User;
    }
    
    // 새 소셜로그인 사용자 생성
    private User createUser(String provider, String providerId, String email) {
        User newUser = new User();
        newUser.setProvider(provider);
        newUser.setProviderId(providerId);
        newUser.setEmail(email);
        newUser.setUsername(null); // 아직 닉네임 없음
        return userRepository.save(newUser);
    }
}
