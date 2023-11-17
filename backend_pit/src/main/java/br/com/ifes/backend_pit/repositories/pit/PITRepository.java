package br.com.ifes.backend_pit.repositories.pit;

import br.com.ifes.backend_pit.models.pit.PIT;
import org.bouncycastle.util.Times;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PITRepository extends JpaRepository<PIT, UUID> {
    Optional<PIT> findByProfessorIdServidorAndPeriodo(UUID idServidor, String periodo);

    Optional<PIT> findByProfessorIdServidorAndDataEntregaEquals(UUID idServidor, Timestamp dataEntrega);

    @Query("SELECT pit from PIT pit " +
            "LEFT JOIN pit.professor professor " +
            "LEFT JOIN professor.usuario usuario " +
            "WHERE usuario.userId = :idUsuario " +
            "AND pit.aprovado = false ")
    Optional<PIT> findPitEmAberto(UUID idUsuario);
    Optional<PIT> findByProfessorUsuarioUserIdAndDataEntregaEquals(UUID idUsuario, Timestamp dataEntrega);

    List<PIT> findAllByProfessorIdServidorOrderByAprovadoAscEmRevisaoAsc(UUID idServidor);

    List<PIT> findAllByEmRevisaoOrderByDataEntregaDesc(Boolean emRevisao);
    List<PIT> findAllByAprovadoOrderByDataEntregaDesc(Boolean aprovado);

    Optional<PIT> findPITByProfessorIdServidorAndPeriodoEquals(UUID idServidor, String periodo);
}
