import {ColumnDataType, Resource} from "src/app/shared/model/resource";
import {professorResource} from "../professores/professores.resource";
import {cargaHorariaResourceColumn} from "../../shared/comum/cargaHoraria.resource";
import {Projeto, TipoProjetoEnumName} from "../../shared/model/projeto";
import {ApiService} from "../../shared/services/api.service";
import {ActivatedRoute, Router} from "@angular/router";

export const projetoResource: Resource = {
    nomeEntidade: {
        singular: 'Projeto',
        plural: 'Projetos'
    },
    fieldPk: 'idProjeto',
    route: {
        url: 'projetos'
    },
    colunas: [
        {
            field: 'tituloProjeto',
            label: 'Titulo',
            required: true
        },
        {
            field: 'tipoProjeto',
            label: 'Tipo de Projeto',
            required: true,
            type: ColumnDataType.Select,
            selectConfiguration: {
                valueExpr: 'id',
                displayExpr: 'nome',
                route: 'projetos/tipoProjeto'
            },
            calculateDisplayValue: (entity: Projeto) => {
                // @ts-ignore
                return TipoProjetoEnumName[entity.tipoProjeto];
            }
        },
        {
            field: 'tipoAcao',
            label: 'Tipo da Ação',
            required: true,
        },
        {
            field: 'numeroCadastro',
            label: 'Número de Cadastro e Portaria',
            required: true,
        },
        {
            field: 'dataInicioVigencia',
            label: 'Inicio da Vigência',
            type: ColumnDataType.Date,
            dateType: 'date',
            required: true,
        },
        {
            field: 'dataFimVigencia',
            label: 'Fim da Vigência',
            dateType: 'date',
            type: ColumnDataType.Date,
            required: true,
        },
        ...cargaHorariaResourceColumn
    ],
    availableReferences: [
        {
            ...professorResource,
            route: {
                url: 'professores',
                targetUrl: 'projetos'
            },
            tipoAssociacao: 'ManyToMany'
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
