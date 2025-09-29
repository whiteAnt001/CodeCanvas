package org.codeCanvas.controller;

import lombok.RequiredArgsConstructor;
import org.codeCanvas.dto.BoardDTO;
import org.codeCanvas.service.BoardService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardApiController {
    private final BoardService boardService;

    // 게시글 작성 api
    @PostMapping("/write")
    public BoardDTO boardWrite(@ModelAttribute BoardDTO dto) throws IOException {
        return boardService.createBoard(dto);
    }

}
