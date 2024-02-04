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
public class RegisterEmployeeRequest {
    @NotBlank
    @Size(max= 100)
    @Size(min= 4, message = "Minimal Character is 4")
    private String username;

    @Size(max = 100)
    @NotBlank
    private String phone;

    @Size(max = 100)
    @NotBlank
    @Email
    private String email;
}
