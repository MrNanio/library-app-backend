package com.nankiewic.libraryappbackend.dto;

import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserJwtDTO {
    String email;
    Collection<SimpleGrantedAuthority> authority;
}
