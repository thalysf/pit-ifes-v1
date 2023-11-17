export interface Projeto {
    idProjeto: string,
    tituloProjeto: string,
    tipoProjeto: string,
    tipoAcao: string,
    numeroCadastro: string,
    cargaHorariaMinima: number,
    cargaHorariaMaxima: number,
    dataInicioVigencia: Date,
    dataFimVigencia: Date
}

export interface ParticipacaoProjeto {
    projeto: Projeto,
    professor: {idServidor: string},
    idParticipacaoProjeto: string,
}

export enum TipoProjetoEnumName {
    ATIVIDADE_EXTENSAO = 'Extensão',
    ATIVIDADE_ENSINO = 'Ensino',
    ATIVIDADE_PESQUISA = 'Pesquisa'
}
