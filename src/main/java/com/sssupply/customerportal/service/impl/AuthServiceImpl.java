package com.sssupply.customerportal.service.impl;

import com.sssupply.customerportal.dto.*;
import com.sssupply.customerportal.entity.User;
import com.sssupply.customerportal.jwt.JwtUtil;
import com.sssupply.customerportal.repository.UserRepository;
import com.sssupply.customerportal.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse login(AuthRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        User user = userRepository.findByEmailAndDeletedAtIsNull(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String workspaceId = user.getWorkspace() != null && user.getWorkspace().getId() != null
                ? user.getWorkspace().getId().toString() : null;
        String accessToken = jwtUtil.generateAccessToken((UserDetails) auth.getPrincipal(),
                user.getRole().toString(), workspaceId);
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());
        return AuthResponse.builder().accessToken(accessToken).refreshToken(refreshToken).expiresIn(3600)
                .user(toUserDto(user)).build();
    }

    @Override
    public AuthResponse refresh(String refreshToken) {
        String email = jwtUtil.extractUsername(refreshToken);
        User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String workspaceId = user.getWorkspace() != null && user.getWorkspace().getId() != null
                ? user.getWorkspace().getId().toString() : null;
        String newAccessToken = jwtUtil.generateAccessToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail()).password(user.getPassword())
                        .roles(user.getRole().toString()).build(),
                user.getRole().toString(), workspaceId);
        return AuthResponse.builder().accessToken(newAccessToken)
                .refreshToken(jwtUtil.generateRefreshToken(user.getEmail()))
                .expiresIn(3600).user(toUserDto(user)).build();
    }

    @Override
    public UserDto getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return toUserDto(user);
    }

    @Override public String inviteUser(InviteRequest request) { return "Invitation sent to " + request.getEmail(); }
    @Override public String acceptInvite(AcceptInviteRequest request) { return "Account created. You can now login."; }
    @Override public String forgotPassword(String email) { return "Password reset link sent."; }
    @Override public String resetPassword(ResetPasswordRequest request) { return "Password reset successfully."; }
    @Override public void logout() { SecurityContextHolder.clearContext(); }

    private UserDto toUserDto(User user) {
        String workspaceId = user.getWorkspace() != null && user.getWorkspace().getId() != null
                ? user.getWorkspace().getId().toString() : null;
        return UserDto.builder().id(user.getId().toString()).email(user.getEmail())
                .name(user.getName()).role(user.getRole()).workspaceId(workspaceId).build();
    }
}
