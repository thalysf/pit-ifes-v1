package br.com.ifes.backend_pit.models.dto.api;

import br.com.ifes.backend_pit.models.usuarios.Usuario;
import br.com.ifes.backend_pit.security.JWTAuthResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class LoginResponseDto {
    private JWTAuthResponse jwt;

    private Usuario usuario;
}
