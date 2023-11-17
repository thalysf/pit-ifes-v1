package br.com.ifes.backend_pit.enums;

import java.io.Serializable;

public enum TipoPortariaEnum implements Serializable {
    ATIVIDADE_APOIO_ENSINO("ATIVIDADE_APOIO_ENSINO", "Atividade de Apoio ao Ensino"),
    ATIVIDADE_PESQUISA("ATIVIDADE_PESQUISA", "Atividade de Pesquisa"),
    ATIVIDADE_EXTENSAO("ATIVIDADE_EXTENSAO", "Atividade de Extensão"),
    ATIVIDADE_GESTAO("ATIVIDADE_GESTAO", "Atividade de Gestão"),
    ATIVIDADE_REPRESENTACAO("ATIVIDADE_REPRESENTACAO", "Atividade de Representação"),
    OUTRAS_ATIVIDADES("OUTRAS_ATIVIDADES", "Outras Atividades");

    private final String id;
    private final String nome;

    private TipoPortariaEnum(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    TipoPortariaEnum(String id){
        TipoPortariaEnum tipoPortariaEnum = TipoPortariaEnum.valueOf(id);
        this.id = tipoPortariaEnum.getId();
        this.nome = tipoPortariaEnum.getNome();
    }

    public String getNome() {
        return nome;
    }

    public String getId() {
        return id;
    }
}
