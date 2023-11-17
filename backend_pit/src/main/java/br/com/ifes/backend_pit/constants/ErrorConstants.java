package br.com.ifes.backend_pit.constants;

public class ErrorConstants {
    public static class PROFESSOR_ERROS {
        public static final String MSG_ERRO_PROFESSOR_INEXISTENTE_UPDATE = "Não é possível atualizar um professor inexistente";

        public static final String MSG_ERRO_EMAIL_JA_EM_USO_POR_OUTRO_PROFESSOR = "O email já está em uso por outro servidor";

        public static final String MSG_ERRO_PROFESSOR_EXISTENTE_CREATE = "Já existe um professor com esse e-mail cadastrado";

        public static final String MSG_ERRO_PROFESSOR_NAO_ENCONTRADO = "Professor não encontrado";
    }

    public static class PORTARIA_ERROS{
        public static final String PORTARIA_NAO_ENCONTRADA = "Portaria não encontrada";
        public static final String EXCLUIR_PORTARIA_COM_ATIVIDADE = "Não é possível excluir uma portaria que esteja associada a uma atividade";
        public static final String ALTERAR_PORTARIA_COM_ATIVIDADE = "Não é possível alterar o tipo de uma portaria que esteja associada a uma atividade";
    }

    public static class SERVIDOR_ERROS {
        public static final String MSG_ERRO_SERVIDOR_INEXISTENTE_UPDATE = "Não é possível atualizar um servidor inexistente";

        public static final String MSG_ERRO_EMAIL_JA_EM_USO_POR_OUTRO_SERVIDOR = "O email já está em uso por outro servidor";

        public static final String MSG_ERRO_SERVIDOR_EXISTENTE_CREATE = "Já existe um servidor com esse e-mail cadastrado";

        public static final String MSG_ERRO_SERVIDOR_NAO_ENCONTRADO = "Servidor não encontrado";
    }

    public static class CURSO_ERROS {
        public static final String MSG_ERRO_CURSO_NAO_ENCONTRADO = "Curso não encontrado";
        public static final String EXCLUIR_CURSO_COM_COMPONENTES = "Para excluir o curso, remova todos os componentes curriculares";
    }

    public static class COMPONENTE_CURRICULAR_ERROS {
        public static final String MSG_ERRO_COMPONENTE_CURRICULAR_NAO_ENCONTRADO = "Componente curricular não encontrado";
        public static final String EXCLUIR_COMPONENTE_DETALHADO = "Não é possível excluir esse componente curricular pois o mesmo já foi usado em um PIT";
    }


    public static class ROLE_ERROS {
        public static final String MSG_ROLE_INEXISTENTE = "A role solicitada não se encontra cadastrada no sistema";
    }


    public static class RECOVERY_PASS_ERROS {
        public static final String MSG_ERRO_USUARIO_NAO_ENCONTRADO = "Não há usuário com o email informado";

        public static final String MSG_ERRO_CODIGO_RECUPERACAO_INVALIDO = "Código de recuperação inválido";

        public static final String MSG_ERRO_CODIGO_RECUPERACAO_EXPIRADO = "Código de recuperação expirado";
    }

    public static class PIT_ERROS {
        public static final String PIT_PROFESSOR_PERIODO_JA_EXISTENTE = "Já exise um PIT em andamento para esse professor nesse período!";
        public static final String PIT_NAO_ENCONTRADO = "PIT não encontrado";
        public static final String PIT_APROVADO_NAO_ALTERA = "Não é possível alterar a resposta de um PIT já aprovado";
        public static final String PIT_EM_REVISAO_NAO_ALTERA = "Não é possível alterar a resposta de um PIT em revisão";

        public static final String TOTAL_HORAS_INVALIDO = "O total de horas deve ser igual a 40";

        public static final String PIT_EM_ANDAMENTO_NAO_ENCONTRADO = "Não foi localizado um PIT em andamento para o usuário";

        public static final String PIT_PERIODO_INVALIDO = "PIT com período inválido";
    }

    public static class ATIVIDADE_ERROS {
        public static final String ATIVIDADE_NAO_ENCONTRADA = "Atividade não encontrada";

        public static final String COMPONENTE_CURRICULAR_NAO_ENCONTRADO = "Componente curricular não encontrado";

        public static final String DETALHAMENTO_COMPONENTE_CURRICULAR_NAO_ENCONTRADO = "Detalhamento Componente curricular não encontrado";
        public static final String ATIVIDADE_SEM_CARGA_HORARIA = "Informe a carga horária mínima e máxima";

        public static final String ATIVIDADE_SEM_NOME = "Informe o nome da atividade";

        public static final String EXCLUIR_ATIVIDADE_COM_PORTARIRAS = "Não é possível excluir atividade com portarias associadas";
        public static final String EXCLUIR_ATIVIDADE_COM_PROJETOS = "Não é possível excluir atividade com projetos associados";
        public static final String EXCLUIR_ATIVIDADE_COM_RESPOSTA = "Não é possível excluir atividade que já foi respondida em um PIT";
    }


}
