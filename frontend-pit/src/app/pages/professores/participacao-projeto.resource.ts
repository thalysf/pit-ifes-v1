import { Resource } from "src/app/shared/model/resource";

export const participacaoProjetoResource: Resource = {
    nomeEntidade: {
        singular: 'Professor',
        plural: 'Professores'
    },
    fieldPk: 'idServidor',
    route: {
        url: 'professores',
    },
    colunas: [
        {
            field: 'professor.nome',
            label: 'Nome',
        },
        {
            field: 'professor.mail',
            required: true,
            label: 'E-mail',
        },
        {
            field: 'professor.campus',
            visible: false,
            label: 'Campus',
        },
        {
            field: 'professor.departamento',
            visible: false,
            label: 'Departamento',
        },
        {
            field: 'professor.siape',
            label: 'SIAPE',
            visible: false,
        },
    ],
    availableReferences: []
}
