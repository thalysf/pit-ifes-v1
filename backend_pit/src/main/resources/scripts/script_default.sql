-- inserts-atividade
INSERT INTO atividade (id_atividade, nome_atividade, tipo_atividade, carga_horaria_minima, carga_horaria_maxima, tipo_detalhamento) values
('8a42d95b-21f5-4299-8d39-5d172b655ddb', 'Aula', 'ATIVIDADE_ENSINO_DEFAULT', null, null, 'DETALHAMENTO_AULA'),
('ae280b35-8eaf-488f-9570-eb0805debdec', 'Mediação Pedagógica', 'ATIVIDADE_ENSINO_DEFAULT', null, null, 'DETALHAMENTO_AULA');

-- inserts-curso
INSERT INTO curso (id_curso, nome) values
('718de0bb-26f6-4fde-adf0-098d4dc37719', 'Sistemas de Informação'),
('b04a680a-fde2-46bf-8f34-6740e5be071e', 'Arquitetura e Urbanismo');