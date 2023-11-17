import { Curso } from "./curso";

export interface ComponenteCurricular {
    idComponenteCurricular: string,
    nome: string,
    cargaHoraria: number,
    curso: Curso
}
