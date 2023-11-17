package br.com.ifes.backend_pit.repositories.cadastros;

import br.com.ifes.backend_pit.enums.RoleNameEnum;
import br.com.ifes.backend_pit.enums.TipoPortariaEnum;
import br.com.ifes.backend_pit.models.cadastros.ComponenteCurricular;
import br.com.ifes.backend_pit.models.cadastros.Portaria;
import br.com.ifes.backend_pit.models.usuarios.Servidor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PortariaRepository extends JpaRepository<Portaria, UUID> {
    List<Portaria> findAllByTipoAtividade(TipoPortariaEnum tipoPortariaEnum);

    Portaria findAByNomeContainingIgnoreCaseAndDescricaoContainingIgnoreCase(String nome, String descricao);

    @Query("SELECT professor FROM Servidor professor " +
            "JOIN professor.usuario.roles role " +
            "WHERE role.roleName = :roleName AND NOT EXISTS (" +
            "  SELECT 1 FROM Portaria p " +
            " JOIN p.professores pp " +
            "  WHERE pp.idServidor = professor.idServidor " +
            "    AND p.idPortaria = :idPortaria" +
            ")")
    List<Servidor> findProfessoresNaoParticipantes(UUID idPortaria, RoleNameEnum roleName);
}
