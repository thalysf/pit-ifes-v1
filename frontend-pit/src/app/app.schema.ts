import { Injectable } from "@angular/core";
import { SchemaService } from "src/app/shared/services/schema-service";
import { cursoResource } from "./pages/cursos/cursos.resource";
import {componenteCurricularResource} from "./pages/componenteCurricular/componente-curricular.resource";
import {professorResource} from "./pages/professores/professores.resource";
import {portariaResource} from "./pages/portaria/portaria.resource";
import {projetoResource} from "./pages/projeto/projeto.resource";
import {apoioEnsinoResource} from "./pages/atividades/apoio-ensino/apoio-ensino.resource";
import {pesquisaResource} from "./pages/atividades/pesquisa/pesquisa.resource";
import {extensaoResource} from "./pages/atividades/extensao/extensao.resource";
import {outrasResource} from "./pages/atividades/outras/outras.resource";

@Injectable()
export class AppSchema extends SchemaService {

    constructor(){
        super({
            professores: {
                ...professorResource
            },
            cursos: {
                ...cursoResource
            },
            componentesCurriculares: {
                ...componenteCurricularResource
            },
            portarias: {
                ...portariaResource
            },
            projetos: {
                ...projetoResource
            },
            apoioEnsino: {
                ...apoioEnsinoResource
            },
            pesquisa: {
                ...pesquisaResource
            },
            extensao: {
                ...extensaoResource
            },
            outras: {
                ...outrasResource
            }
        })
    }
}
