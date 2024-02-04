package com.atri.puscerdas.model.auth;

import jakarta.persistence.Column;
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
public class ChangePasswordRequest {
    @NotBlank
    @Size(max= 100)
    @Size(min= 4, message = "Minimal Character is 4")
    private String username;

    @NotBlank
    @Size(max = 100)
    @Size(min= 5, message = "Minimal Character is 5")
    private String password;

    @NotBlank
    private String idReset;

    private long idResetExp;
}
