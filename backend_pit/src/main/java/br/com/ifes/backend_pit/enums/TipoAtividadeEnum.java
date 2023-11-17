package br.com.ifes.backend_pit.enums;

public enum TipoAtividadeEnum {
    ATIVIDADE_ENSINO_DEFAULT("ATIVIDADE_ENSINO_DEFAULT", "Atividades geradas pelo sistema"),
    ATIVIDADE_APOIO_AULA("ATIVIDADE_APOIO_AULA", "Atividade - aula"),
    ATIVIDADE_APOIO_ENSINO("ATIVIDADE_APOIO_ENSINO", "Atividade de apoio ao ensino"),
    ATIVIDADE_PESQUISA("ATIVIDADE_PESQUISA", "Atividade de pesquisa"),
    ATIVIDADE_EXTENSAO("ATIVIDADE_EXTENSAO", "Atividade de extensão"),
    ATIVIDADE_GESTAO("ATIVIDADE_GESTAO", "Atividade de gestão"),
    ATIVIDADE_REPRESENTACAO("ATIVIDADE_REPRESENTACAO", "Atividade de representação"),
    OUTRAS_ATIVIDADES("OUTRAS_ATIVIDADES", "Outras atividades");

    private final String id;
    private final String nome;

    private TipoAtividadeEnum(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    TipoAtividadeEnum(String id) {
        TipoAtividadeEnum tipoAtividadeEnum = TipoAtividadeEnum.valueOf(id);
        this.id = tipoAtividadeEnum.getId();
        this.nome = tipoAtividadeEnum.getNome();
    }

    public String getNome() {
        return nome;
    }

    public String getId() {
        return id;
    }
}
