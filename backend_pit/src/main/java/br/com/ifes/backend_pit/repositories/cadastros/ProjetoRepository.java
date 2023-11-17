package br.com.ifes.backend_pit.repositories.cadastros;

import br.com.ifes.backend_pit.enums.TipoProjetoEnum;
import br.com.ifes.backend_pit.models.atividades.Atividade;
import br.com.ifes.backend_pit.models.cadastros.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, UUID> {
    Projeto findAByTituloProjetoContainingIgnoreCaseAndNumeroCadastroContainingIgnoreCase(String tituloProjeto, String numeroCadastro);

    List<Projeto> findByTipoProjetoAndAtividadesNotContains(TipoProjetoEnum tipoProjeto, Atividade atividade);
}
