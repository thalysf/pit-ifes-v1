import { ComponenteCurricular } from "./componenteCurricular";

export interface Aula {
    cargaHorariaSemanal: number,
    componenteCurricular: ComponenteCurricular
}

export interface MediacaoPedagogica {
    cargaHorariaSemanal: number,
    componenteCurricular: ComponenteCurricular
}

export interface AtividadeApoioEnsino {
    atividade: string,
    cargaHorariaSemanal: number,
    precisaAprovacao: boolean,
    detalhamentoSimples: DetalhamentoSimplesAtividadeApoio
}

export interface DetalhamentoSimplesAtividadeApoio {
    cargaHorariaSemanal: number,
    componenteCurricular: ComponenteCurricular
}
