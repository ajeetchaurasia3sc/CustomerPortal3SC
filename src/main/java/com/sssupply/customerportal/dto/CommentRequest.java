package com.sssupply.customerportal.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CommentRequest {
    @NotBlank(message = "Comment body cannot be empty") private String body;
    private boolean isInternal = false;
}
