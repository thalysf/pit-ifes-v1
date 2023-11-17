package br.com.ifes.backend_pit.repositories.cadastros;

import br.com.ifes.backend_pit.enums.RoleNameEnum;
import br.com.ifes.backend_pit.models.cadastros.ParticipacaoProjeto;
import br.com.ifes.backend_pit.models.usuarios.Servidor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ParticipacaoProjetoRepository extends JpaRepository<ParticipacaoProjeto, UUID> {
    List<ParticipacaoProjeto> findAllByProjetoIdProjeto(UUID idProjeto);

    @Query("SELECT professor FROM Servidor professor " +
            "JOIN professor.usuario.roles role " +
            "WHERE role.roleName = :roleName AND NOT EXISTS (" +
            "  SELECT 1 FROM ParticipacaoProjeto pp " +
            "  WHERE pp.professor.idServidor = professor.idServidor " +
            "    AND pp.projeto.idProjeto = :idProjeto" +
            ")")
    List<Servidor> findProfessoresNaoParticipantes(@Param("idProjeto") UUID idProjeto, RoleNameEnum roleName);


    Optional<ParticipacaoProjeto> findByProjetoIdProjetoAndProfessorIdServidor(UUID idProjeto, UUID idServidor);

    long deleteByProjetoIdProjetoAndProfessorIdServidor(UUID idProjeto, UUID idServidor);
}
