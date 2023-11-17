import {ColumnDataType, Resource} from "src/app/shared/model/resource";
import {
    dataToDouble,
    doubleToCargaHorariaComponent,
    doubleToDate,
    doubleToDateStringFormat, stringHourFormatToDouble
} from "../../shared/comum/cargaHoraria.resource";
import {HoraConfiguration} from "../../shared/components/carga-horaria-editor/carga-horaria-editor.component";

export const componenteCurricularResource: Resource = {
    nomeEntidade: {
        singular: 'Componente Curricular',
        plural: 'Componentes Curriculares'
    },
    fieldPk: 'idComponenteCurricular',
    route: {
        url: 'componentesCurriculares',
        targetUrl: 'cursos'
    },
    colunas: [
        {
            field: 'nome',
            required: true
        },
        {
            field: 'cargaHoraria',
            label: 'Carga Horaria Semanal',
            required: true,
            type: ColumnDataType.CargaHoraria,
            getValueBeforEntity: (double: number) => doubleToCargaHorariaComponent(double),
            getValueBeforSave: (carga: HoraConfiguration) => stringHourFormatToDouble(carga.id),
            calculateDisplayValue: (entity: any) => doubleToDateStringFormat(entity.cargaHoraria)
        },
    ],
    availableReferences: []
}
