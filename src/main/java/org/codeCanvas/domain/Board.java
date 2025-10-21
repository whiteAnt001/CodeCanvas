package org.codeCanvas.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    private String title;
    private String content;
    private String board;
    private String image_name;
    private String image_original_name;
    private LocalDateTime regDate;

    @ManyToOne
    @JoinColumn(name = "user_id") // FK 컬럼명
    private User user;

    private String writerName;
}
