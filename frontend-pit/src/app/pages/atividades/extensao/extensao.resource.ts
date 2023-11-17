import {ColumnDataType, Resource} from "src/app/shared/model/resource";
import {portariaResource} from "../../portaria/portaria.resource";
import {projetoResource} from "../../projeto/projeto.resource";
import {Atividade, TipoDetalhamentoEnum, TipoDetalhamentoEnumName} from "../../../shared/model/respostaAtividade";
import {
    dataToDouble,
    doubleToCargaHorariaComponent,
    doubleToDate,
    doubleToDateStringFormat, stringHourFormatToDouble
} from "../../../shared/comum/cargaHoraria.resource";
import {HoraConfiguration} from "../../../shared/components/carga-horaria-editor/carga-horaria-editor.component";

export const extensaoResource: Resource = {
    nomeEntidade: {
        singular: 'Atividade de Extensão',
        plural: 'Atividades de Extensão'
    },
    fieldPk: 'idAtividade',
    route: {
        url: 'atividades/extensao',
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
                route: 'atividades/extensao/tipoDetalhamento',
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
                return entity?.tipoDetalhamento == TipoDetalhamentoEnum.NENHUM || entity?.tipoDetalhamento == TipoDetalhamentoEnum.DETALHAMENTO_ALUNO;
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
                return entity?.tipoDetalhamento == TipoDetalhamentoEnum.NENHUM || entity?.tipoDetalhamento == TipoDetalhamentoEnum.DETALHAMENTO_ALUNO;
            }
        }
    ],
    availableReferences: [
        {
            ...projetoResource,
            tipoAssociacao: 'ManyToMany',
            route: {
                url: 'projetos',
                targetUrl: 'atividades'
            },
            isVisible: (entity: any) => {
                return entity?.tipoDetalhamento == TipoDetalhamentoEnum.DETALHAMENTO_PROJETO;
            }
        },
        {
            ...portariaResource,
            tipoAssociacao: 'ManyToMany',
            route: {
                url: 'portarias/extensao',
                targetUrl: 'atividades'
            },
            isVisible: (entity: any) => {
                return entity?.tipoDetalhamento == TipoDetalhamentoEnum.DETALHAMENTO_PORTARIA;
            }
        }
    ]
}
