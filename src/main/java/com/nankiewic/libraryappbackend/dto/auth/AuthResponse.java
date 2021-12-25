package com.nankiewic.libraryappbackend.dto.auth;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class AuthResponse {

    String jwtToken;
    String email;
    String userId;
    String roleName;
}
