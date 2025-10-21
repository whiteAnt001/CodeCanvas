package org.codeCanvas.service;

import lombok.RequiredArgsConstructor;
import org.codeCanvas.domain.Comment;
import org.codeCanvas.dto.CommentDto;
import org.codeCanvas.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    public List<CommentDto> getCommentsWithReplies(long boardId) {
        // 모든 댓글 한번에 조회
        List<Comment> allComments = commentRepository.findByBoardId(boardId);

        // 메모리에서 부모-자식 관계 구성
        Map<Long, List<CommentDto>> repliesMap = new HashMap<>();
        List<CommentDto> parentComments = new ArrayList<>();

        for(Comment comment : allComments) {
            CommentDto dto = CommentDto.from(comment);

            if(comment.getParentId() == null) {
                parentComments.add(dto);
            } else {
                repliesMap
                        .computeIfAbsent(comment.getParentId(), k -> new ArrayList<>())
                        .add(dto);
            }

            for(CommentDto parent : parentComments) {
                parent.setReplies
            }
        }
    }
}
