import {Pit} from "./pit";
import {ComponenteCurricular} from "./componenteCurricular";
import {Portaria} from "./portaria";
import {HoraConfiguration} from "../components/carga-horaria-editor/carga-horaria-editor.component";

export enum TipoDetalhamentoEnumName {
    NENHUM = 'Informar apenas carga horaria',
    DETALHAMENTO_PROJETO = 'Informar projetos',
    DETALHAMENTO_PORTARIA = 'Informar portarias',
    DETALHAMENTO_ALUNO = 'Informar alunos',
    DETALHAMENTO_AULA = 'Informar componentes curriculares',
}

export enum TipoDetalhamentoEnum {
    NENHUM = 'NENHUM',
    DETALHAMENTO_PROJETO = 'DETALHAMENTO_PROJETO',
    DETALHAMENTO_PORTARIA = 'DETALHAMENTO_PORTARIA',
    DETALHAMENTO_ALUNO = 'DETALHAMENTO_ALUNO',
    DETALHAMENTO_AULA = 'DETALHAMENTO_AULA',
}

export interface Atividade {
    idAtividade: string,
    tipoAtividade: string,
    nomeAtividade: string,
    tipoDetalhamento: TipoDetalhamentoEnum,
    cargaHorariaMinima?: number,
    cargaHorariaMaxima?: number,
    portarias?: Portaria[]
}

export interface RespostaAtividade {
    idRespostaAtividade: string,
    cargaHorariaSemanal: number,
    pit: Pit,
    atividade: Atividade
}

export interface RespostaAtividadeComponente {
    idRespostaAtividade?: string,
    cargaHorariaSemanal: HoraConfiguration,
    pit?: Pit,
    atividade?: Atividade
}
