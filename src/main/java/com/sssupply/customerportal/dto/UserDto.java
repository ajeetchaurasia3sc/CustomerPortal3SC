package com.sssupply.customerportal.dto;
import com.sssupply.customerportal.enums.UserRole;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UserDto {
    private String id;
    private String email;
    private String name;
    private UserRole role;
    private String workspaceId;
}
