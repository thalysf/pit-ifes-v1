package br.com.ifes.backend_pit.enums;

import java.io.Serializable;

public enum TipoParticipacaoProjetoEnum implements Serializable {
    COORDENADOR("COORDENADOR", "Coordenador"),
    ORIENTADOR("ORIENTADOR", "Orientador");

    private final String id;
    private final String nome;

    private TipoParticipacaoProjetoEnum(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    TipoParticipacaoProjetoEnum(String id){
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
