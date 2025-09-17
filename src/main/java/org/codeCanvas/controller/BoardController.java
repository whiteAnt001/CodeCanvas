package org.codeCanvas.controller;

import lombok.RequiredArgsConstructor;
import org.codeCanvas.domain.Board;
import org.codeCanvas.dto.BoardDTO;
import org.codeCanvas.service.BoardService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/write")
    public BoardDTO boardWrite(@ModelAttribute BoardDTO dto) throws IOException {
        return boardService.createBoard(dto);
    }

}
