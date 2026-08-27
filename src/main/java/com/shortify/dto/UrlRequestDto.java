package com.shortify.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UrlRequestDto {
    @NotBlank(message = "URL cannot be empty")
    @URL(message = "Must be a valid URL")
    private String originalUrl;

    private String customAlias;
    
    private LocalDateTime expiresAt;
}
