-- inserts-role-table
INSERT INTO curso (id_curso, nome) values
('718de0bb-26f6-4fde-adf0-098d4dc37719', 'Sistemas de Informação'),
('b04a680a-fde2-46bf-8f34-6740e5be071e', 'Arquitetura e Urbanismo');


insert into componente_curricular (id_componente_curricular, carga_horaria, nome, curso_id_curso) values
('a6cf59e8-ace1-4071-a723-632c9c812dd6', 4, 'Lógica', '718de0bb-26f6-4fde-adf0-098d4dc37719'),
('89f75814-cfd0-43a8-b087-f67991f0487d', 4, 'Organização e Arquitetura de Computadores', '718de0bb-26f6-4fde-adf0-098d4dc37719'),
('84b21de1-3d0f-463d-83e4-b42ecf8d6109', 4, 'Programação I', '718de0bb-26f6-4fde-adf0-098d4dc37719'),
('3b72af80-301f-44f6-8bf2-31ee40d5065c', 4, 'Programação II', '718de0bb-26f6-4fde-adf0-098d4dc37719'),
('13851b46-d663-4410-8163-46dba861e7b2', 6, 'Sociologia', '718de0bb-26f6-4fde-adf0-098d4dc37719'),
('6e0e2f02-7b91-404b-b5f9-a882f2765825', 6, 'Metodologia de Pesquisa', '718de0bb-26f6-4fde-adf0-098d4dc37719'),
-- arquitetura
('8053d732-9d8b-4e37-89c8-d5e0bd319f0a', 2, 'Arquitetura de Interiores e Ergonomia I', 'b04a680a-fde2-46bf-8f34-6740e5be071e'),
('e876d580-92e5-4cbb-bcbf-82ca26fa721d', 2, 'Desenho Arquitetônico I', 'b04a680a-fde2-46bf-8f34-6740e5be071e'),
('bf2d2df6-01f4-41e8-b011-5d92185616b6', 4, 'Física', 'b04a680a-fde2-46bf-8f34-6740e5be071e'),
('2b8a2a82-bfc7-4350-95c4-c9071e1637b2', 4, 'História e Teoria da Arquitetura Brasileira I', 'b04a680a-fde2-46bf-8f34-6740e5be071e'),
('cb0e6016-7f38-490f-b5d5-19659ae74e84', 4, 'Instalações Prediais – Elétrica e Complementares I', 'b04a680a-fde2-46bf-8f34-6740e5be071e'),
('fe070570-a418-4e07-99a3-50ea321e1ad4', 4, 'Topografia Aplicada à Arquitetura', 'b04a680a-fde2-46bf-8f34-6740e5be071e');


