import { ComponenteCurricular } from "./componenteCurricular"
import { Coordenador } from "./coordenador"

export interface Curso {
    idCurso?: string,
    nome: string,
    componentesCurriculares?: ComponenteCurricular[],
    coordenador?: Coordenador
}

