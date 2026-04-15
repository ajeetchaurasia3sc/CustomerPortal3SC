package com.sssupply.customerportal.dto;
import jakarta.validation.constraints.*;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AuthRequest {
    @NotBlank(message = "Email is required") @Email private String email;
    @NotBlank(message = "Password is required") private String password;
}
