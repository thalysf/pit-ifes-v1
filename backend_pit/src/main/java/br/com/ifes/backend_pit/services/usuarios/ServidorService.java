package br.com.ifes.backend_pit.services.usuarios;

import br.com.ifes.backend_pit.email.EmailService;
import br.com.ifes.backend_pit.enums.RoleNameEnum;
import br.com.ifes.backend_pit.exception.BusinessException;
import br.com.ifes.backend_pit.exception.NotFoundException;
import br.com.ifes.backend_pit.models.usuarios.Role;
import br.com.ifes.backend_pit.models.usuarios.Servidor;
import br.com.ifes.backend_pit.models.usuarios.Usuario;
import br.com.ifes.backend_pit.repositories.usuarios.RoleRepository;
import br.com.ifes.backend_pit.repositories.usuarios.ServidorRepository;
import br.com.ifes.backend_pit.repositories.usuarios.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.ifes.backend_pit.constants.EmailContants.MSG_SENHA_ATUALIZADA_PIT_SERVIDOR;
import static br.com.ifes.backend_pit.constants.EmailContants.MSG_SENHA_CRIADA_PIT_SERVIDOR;
import static br.com.ifes.backend_pit.constants.ErrorConstants.SERVIDOR_ERROS.MSG_ERRO_EMAIL_JA_EM_USO_POR_OUTRO_SERVIDOR;
import static br.com.ifes.backend_pit.constants.ErrorConstants.SERVIDOR_ERROS.MSG_ERRO_SERVIDOR_INEXISTENTE_UPDATE;
import static br.com.ifes.backend_pit.constants.ErrorConstants.SERVIDOR_ERROS.MSG_ERRO_SERVIDOR_NAO_ENCONTRADO;

@Service
@RequiredArgsConstructor
@Transactional
public class ServidorService {

    private final UUID idAdministrador = UUID.fromString("2b15f856-4b89-4fb7-8e6a-bd7b24ecac1d");
    private final ServidorRepository servidorRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final EmailService emailService;

    private final BCryptPasswordEncoder passwordEncoder;


    public List<Servidor> getServidores() {
        return this.servidorRepository.findAll();
    }

    public Servidor saveServidor(Servidor servidor) {
        // Verifica se o email já está sendo usado por outro servidor
        if (servidorRepository.existsByEmailAndIdServidorNot(servidor.getEmail(), servidor.getIdServidor()))
            throw new BusinessException(MSG_ERRO_EMAIL_JA_EM_USO_POR_OUTRO_SERVIDOR);

        //Verificar se já existe usuario
        Optional<Usuario> usuarioOpt = this.userRepository.findByUsername(servidor.getEmail());


        Usuario usuario = new Usuario();
        String token = null;
        if (usuarioOpt.isPresent()) usuario = usuarioOpt.get();
        else {
            token = UUID.randomUUID().toString();
            usuario.setPassword(passwordEncoder.encode(token));
            usuario.setUsername(servidor.getEmail());
        }

        servidor.setUsuario(usuario);
        servidor = servidorRepository.save(servidor);

        // Envia e-mail para o servidor contendo a senha do seu login
        emailService.enviarEmail(servidor.getEmail(), MSG_SENHA_CRIADA_PIT_SERVIDOR, token);

        this.setRoleServidor(servidor);

        return servidor;
    }

    public Servidor updateServidor(Servidor servidor) {

        Servidor servidorOriginal = this.getServidor(servidor.getIdServidor());

        servidor.setAdministrador(servidorOriginal.isAdministrador());

        if(servidor.getIdServidor().equals(idAdministrador)) {
            throw new BusinessException("Não é permitido alterar o administrador principal");
        }

        // Verifica se o email já está sendo usado por outro servidor
        if (servidorRepository.existsByEmailAndIdServidorNot(servidor.getEmail(), servidor.getIdServidor()))
            throw new BusinessException(MSG_ERRO_EMAIL_JA_EM_USO_POR_OUTRO_SERVIDOR);

        servidor.setUsuario(servidorOriginal.getUsuario());
        servidorRepository.save(servidor);

        return servidor;
    }

    public void deleteServidor(UUID id) {
        if(id.equals(idAdministrador)) {
            throw new BusinessException("Não é permitido excluir o administrador principal");
        }
        this.servidorRepository.deleteById(id);
    }

    public Servidor getServidor(UUID id) {
        return servidorRepository.findById(id).orElseThrow(() -> new NotFoundException(MSG_ERRO_SERVIDOR_NAO_ENCONTRADO));
    }

    public void setRoleServidor(Servidor servidor) {
        Usuario usuario = servidor.getUsuario();
        Optional<Role> roleServidor = roleRepository.findByRoleName(RoleNameEnum.SERVIDOR);

        List<Role> roles = usuario.getRoles();
        roles.add(roleServidor.get());
        usuario.setRoles(roles);

        this.userRepository.save(usuario);
    }

    public Servidor getServidorByUserId(UUID userId) {
        return servidorRepository.findByUsuarioUserId(userId).orElseThrow(() -> new NotFoundException(MSG_ERRO_SERVIDOR_NAO_ENCONTRADO));
    }
}
