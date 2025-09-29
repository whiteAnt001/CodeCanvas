package org.codeCanvas.controller;

import lombok.RequiredArgsConstructor;
import org.codeCanvas.domain.Board;
import org.codeCanvas.domain.User;
import org.codeCanvas.dto.BoardDTO;
import org.codeCanvas.repository.UserRepository;
import org.codeCanvas.service.UserService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserApiController {
    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/check-username")
    public boolean checkUsername(@RequestParam String username) {
        return userRepository.existsByUsername(username);
    }
}
