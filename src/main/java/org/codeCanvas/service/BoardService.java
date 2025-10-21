package org.codeCanvas.service;

import lombok.RequiredArgsConstructor;
import org.codeCanvas.domain.Board;
import org.codeCanvas.domain.User;
import org.codeCanvas.dto.BoardDTO;
import org.codeCanvas.repository.BoardRepository;
import org.codeCanvas.repository.UserRepository;
import org.codeCanvas.util.FileUploadUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public BoardDTO createBoard(BoardDTO dto, User user) throws IOException {
        Board board = new Board();
        board.setTitle(dto.getTitle());
        board.setContent(dto.getContent());
        board.setBoard(dto.getBoard());
        board.setUser(user);
        board.setWriterName(user.getUsername());
        board.setRegDate(LocalDateTime.now());

        if(dto.getFiles() != null && dto.getFiles().length > 0) {
            String[] savedFileNames = FileUploadUtil.saveFiles(dto.getFiles());
            board.setImage_name(savedFileNames[0]);
        }

        boardRepository.save(board);

        dto.setImage_name(board.getImage_name());
        return dto;
    }

    // 모든 게시글 가져오기
    public List<Board> getBoardList() {
        return boardRepository.findAll();
    }

    // 카테고리 별 최신 5개 게시글만 가져오기
    public List<Board> getListPostByCategory(String board) {
        return boardRepository.findTop5ByBoardOrderByIdxDesc(board);
    }

    public Optional<Board> findByIdx(Long idx) {
        return boardRepository.findById(idx);
    }
}
