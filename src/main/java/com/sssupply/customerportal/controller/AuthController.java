package com.sssupply.customerportal.controller;

import com.sssupply.customerportal.dto.*;
import com.sssupply.customerportal.enums.*;
import com.sssupply.customerportal.service.AuthService;
import com.sssupply.customerportal.util.EnumMetaUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Tag(name = "Auth")
@RestController @RequestMapping("/api/v1/auth") @RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        Map<String, Object> meta = Map.of(
                "ticket", Map.of("status", EnumMetaUtil.names(TicketStatus.class),
                        "priority", EnumMetaUtil.names(TicketPriority.class),
                        "category", EnumMetaUtil.names(TicketCategory.class)),
                "project", Map.of("status", EnumMetaUtil.names(ProjectStatus.class)));
        return ResponseEntity.ok(ApiResponse.success(response, meta));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(ApiResponse.success(authService.refresh(request.getRefreshToken())));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> getCurrentUser() {
        return ResponseEntity.ok(ApiResponse.success(authService.getCurrentUser()));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() { return ResponseEntity.ok(ApiResponse.success(null)); }

    @PostMapping("/invite")
    public ResponseEntity<ApiResponse<String>> inviteUser(@RequestBody InviteRequest request) {
        return ResponseEntity.ok(ApiResponse.success(authService.inviteUser(request)));
    }

    @PostMapping("/accept-invite")
    public ResponseEntity<ApiResponse<String>> acceptInvite(@RequestBody AcceptInviteRequest request) {
        return ResponseEntity.ok(ApiResponse.success(authService.acceptInvite(request)));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.ok(ApiResponse.success(authService.forgotPassword(request.getEmail())));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(ApiResponse.success(authService.resetPassword(request)));
    }
}
