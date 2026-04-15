package com.sssupply.customerportal.dto;
import jakarta.validation.constraints.*;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor
public class InviteRequest {
    @NotBlank @Email private String email;
    private String name;
    private String role;
}
