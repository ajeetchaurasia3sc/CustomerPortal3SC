package com.sssupply.customerportal.service;
import com.sssupply.customerportal.dto.*;
public interface AuthService {
    AuthResponse login(AuthRequest request);
    AuthResponse refresh(String refreshToken);
    UserDto getCurrentUser();
    String inviteUser(InviteRequest request);
    String acceptInvite(AcceptInviteRequest request);
    String forgotPassword(String email);
    String resetPassword(ResetPasswordRequest request);
    void logout();
}
