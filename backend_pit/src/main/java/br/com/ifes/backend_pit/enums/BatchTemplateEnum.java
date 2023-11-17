package br.com.ifes.backend_pit.enums;

public enum BatchTemplateEnum {
    COMPONENTE_CURRICULAR("COMPONENTE_CURRICULAR", "Componente Curricular"),
    SERVIDOR("SERVIDOR", "Servidor"),
    PORTARIA("PORTARIA", "Portaria"),
    PROJETO("PROJETO", "Projeto");

    private final String id;
    private final String nome;

    private BatchTemplateEnum(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    BatchTemplateEnum(String id) {
        BatchTemplateEnum batchTemplateEnum = BatchTemplateEnum.valueOf(id);
        this.id = batchTemplateEnum.getId();
        this.nome = batchTemplateEnum.getNome();
    }

    public String getNome() {
        return nome;
    }

    public String getId() {
        return id;
    }


}