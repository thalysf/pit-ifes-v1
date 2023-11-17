import {ColumnDataType, Resource} from "src/app/shared/model/resource";
import {portariaResource} from "../../portaria/portaria.resource";
import {Atividade, TipoDetalhamentoEnumName} from "../../../shared/model/respostaAtividade";

export const outrasResource: Resource = {
    nomeEntidade: {
        singular: 'Outras atividades',
        plural: 'Outras atividades'
    },
    fieldPk: 'idAtividade',
    route: {
        url: 'atividades/outras',
    },
    colunas: [
        {
            field: 'numeroOrdem',
            label: 'Ordem',
            type: ColumnDataType.Number,
            width: 80
        },
        {
            field: 'nomeAtividade',
            required: true
        },
        {
            field: 'tipoDetalhamento',
            type: ColumnDataType.Select,
            selectConfiguration: {
                valueExpr: 'id',
                displayExpr: 'nome',
                defaultData: [
                    {id: 'DETALHAMENTO_PORTARIA', nome: 'Informar Portarias'}
                ]
            },
            required: true,
            calculateDisplayValue: (entity: Atividade) => {
                // @ts-ignore
                return TipoDetalhamentoEnumName[entity.tipoDetalhamento];
            }
        },
        {
            field: 'abaixoDoSubTotal',
            label: 'Exibir abaixo do subtotal',
            type: ColumnDataType.Checkbox,
            unAvailableOnList: true
        }
    ],
    availableReferences: [
        {
            ...portariaResource,
            tipoAssociacao: 'ManyToMany',
            route: {
                url: 'portarias/outras',
                targetUrl: 'atividades'
            },
        }
    ]
}
