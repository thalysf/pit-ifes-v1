import {TipoAtividadePortariaEnum} from "../enum/tipoPortariaEnum";

export interface Portaria {
    idPortaria: string,
    nome: string,
    tipoAtividade: TipoAtividadePortariaEnum,
    descricao: string,
    dataInicioVigencia: string,
    dataFimVigencia: string,
    cargaHorariaMinima: number,
    cargaHorariaMaxima: number,
}
