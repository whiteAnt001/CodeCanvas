package org.codeCanvas.controller;

import lombok.RequiredArgsConstructor;
import org.codeCanvas.domain.Board;
import org.codeCanvas.dto.BoardDTO;
import org.codeCanvas.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    // 게시글 작성
    @GetMapping("/write")
    public String board_write() {
        return "user/board-write";
    }
    
    // 게시글 상세조회
    @GetMapping("/{id}")
    public String board_view(@PathVariable("id") Long idx, Model model) {
        Board board = boardService.findByIdx(idx).orElseThrow(() -> new NoSuchElementException("게시글이 존재하지 않습니다."));
        model.addAttribute("board", board);

        return "user/board-view";
    }

}
