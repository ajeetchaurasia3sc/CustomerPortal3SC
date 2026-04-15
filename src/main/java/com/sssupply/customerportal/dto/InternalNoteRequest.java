package com.sssupply.customerportal.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class InternalNoteRequest {
    @NotBlank(message = "Note body cannot be empty") private String body;
}
