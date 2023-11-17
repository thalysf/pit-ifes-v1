package br.com.ifes.backend_pit.services.batch;

import br.com.ifes.backend_pit.email.EmailService;
import br.com.ifes.backend_pit.enums.RoleNameEnum;
import br.com.ifes.backend_pit.exception.BusinessException;
import br.com.ifes.backend_pit.models.usuarios.Role;
import br.com.ifes.backend_pit.models.usuarios.Servidor;
import br.com.ifes.backend_pit.models.usuarios.Usuario;
import br.com.ifes.backend_pit.repositories.usuarios.RoleRepository;
import br.com.ifes.backend_pit.repositories.usuarios.ServidorRepository;
import br.com.ifes.backend_pit.repositories.usuarios.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.ifes.backend_pit.constants.EmailContants.MSG_ATUALIZACAO_CADASTRO_PIT_SERVIDOR;
import static br.com.ifes.backend_pit.constants.EmailContants.MSG_SENHA_CRIADA_PIT_SERVIDOR;

@Service
@Transactional
@RequiredArgsConstructor
public class ServidorBatchService {
    private final ServidorRepository servidorRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    private final RoleRepository roleRepository;

    public void processarServidoresBatch(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new HSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }

                if(
                        row.getCell(0).getStringCellValue().equals("")
                        || row.getCell(1).getStringCellValue().equals("")
                ){
                    continue;
                }


                Servidor servidor = new Servidor();
                servidor.setNome(row.getCell(0).getStringCellValue());
                servidor.setEmail(row.getCell(1).getStringCellValue());
                servidor.setCampus(row.getCell(2).getStringCellValue());
                servidor.setDepartamento(row.getCell(3).getStringCellValue());
                servidor.setSiape(String.valueOf(Math.round(row.getCell(4).getNumericCellValue())));
                servidor.setJornadaTrabalho(Double.parseDouble(row.getCell(5).getRichStringCellValue().toString()));
                servidor.setAreaPrincipalAtuacao(row.getCell(6).getStringCellValue());
                servidor.setTitulacao(row.getCell(7).getStringCellValue());
                servidor.setEfetivo(convertSimNaoToBoolean(row.getCell(8).getStringCellValue()));
                servidor.setPossuiAfastamento(convertSimNaoToBoolean(row.getCell(9).getStringCellValue()));

                salvarOuAtualizarServidor(servidor);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("Não foi possível realizar a carga em lote dos servidores " + e.getMessage());
        }
    }

    private boolean convertSimNaoToBoolean(String value) {
        return "sim".equalsIgnoreCase(value);
    }

    private void salvarOuAtualizarServidor(Servidor servidor) {
        Optional<Servidor> servidorExistenteOpt = servidorRepository.findByEmailIgnoreCaseAndSiape(servidor.getEmail(), servidor.getSiape());

        if (servidorExistenteOpt.isPresent()) {
            Servidor servidorExistente = servidorExistenteOpt.get();
            servidorExistente.setNome(servidor.getNome());
            servidorExistente.setCampus(servidor.getCampus());
            servidorExistente.setDepartamento(servidor.getDepartamento());
            servidorExistente.setJornadaTrabalho(servidor.getJornadaTrabalho());
            servidorExistente.setAreaPrincipalAtuacao(servidor.getAreaPrincipalAtuacao());
            servidorExistente.setEfetivo(servidor.isEfetivo());
            servidorExistente.setPossuiAfastamento(servidor.isPossuiAfastamento());

            servidorRepository.save(servidorExistente);

            emailService.enviarEmail(servidorExistente.getEmail(), MSG_ATUALIZACAO_CADASTRO_PIT_SERVIDOR, "Cadastro do servidor " + servidor.getNome() + " atualizado");

            setRoleServidor(servidorExistente);
        } else {
            Optional<Usuario> usuarioOpt = userRepository.findByUsername(servidor.getEmail());
            Usuario usuario = new Usuario();
            String token = null;

            if (usuarioOpt.isPresent()) {
                usuario = usuarioOpt.get();
            } else {
                token = UUID.randomUUID().toString();
                usuario.setPassword(passwordEncoder.encode(token));
                usuario.setUsername(servidor.getEmail());
            }

            servidor.setUsuario(usuario);
            servidorRepository.save(servidor);

            if (token != null) {
                emailService.enviarEmail(servidor.getEmail(), MSG_SENHA_CRIADA_PIT_SERVIDOR, token);
            }

            setRoleServidor(servidor);
        }
    }


    private void setRoleServidor(Servidor servidor) {
        Usuario usuario = servidor.getUsuario();
        Optional<Role> roleServidor = roleRepository.findByRoleName(RoleNameEnum.SERVIDOR);

        List<Role> roles = usuario.getRoles();
        roles.add(roleServidor.get());
        usuario.setRoles(roles);

        this.userRepository.save(usuario);
    }
}
