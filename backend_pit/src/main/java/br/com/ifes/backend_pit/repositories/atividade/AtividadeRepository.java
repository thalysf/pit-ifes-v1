package br.com.ifes.backend_pit.repositories.atividade;

import br.com.ifes.backend_pit.enums.TipoAtividadeEnum;
import br.com.ifes.backend_pit.models.atividades.Atividade;
import br.com.ifes.backend_pit.models.cadastros.ParticipacaoProjeto;
import br.com.ifes.backend_pit.models.cadastros.Portaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, UUID> {
    List<Atividade> findByTipoAtividadeOrderByNumeroOrdem(TipoAtividadeEnum tipoAtividade);

    Atividade findAtividadeByNomeAtividadeContainingIgnoreCase(String nome);


    @Query("SELECT portaria from Portaria portaria " +
            "LEFT JOIN portaria.atividade atividade " +
            "LEFT JOIN portaria.professores professor " +
            "WHERE atividade.idAtividade = :idAtividade " +
            "AND professor.idServidor = :idProfessor " +
            "AND portaria.dataInicioVigencia <= current_timestamp " +
            "AND portaria.dataFimVigencia >= current_timestamp ")
    List<Portaria> findPortariasProfessorAtividade(UUID idProfessor, UUID idAtividade);

    @Query("SELECT participacao from ParticipacaoProjeto participacao " +
            "LEFT JOIN participacao.professor professor " +
            "LEFT JOIN participacao.projeto projeto " +
            "LEFT JOIN projeto.atividades atividade " +
            "WHERE atividade.idAtividade = :idAtividade " +
            "AND professor.idServidor = :idProfessor " +
            "AND projeto.dataInicioVigencia <= current_timestamp " +
            "AND projeto.dataFimVigencia >= current_timestamp ")
    List<ParticipacaoProjeto> findProjetosProfessorAtividade(UUID idProfessor, UUID idAtividade);
}
