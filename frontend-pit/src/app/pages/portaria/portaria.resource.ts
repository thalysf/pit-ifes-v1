import {ColumnDataType, Resource} from "src/app/shared/model/resource";
import {Portaria} from "../../shared/model/portaria";
import {cargaHorariaResourceColumn} from "../../shared/comum/cargaHoraria.resource";
import {TipoAtividadePortariaNomeEnum} from "../../shared/enum/tipoPortariaEnum";
import {ApiService} from "../../shared/services/api.service";
import {ActivatedRoute, Router} from "@angular/router";

export const portariaResource: Resource = {
    nomeEntidade: {
        singular: 'Portaria',
        plural: 'Portarias'
    },
    fieldPk: 'idPortaria',
    route: {
        url: 'portarias'
    },
    colunas: [
        {
            field: 'nome',
            label: 'Número da portaria',
            required: true
        },
        {
            field: 'tipoAtividade',
            label: 'Tipo',
            type: ColumnDataType.Select,
            selectConfiguration: {
                valueExpr: 'id',
                displayExpr: 'nome',
                route: 'portarias/tipoAtividade',
            },
            visible: false,
            calculateDisplayValue: (entity: any) => {
                // @ts-ignore
                return TipoAtividadePortariaNomeEnum[entity['tipoAtividade']];
            },
            required: true,
        },
        {
            field: 'descricao',
            label: 'Descrição',
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
    availableReferences: [],
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

function importarArquivo(apiService: ApiService, router: Router, route: ActivatedRoute) {
    router.navigate([`importar`], {relativeTo: route});
}
