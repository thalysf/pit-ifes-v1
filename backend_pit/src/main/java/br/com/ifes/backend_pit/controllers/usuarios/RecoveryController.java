package br.com.ifes.backend_pit.controllers.usuarios;

import br.com.ifes.backend_pit.models.dto.api.ResetPasswordDto;
import br.com.ifes.backend_pit.services.usuarios.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user/password")
@RequiredArgsConstructor
public class RecoveryController {

    private final UserService userService;

    @PostMapping("/recover")
    @ResponseStatus(HttpStatus.OK)
    public void recover(@RequestBody String email) {
        userService.sendRecoveryCode(email);
    }

    @PatchMapping("/reset")
    @ResponseStatus(HttpStatus.OK)
    public void reset(@RequestBody ResetPasswordDto resetPasswordDto) {
        userService.resetPassword(resetPasswordDto.getEmail(), resetPasswordDto.getRecoveryCode(), resetPasswordDto.getNewPassword());
    }
}
