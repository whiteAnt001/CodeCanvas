package org.codeCanvas.service;

import lombok.RequiredArgsConstructor;
import org.codeCanvas.domain.Board;
import org.codeCanvas.dto.BoardDTO;
import org.codeCanvas.repository.BoardRepository;
import org.codeCanvas.util.FileUploadUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardDTO createBoard(BoardDTO dto) throws IOException {
        Board board = new Board();
        board.setTitle(dto.getTitle());
        board.setContent(dto.getContent());
        board.setBoard(dto.getBoard());
        board.setWriter(dto.getWriter());
        board.setRegDate(new Date());

        if(dto.getFiles() != null && dto.getFiles().length > 0) {
            String[] savedFileNames = FileUploadUtil.saveFiles(dto.getFiles());
            board.setImage_name(savedFileNames[0]);
        }

        boardRepository.save(board);

        dto.setImage_name(board.getImage_name());
        return dto;
    }
}
