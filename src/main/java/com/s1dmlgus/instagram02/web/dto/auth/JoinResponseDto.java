package com.s1dmlgus.instagram02.web.dto.auth;

import com.s1dmlgus.instagram02.domain.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JoinResponseDto {

    private Long userId;
    private String username;
    private String email;

    private String name;
    private Role role;
}
