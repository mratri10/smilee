package com.atri.puscerdas.model.auth;

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
public class RegisterRequest {
    @NotBlank
    @Size(max= 100)
    @Size(min= 4, message = "Minimal Charecter is 4")
    private String username;

    @NotBlank
    @Size(max = 100)
    @Size(min= 5, message = "Minimal Charecter is 5")
    private String password;

    @Size(max = 100)
    private String phone;

    @Size(max = 100)
    private String email;
}
