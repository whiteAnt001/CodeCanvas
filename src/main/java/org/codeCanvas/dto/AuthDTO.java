package org.codeCanvas.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AuthDTO {
    private String email;
    private String username;
    private String role;
    private String password;
}
