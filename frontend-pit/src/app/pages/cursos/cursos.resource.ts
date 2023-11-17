import {Resource} from "src/app/shared/model/resource";
import {componenteCurricularResource} from "../componenteCurricular/componente-curricular.resource";
import {ApiService} from "../../shared/services/api.service";
import {ActivatedRoute, Router} from "@angular/router";

export const cursoResource: Resource = {
    nomeEntidade: {
        singular: 'Curso',
        plural: 'Cursos'
    },
    fieldPk: 'idCurso',
    route: {
        url: 'cursos',
    },
    colunas: [
        {
            field: 'nome',
            required: true
        },
    ],
    availableReferences: [
        {
            ...componenteCurricularResource,
            tipoAssociacao: 'OneToMany'
        }
    ],
    actions: [
        {
            type: 'success',
            onClick: (apiService: ApiService, router: Router, route: ActivatedRoute) => importarArquivo(apiService, router, route),
            hint: 'Importar arquivo',
            icon: 'upload',
            text: '',
            mode: 'batch'
        }
    ]
}

function importarArquivo(apiService: ApiService, router: Router, route: ActivatedRoute){
    router.navigate([`importar`], {relativeTo: route});
}
