package org.codeCanvas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {
    // 메인화면
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // 게시글 작성
    @GetMapping("/write")
    public String writer() {
        return "user/board-write";
    }

}
