package com.atri.puscerdas.model.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    private String username;
    private String phone;
    private String email;
    private Integer role;
    private Integer status;
}
