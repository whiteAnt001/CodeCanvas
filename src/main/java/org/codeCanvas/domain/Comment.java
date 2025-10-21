package org.codeCanvas.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String username;

    @Column(name = "board_id", insertable = false, updatable = false)
    private Long boardId;  // ID만 저장 (읽기 전용)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board; // 엔티티 참조

    @Column(name = "parent_id")
    private Long parentId;

    private String content;
    private Long likeCount;
    private LocalDateTime createdAt;

    // 좋아요 증가
    public void incrementLikeCount() {
        this.likeCount++;
    }
    // 좋아요 감소(취소)
    public void decrementLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    // 답글 여부 확인
    public boolean isReply() {
        return this.parentId != null;
    }

}
