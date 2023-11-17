import { Curso } from "./curso";
import { Professor } from "./professor";

export interface Coordenador {
    professor: Professor,
    cursos: Curso[]
}
