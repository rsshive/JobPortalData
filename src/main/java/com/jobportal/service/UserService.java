package com.jobportal.service;

import com.jobportal.model.User;
import com.jobportal.model.enums.Role;

import java.util.List;
import java.util.Optional;
import com.jobportal.model.enums.AccountStatus;

public interface UserService {
    User registerUser(User user);
    Optional<User> login(String email, String password);
    Optional<User> findById(Integer userId);
    Optional<User> findByEmail(String email);
    List<User> getAllUsers();
    User updateUserStatus(Integer userId, AccountStatus status);
    User updateUserStatusByEmail(String email, AccountStatus status);
    void createPasswordResetTokenForUser(User user, String token);
    String validatePasswordResetToken(String token);
    void changeUserPassword(User user, String newPassword);
}
