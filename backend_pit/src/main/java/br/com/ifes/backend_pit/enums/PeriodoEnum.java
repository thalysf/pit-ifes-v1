package br.com.ifes.backend_pit.enums;

public enum PeriodoEnum {
    PERIODO_01("1", "Primeiro Período"),
    PERIODO_02("2", "Segundo Período");

    private final String id;
    private final String nome;

    private PeriodoEnum(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public String getId() {
        return id;
    }

    public String getPeriodo() {
        return id;
    }

    public static PeriodoEnum fromId(String id) {
        for (PeriodoEnum periodo : PeriodoEnum.values()) {
            if (periodo.getId().equals(id)) {
                return periodo;
            }
        }
        throw new IllegalArgumentException("Invalid id: " + id);
    }
}
