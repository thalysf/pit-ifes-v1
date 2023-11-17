package br.com.ifes.backend_pit.enums;

import java.io.Serializable;

public enum TipoDetalhamentoEnum implements Serializable {

    DETALHAMENTO_PORTARIA("DETALHAMENTO_PORTARIA", "Informar Portarias"),
    DETALHAMENTO_PROJETO("DETALHAMENTO_PROJETO", "Informar Projetos"),
    DETALHAMENTO_AULA("DETALHAMENTO_AULA", "Informar Componentes Curriculares"),
    DETALHAMENTO_ALUNO("DETALHAMENTO_ALUNO", "Informar Alunos"),
    NENHUM("NENHUM", "Informar apenas a carga horária");

    private final String id;
    private final String nome;

    private TipoDetalhamentoEnum(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    TipoDetalhamentoEnum(String id){
        TipoParticipacaoProjetoEnum tipoParticipacaoProjetoEnum = TipoParticipacaoProjetoEnum.valueOf(id);
        this.id = tipoParticipacaoProjetoEnum.getId();
        this.nome = tipoParticipacaoProjetoEnum.getNome();
    }

    public String getNome() {
        return nome;
    }

    public String getId() {
        return id;
    }
}
