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
public class ResetRequest {
    @NotBlank
    @Size(max= 100)
    @Size(min= 4, message = "Minimal Character is 4")
    private String username;
}
