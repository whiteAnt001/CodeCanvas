package org.codeCanvas.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.codeCanvas.domain.Board;
import org.codeCanvas.domain.User;
import org.codeCanvas.repository.BoardRepository;
import org.codeCanvas.repository.UserRepository;
import org.codeCanvas.util.JwtUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final JwtUtil jwtUtil;

    // 게시글 저장
    public Board saveBoard(Board board) {
        return boardRepository.save(board);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다.: " + username));
    }

    public User getUserInfoToken(String token) {
        Claims claims = jwtUtil.getClaims(token);
        String email = claims.getSubject();

        return userRepository.findByEmail(email);
    }
}
