package org.codeCanvas.repository;

import org.codeCanvas.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByIdx(Long idx);

    List<Board> findAll ();

    // 특정 카테고리 게시글 중 최신 5개만 가져오기?
    List<Board> findTop5ByBoardOrderByIdxDesc(String board);
}
