package br.com.ifes.backend_pit.enums;

public enum TipoProjetoEnum {
    ATIVIDADE_ENSINO("ATIVIDADE_ENSINO", "Atividade de Apoio ao Ensino"),
    ATIVIDADE_PESQUISA("ATIVIDADE_PESQUISA", "Atividade Pesquisa"),
    ATIVIDADE_EXTENSAO("ATIVIDADE_EXTENSAO", "Atividade Extensão");

    private final String id;
    private final String nome;

    private TipoProjetoEnum(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    TipoProjetoEnum(String id) {
        TipoProjetoEnum tipoProjetoEnum = TipoProjetoEnum.valueOf(id);
        this.id = tipoProjetoEnum.getId();
        this.nome = tipoProjetoEnum.getNome();
    }

    public String getNome() {
        return nome;
    }

    public String getId() {
        return id;
    }


}
