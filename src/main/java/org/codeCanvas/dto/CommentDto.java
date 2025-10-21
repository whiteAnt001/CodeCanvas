package org.codeCanvas.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.codeCanvas.domain.Board;
import org.codeCanvas.domain.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class CommentDto {
    private Long idx;
    private Long boardId;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private Long likeCount;
    private List<CommentDto> replies;

    public static CommentDto from(Comment comment) {
        return CommentDto.builder()
                .idx(comment.getIdx())
                .boardId(comment.getBoardId())
                .content(comment.getContent())
                .username(comment.getUsername())
                .createdAt(comment.getCreatedAt())
                .likeCount(comment.getLikeCount())
                .replies(new ArrayList<>())
                .build();
    }
}

