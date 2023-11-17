import {ColumnDataType, Resource} from "src/app/shared/model/resource";
import {portariaResource} from "../../portaria/portaria.resource";
import {Atividade, TipoDetalhamentoEnum, TipoDetalhamentoEnumName} from "../../../shared/model/respostaAtividade";
import {
    doubleToCargaHorariaComponent,
    doubleToDateStringFormat,
    stringHourFormatToDouble
} from "../../../shared/comum/cargaHoraria.resource";
import {HoraConfiguration} from "../../../shared/components/carga-horaria-editor/carga-horaria-editor.component";

export const apoioEnsinoResource: Resource = {
    nomeEntidade: {
        singular: 'Atividade de Apoio ao Ensino',
        plural: 'Atividades de Apoio ao Ensino'
    },
    fieldPk: 'idAtividade',
    route: {
        url: 'atividades/apoioEnsino',
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
                route: 'atividades/apoioEnsino/tipoDetalhamento',
            },
            required: true,
            calculateDisplayValue: (entity: Atividade) => {
                // @ts-ignore
                return TipoDetalhamentoEnumName[entity.tipoDetalhamento];
            }
        },
        {
            field: 'cargaHorariaMinima',
            label: 'Carga Horaria Min.',
            required: true,
            type: ColumnDataType.CargaHoraria,
            getValueBeforEntity: (double: number) => doubleToCargaHorariaComponent(double),
            getValueBeforSave: (carga: HoraConfiguration) => stringHourFormatToDouble(carga.id),
            calculateDisplayValue: (entity: any) => doubleToDateStringFormat(entity.cargaHorariaMinima),
            visibleForm: (entity: any) => {
                return entity?.tipoDetalhamento == TipoDetalhamentoEnum.NENHUM
                    || entity?.tipoDetalhamento == TipoDetalhamentoEnum.DETALHAMENTO_ALUNO
                    || entity?.tipoDetalhamento == TipoDetalhamentoEnum.DETALHAMENTO_AULA;
            },
        },
        {
            field: 'cargaHorariaMaxima',
            label: 'Carga Horaria Max.',
            required: true,
            type: ColumnDataType.CargaHoraria,
            getValueBeforEntity: (double: number) => doubleToCargaHorariaComponent(double),
            getValueBeforSave: (carga: HoraConfiguration) => stringHourFormatToDouble(carga.id),
            calculateDisplayValue: (entity: any) => doubleToDateStringFormat(entity.cargaHorariaMaxima),
            visibleForm: (entity: any) => {
                return entity?.tipoDetalhamento == TipoDetalhamentoEnum.NENHUM
                    || entity?.tipoDetalhamento == TipoDetalhamentoEnum.DETALHAMENTO_ALUNO
                    || entity?.tipoDetalhamento == TipoDetalhamentoEnum.DETALHAMENTO_AULA;
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
                url: 'portarias/apoioEnsino',
                targetUrl: 'atividades'
            },
            isVisible: (entity: any) => {
                return entity?.tipoDetalhamento == TipoDetalhamentoEnum.DETALHAMENTO_PORTARIA;
            }
        }
    ]
}
