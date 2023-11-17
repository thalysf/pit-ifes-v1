import {Professor} from "./professor";

export interface Pit{
    idPIT: string,
    aprovado: boolean,
    dataEntrega: string,
    periodo: string,
    professor: Professor
}
