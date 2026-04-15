package com.sssupply.customerportal.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor
public class AcceptInviteRequest {
    @NotBlank private String token;
    @NotBlank private String name;
    @NotBlank private String password;
}
