import {ColumnDataType, Resource} from "src/app/shared/model/resource";


export const perfilResource: Resource = {
    nomeEntidade: {
        plural: 'Perfis',
        singular: 'Perfil'
    },
    fieldPk: 'idProfessor',
    route: {
        url: 'professores'
    },
    disableBefore: true,
    disableDelete: true,
    colunas: [
        {
            field: 'nome',
            required: true
        },
        {
            field: 'email',
            required: true,
            validation: [
                {
                    type: 'email',
                    message: 'Digite um e-mail válido.'
                }
            ]
        },
        {
            field: 'campus',
            visible: false,
        },
        {
            field: 'departamento',
            visible: false,
        },
        {
            field: 'siape',
            label: 'SIAPE',
            validation: [
                {
                    type: 'numeric',
                    message: 'SIAPE deve conter apenas digitos numéricos'
                }
            ]
        },
        {
            field: 'jornadaTrabalho',
            label: 'Jornada de Trabalho',
            type: ColumnDataType.Number,
            visible: false,
        },
        {
            field: 'areaPrincipalAtuacao',
            label: 'Atuação',
            visible: false,
        },
        {
            field: 'titulacao',
            label: 'Titulação',
            visible: false,
        },
        {
            field: 'possuiAfastamento',
            label: 'Possui Afastamento',
            visible: false,
            type: ColumnDataType.Checkbox,
            colSpan: 1,
        },
        {
            field: 'efetivo',
            type: ColumnDataType.Checkbox,
            visible: false,
            colSpan: 1
        },
    ],
    availableReferences: []
}
