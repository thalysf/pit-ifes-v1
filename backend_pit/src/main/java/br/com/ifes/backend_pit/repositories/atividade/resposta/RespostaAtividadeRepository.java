package br.com.ifes.backend_pit.repositories.atividade.resposta;

import br.com.ifes.backend_pit.enums.TipoAtividadeEnum;
import br.com.ifes.backend_pit.models.atividades.Atividade;
import br.com.ifes.backend_pit.models.atividades.resposta.RespostaAtividade;
import br.com.ifes.backend_pit.models.cadastros.ComponenteCurricular;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RespostaAtividadeRepository extends JpaRepository<RespostaAtividade, UUID> {

    @Query("SELECT ra " +
            "FROM RespostaAtividade ra " +
            "JOIN ra.detalhamentoComponentesCurriculares detalhamento " +
            "WHERE ra.pit.idPIT = :idPIT " +
            "AND detalhamento.componenteCurricular.idComponenteCurricular = :idComponenteCurricular " +
            "AND ra.atividade.idAtividade = :idAula")
    Optional<RespostaAtividade> findByPitIdAndComponenteCurricularId(UUID idPIT, UUID idComponenteCurricular, UUID idAula);

    Optional<RespostaAtividade> findByPitIdPITAndAtividadeIdAtividade(UUID idPit, UUID idAtividade);

    @Query("SELECT cc FROM ComponenteCurricular cc " +
            "WHERE NOT EXISTS (" +
            "   SELECT 1 FROM DetalhamentoComponenteCurricular dc " +
            "   WHERE dc.componenteCurricular = cc " +
            "   AND dc.respostaAtividade.pit.idPIT = :idPit" +
            "   AND dc.respostaAtividade.atividade.idAtividade = :idAtividade" +
            ")")
    Optional<List<ComponenteCurricular>> findComponenteNaoSelecionados(UUID idPit, UUID idAtividade);

    @Query("SELECT DISTINCT atividade from  Atividade atividade " +
            "LEFT JOIN atividade.portarias portaria " +
            "LEFT JOIN portaria.professores professoresPortaria " +
            "LEFT JOIN atividade.projetos projeto " +
            "LEFT JOIN projeto.participacaoProjetos partProj " +
            "WHERE atividade.tipoAtividade = :tipoAtividade " +
            "AND (atividade.tipoDetalhamento = 'NENHUM' " +
            "OR atividade.tipoDetalhamento = 'DETALHAMENTO_ALUNO' " +
            "OR atividade.tipoDetalhamento = 'DETALHAMENTO_AULA' " +
            "OR (atividade.tipoDetalhamento = 'DETALHAMENTO_PROJETO' AND partProj.professor.idServidor = :idServidor) " +
            "AND ((:periodo = '1' AND (YEAR(projeto.dataFimVigencia) >= YEAR(current_timestamp) AND (MONTH(projeto.dataFimVigencia) > 2 or YEAR(projeto.dataFimVigencia) > YEAR(current_timestamp)))) OR (:periodo = '2' AND (YEAR(projeto.dataFimVigencia) >= YEAR(current_timestamp) AND (MONTH(projeto.dataFimVigencia) > 8 or YEAR(projeto.dataFimVigencia) > YEAR(current_timestamp)))))" +
            "OR (atividade.tipoDetalhamento = 'DETALHAMENTO_PORTARIA' AND professoresPortaria.idServidor = :idServidor) " +
            "AND ((:periodo = '1' AND (YEAR(portaria.dataFimVigencia) >= YEAR(current_timestamp) AND (MONTH(portaria.dataFimVigencia) > 2 or YEAR(portaria.dataFimVigencia) > YEAR(current_timestamp)))) OR (:periodo = '2' AND (YEAR(portaria.dataFimVigencia) >= YEAR(current_timestamp) AND (MONTH(portaria.dataFimVigencia) > 8 or YEAR(portaria.dataFimVigencia) > YEAR(current_timestamp))))))")
    List<Atividade> listarAtividadesProfessor(UUID idServidor, TipoAtividadeEnum tipoAtividade, String periodo);


    Optional<RespostaAtividade> findByAtividadeIdAtividadeAndPitIdPIT(UUID idAtividade, UUID idPIT);

    List<RespostaAtividade> findAllByPitIdPITAndAtividadeTipoAtividade(UUID idPit, TipoAtividadeEnum tipoAtividadeEnum);

    List<RespostaAtividade> findAllByPitIdPIT(UUID idPit);
}
