package br.com.ifes.backend_pit.controllers.usuarios;

import br.com.ifes.backend_pit.models.dto.api.LoginResponseDto;
import br.com.ifes.backend_pit.models.usuarios.Usuario;
import br.com.ifes.backend_pit.security.JWTAuthResponse;
import br.com.ifes.backend_pit.services.usuarios.LoginService;
import br.com.ifes.backend_pit.services.usuarios.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final UserService userService;

    @PostMapping()
    public LoginResponseDto login(@RequestBody Usuario login){
        String token = loginService.login(login);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        Usuario usuario = this.userService.getUsuario(login.getUsername());

        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setJwt(jwtAuthResponse);
        loginResponseDto.setUsuario(usuario);

        return loginResponseDto;
    }
}
