package org.codeCanvas.service;

import lombok.RequiredArgsConstructor;
import org.codeCanvas.domain.Board;
import org.codeCanvas.repository.BoardRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final BoardRepository boardRepository;
    // 게시글 저장
    public Board saveBoard(Board board) {
        return boardRepository.save(board);
    }

}
