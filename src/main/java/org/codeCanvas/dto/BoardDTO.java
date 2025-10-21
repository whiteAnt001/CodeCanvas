package org.codeCanvas.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class BoardDTO {
    private Long idx;
    private String writer;
    private String title;
    private String content;
    private String board;
    private String image_name;
    private String image_original_name;
    private Date regDate;
    // 업로드 파일
    @JsonIgnore
    private MultipartFile[] files;
}
