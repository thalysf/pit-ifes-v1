package br.com.ifes.backend_pit.models.dto.api;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RequestPITDto {
    @NotBlank(message = "O período não pode estar em branco")
    private String periodo;
}
