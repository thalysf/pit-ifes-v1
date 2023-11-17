import {ColumnDataType, Resource} from "src/app/shared/model/resource";
import {professorResource} from "../professores/professores.resource";

export const coordenadoresResource: Resource = {
    nomeEntidade: {
        singular: 'Coordenador',
        plural: 'Coordenadores'
    },
    fieldPk: 'idServidor',
    route: {
        url: 'coordenadores'
    },
    colunas: [
        {
            field: 'tituloProjeto',
            label: 'Nome',
            required: true
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
    ],
    availableReferences: [
        {
            ...professorResource,
            route: {
                url: 'professores',
                targetUrl: 'coordenadores'
            },
            tipoAssociacao: 'ManyToMany'
        }
    ]
}
