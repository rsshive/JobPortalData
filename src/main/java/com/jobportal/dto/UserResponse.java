package com.jobportal.dto;

import com.jobportal.model.enums.AccountStatus;
import com.jobportal.model.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Integer userId;
    private String email;
    private Role role;
    private AccountStatus status;
}
