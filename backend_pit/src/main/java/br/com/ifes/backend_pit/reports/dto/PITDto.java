package br.com.ifes.backend_pit.reports.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import br.com.ifes.backend_pit.reports.dto.atividades.ensino.apoio_ensino.AtividadesApoioEnsino;
import br.com.ifes.backend_pit.reports.dto.atividades.ensino.aula.Aulas;
import br.com.ifes.backend_pit.reports.dto.atividades.ensino.detalhamento_componente_curricular.DetalhamentosComponenteCurricular;
import br.com.ifes.backend_pit.reports.dto.atividades.ensino.detalhamento_projetos.DetalhamentosProjetos;
import br.com.ifes.backend_pit.reports.dto.atividades.ensino.mediacao_pedagogica.MediacoesPedagogicas;
import br.com.ifes.backend_pit.reports.dto.atividades.ensino.planejamento_manutencao_ensino.AtividadesPlanejamentoManutencaoEnsino;
import br.com.ifes.backend_pit.reports.dto.atividades.extensao.atividade.AtividadesExtensao;
import br.com.ifes.backend_pit.reports.dto.atividades.extensao.detalhamento.DetalhamentosExtensao;
import br.com.ifes.backend_pit.reports.dto.atividades.gestao.AtividadesGestao;
import br.com.ifes.backend_pit.reports.dto.atividades.outras.OutrasAtividades;
import br.com.ifes.backend_pit.reports.dto.atividades.pesquisa.atividade.AtividadesPesquisa;
import br.com.ifes.backend_pit.reports.dto.atividades.pesquisa.detalhamento.DetalhamentosPesquisas;
import br.com.ifes.backend_pit.reports.dto.atividades.representacao.AtividadesRepresentacao;
import br.com.ifes.backend_pit.reports.dto.cadastro.DadosCadastraisDTO;
import br.com.ifes.backend_pit.reports.dto.resumo.ResumoDTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PITDto {
    String periodo;

    String idPit;
    DadosCadastraisDTO dadosCadastraisDTO;
    String resumoCargaHorariaTotal;

    ResumoDTO resumoDTO;

    Aulas aulas;

    AtividadesPlanejamentoManutencaoEnsino atividadesPlanejamentoManutencaoEnsino;

    AtividadesApoioEnsino atividadesApoioEnsino;

    DetalhamentosComponenteCurricular detalhamentosComponenteCurricular;

    DetalhamentosProjetos detalhamentosProjetos;

    MediacoesPedagogicas mediacoesPedagogicas;

    AtividadesPesquisa atividadesPesquisa;

    DetalhamentosPesquisas detalhamentosPesquisas;

    AtividadesExtensao atividadesExtensao;

    DetalhamentosExtensao detalhamentosExtensao;

    AtividadesGestao atividadesGestao;

    AtividadesRepresentacao atividadesRepresentacao;

    OutrasAtividades outrasAtividades;
}
