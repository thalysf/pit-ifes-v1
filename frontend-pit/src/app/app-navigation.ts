import {RoleEnum} from "./shared/enum/roleEnum";

export interface NavigationMenu {
    text: string,
    icon?: string,
    path?: string,
    items?: NavigationMenu[],
    permissoes?: RoleEnum[]
}


export const navigation: NavigationMenu[] = [
    {
        text: 'Servidores',
        icon: 'group',
        permissoes: [RoleEnum.DIRETOR],
        path: '/professores',
    },
    {
        text: 'Cadastros',
        icon: 'inserttable',
        permissoes: [RoleEnum.DIRETOR],
        items: [
            {
                text: 'Cursos',
                path: '/cursos',
            },
            {
                text: 'Portarias',
                path: '/portarias',
            },
            {
                text: 'Projetos',
                path: '/projetos',
            },
        ]
    },
    {
        text: 'Atividades',
        icon: 'folder',
        permissoes: [RoleEnum.DIRETOR],
        items: [
            {
                text: 'Apoio ao Ensino',
                path: '/atividades/apoioEnsino',
            },
            {
                text: 'Pesquisa',
                path: '/atividades/pesquisa',
            },
            {
                text: 'Extensão',
                path: '/atividades/extensao',
            },
            {
                text: 'Outras',
                path: '/atividades/outras',
            },
        ]
    },
    {
        text: 'Meus PITs',
        icon: 'textdocument',
        path: 'pits',
        permissoes: [RoleEnum.PROFESSOR],
    },
    {
        text: 'PITs dos Professores',
        icon: 'textdocument',
        permissoes: [RoleEnum.DIRETOR],
        items: [
            {
                text: 'PITs em Revisão',
                path: '/pits/emRevisao',
                permissoes: [RoleEnum.DIRETOR],
            },
            {
                text: 'PITs Aprovados',
                path: '/pits/aprovado',
                permissoes: [RoleEnum.DIRETOR],
            },
        ]
    },
    {
        text: 'Perfil',
        path: `perfil`,
        icon: 'user'
    },
];
