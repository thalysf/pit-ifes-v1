package br.com.ifes.backend_pit.models.dto.api;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ResetPasswordDto {
    @NotBlank
    private String email;
    @NotBlank
    private String recoveryCode;
    @NotBlank
    private String newPassword;
}
