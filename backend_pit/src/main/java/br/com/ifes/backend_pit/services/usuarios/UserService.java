package br.com.ifes.backend_pit.services.usuarios;

import br.com.ifes.backend_pit.email.EmailService;
import br.com.ifes.backend_pit.exception.NotFoundException;
import br.com.ifes.backend_pit.models.usuarios.Usuario;
import br.com.ifes.backend_pit.repositories.usuarios.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static br.com.ifes.backend_pit.constants.ErrorConstants.RECOVERY_PASS_ERROS.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    private final EmailService emailService;

    private final BCryptPasswordEncoder passwordEncoder;


    private final Integer EXPIRATION_TIME = 30;


    public void sendRecoveryCode(String email) {
        Usuario user = userRepository.findByUsername(email)
                .orElseThrow(() -> new IllegalArgumentException(MSG_ERRO_USUARIO_NAO_ENCONTRADO));

        final AtomicInteger TAM_RECOVERY_CODE = new AtomicInteger(8);
        String recoveryCode = RandomStringUtils.randomAlphanumeric(TAM_RECOVERY_CODE.get());
        Instant recoveryCodeGeneratedAt = Instant.now();

        user.setRecoveryCode(recoveryCode);
        user.setRecoveryCodeGeneratedAt(recoveryCodeGeneratedAt);

        userRepository.save(user);

        emailService.enviarEmail(
                user.getUsername(),
                "Código de Recuperação de Senha",
                "Seu código de recuperação de senha é: " + recoveryCode +
                        ". Ele expira em " + EXPIRATION_TIME +" minutos."
        );
    }

    public void resetPassword(String email, String recoveryCode, String newPassword) {
        Usuario user = userRepository.findByUsername(email)
                .orElseThrow(() -> new IllegalArgumentException(MSG_ERRO_USUARIO_NAO_ENCONTRADO));


        if (user.getRecoveryCode() == null || !user.getRecoveryCode().equals(recoveryCode)) {
            throw new IllegalArgumentException(MSG_ERRO_CODIGO_RECUPERACAO_INVALIDO);
        }

        Instant threeMinutesAgo = Instant.now().minus(Duration.ofMinutes(EXPIRATION_TIME));
        if (user.getRecoveryCodeGeneratedAt().isBefore(threeMinutesAgo)) {
            throw new IllegalArgumentException(MSG_ERRO_CODIGO_RECUPERACAO_EXPIRADO);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.clearRecoveryCode();

        userRepository.save(user);

        emailService.enviarEmail(
                user.getUsername(),
                "Senha Alterada",
                "Sua senha foi alterada com sucesso."
        );
    }

    public Usuario getUsuario(UUID idUsuario){
        return this.userRepository.findById(idUsuario).orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }

    public Usuario getUsuario(String username){
        return this.userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }
}
