package org.codeCanvas.controller;

import lombok.RequiredArgsConstructor;
import org.codeCanvas.domain.User;
import org.codeCanvas.dto.BoardDTO;
import org.codeCanvas.repository.UserRepository;
import org.codeCanvas.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardApiController {
    private final BoardService boardService;
    private final UserRepository userRepository;

    // 게시글 작성 api
    @PostMapping("/write")
    public ResponseEntity<?> boardWrite(@ModelAttribute BoardDTO dto,
                                        @RequestPart(value = "files", required = false) MultipartFile[] files,
                                        @AuthenticationPrincipal Object principal) throws IOException {
        User user = null;

        if(principal instanceof User ) {
            user = (User) principal;
            user = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("사용자 없음"));
        } else if(principal instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) principal;
            String email  = oAuth2User.getAttribute("email");
            user = userRepository.findByEmail(email);
            if (user == null) {
                throw new RuntimeException("사용자 없음");
            }

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        BoardDTO saved = boardService.createBoard(dto, user);
        return ResponseEntity.ok(saved);
    }

}
