package br.com.ifes.backend_pit.services.usuarios;

import br.com.ifes.backend_pit.email.EmailService;
import br.com.ifes.backend_pit.enums.RoleNameEnum;
import br.com.ifes.backend_pit.exception.BusinessException;
import br.com.ifes.backend_pit.exception.NotFoundException;
import br.com.ifes.backend_pit.models.usuarios.Role;
import br.com.ifes.backend_pit.models.usuarios.Servidor;
import br.com.ifes.backend_pit.models.usuarios.Usuario;
import br.com.ifes.backend_pit.repositories.usuarios.ProfessorRepository;
import br.com.ifes.backend_pit.repositories.usuarios.RoleRepository;
import br.com.ifes.backend_pit.repositories.usuarios.ServidorRepository;
import br.com.ifes.backend_pit.repositories.usuarios.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.ifes.backend_pit.constants.EmailContants.MSG_SENHA_CRIADA_PIT_PROFESSOR;
import static br.com.ifes.backend_pit.constants.ErrorConstants.PROFESSOR_ERROS.MSG_ERRO_EMAIL_JA_EM_USO_POR_OUTRO_PROFESSOR;
import static br.com.ifes.backend_pit.constants.ErrorConstants.PROFESSOR_ERROS.MSG_ERRO_PROFESSOR_INEXISTENTE_UPDATE;
import static br.com.ifes.backend_pit.constants.ErrorConstants.PROFESSOR_ERROS.MSG_ERRO_PROFESSOR_NAO_ENCONTRADO;

@Service
@AllArgsConstructor
@Transactional
public class ProfessorService {
    private final ProfessorRepository professorRepository;
    private final ServidorRepository servidorRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final EmailService emailService;

    private final BCryptPasswordEncoder passwordEncoder;


    public List<Servidor> getProfessores() {
        return this.servidorRepository.findServidoresByRoleName(RoleNameEnum.PROFESSOR);
    }

    public Servidor saveProfessor(Servidor professor) {
        // Verifica se o email já está sendo usado por outro professor
        if (professorRepository.existsByEmailAndIdServidorNot(professor.getEmail(), professor.getIdServidor()))
            throw new BusinessException(MSG_ERRO_EMAIL_JA_EM_USO_POR_OUTRO_PROFESSOR);

        //Verificar se já existe usuario
        Optional<Usuario> usuarioOpt = this.userRepository.findByUsername(professor.getEmail());


        Usuario usuario = new Usuario();
        String token = null;
        if (usuarioOpt.isPresent()) usuario = usuarioOpt.get();
        else {
            token = UUID.randomUUID().toString();
            usuario.setPassword(passwordEncoder.encode(token));
            usuario.setUsername(professor.getEmail());
        }

        professor.setUsuario(usuario);
        professor = professorRepository.save(professor);

        // Envia e-mail para o professor contendo a senha do seu login
        emailService.enviarEmail(professor.getEmail(), MSG_SENHA_CRIADA_PIT_PROFESSOR, token);

        this.setRoleProfessor(professor);
        this.setRoleAdministrador(professor);

        return professor;
    }

    public Servidor updateProfessor(Servidor professor) {
        //Verificar se o professor existe
        Optional<Servidor> professorOpt = this.professorRepository.findById(professor.getIdServidor());

        if (professorOpt.isEmpty()) throw new BusinessException(MSG_ERRO_PROFESSOR_INEXISTENTE_UPDATE);

        // Verifica se o email já está sendo usado por outro professor
        if (professorRepository.existsByEmailAndIdServidorNot(professor.getEmail(), professor.getIdServidor()))
            throw new BusinessException(MSG_ERRO_EMAIL_JA_EM_USO_POR_OUTRO_PROFESSOR);

        professor.setUsuario(professorOpt.get().getUsuario());

        this.setRoleAdministrador(professor);

        return professorRepository.save(professor);
    }

    public void deleteProfessor(UUID id) {
        this.professorRepository.deleteById(id);
    }

    public Servidor getProfessor(UUID id) {
        return professorRepository.findById(id).orElseThrow(() -> new NotFoundException(MSG_ERRO_PROFESSOR_NAO_ENCONTRADO));
    }

    public void setRoleProfessor(Servidor professor) {
        Usuario usuario = professor.getUsuario();
        Optional<Role> roleProfessor = roleRepository.findByRoleName(RoleNameEnum.PROFESSOR);

        List<Role> roles = usuario.getRoles();
        roles.add(roleProfessor.get());
        usuario.setRoles(roles);

        this.userRepository.save(usuario);
    }

    public void setRoleAdministrador(Servidor professor){
        Usuario usuario = professor.getUsuario();

        if(professor.isAdministrador()){
            Role roleDiretor = roleRepository.findByRoleName(RoleNameEnum.DIRETOR).orElseThrow(() -> new NotFoundException("Role não encontrada"));
            usuario.getRoles().add(roleDiretor);
        } else {
            usuario.getRoles().removeIf(role -> role.getRoleName().equals(RoleNameEnum.DIRETOR));
        }

        this.userRepository.save(usuario);
    }
}
