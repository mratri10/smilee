package com.atri.puscerdas.model.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateAuthRequest {

    @Size(max = 100)
    private String phone;

    @Size(max = 100)
    @Email
    private String email;

    @Size(max = 100)
    private String password;
    private String userEncrypt;

    private Integer status;
}
