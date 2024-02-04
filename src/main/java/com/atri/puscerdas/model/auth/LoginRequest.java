package com.atri.puscerdas.model.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
    @Size(max = 100)
    private String phone;

    @Size(max = 100)
    @Email
    private String email;

    @Size(max= 100)
    @Size(min= 4, message = "Minimal Character is 4")
    private String username;

    @NotBlank
    @Size(max = 100)
    @Size(min= 5, message = "Minimal Character is 5")
    private String password;
}
