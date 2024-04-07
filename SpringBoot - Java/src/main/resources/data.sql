-- Inserções para a tabela endereco
INSERT INTO endereco (cep, logradouro, numero, complemento, cidade, estado)
VALUES
('12345-678', 'Rua A', 123, 'Apto 101', 'Cidade A', 'Estado A'),
('54321-876', 'Avenida B', 456, 'Sala 202', 'Cidade B', 'Estado B');

-- Inserções para a tabela especialidade
INSERT INTO especialidade (nome)
VALUES
('Corte Masculino'),
('Barba'),
('Coloração');

-- Inserções para a tabela avaliacao
INSERT INTO avaliacao (resultadoAvaliacao)
VALUES
(5.0),
(3.8),
(4.6);

-- Inserções para a tabela barbearia
INSERT INTO barbearia (nome_negocio, celular, email, img_perfil, barbearia_fk_endereco)
VALUES
('Barbearia Teste 1', '123456789', 'contato@teste1.com', NULL, 1),
('Barbearia Teste 2', '987654321', 'contato@teste2.com', NULL, 2);

-- Inserções para a tabela usuario
INSERT INTO usuario (nome, email, senha, celular, imgPerfil, user_admin, user_fk_barbearia)
VALUES
('Usuário Teste 1', 'usuario1@teste.com', 'senha123', '987654321', NULL, 0, 1),
('Usuário Teste 2', 'usuario2@teste.com', 'senha456', '123456789', NULL, 0, 2);

-- Inserções para a tabela financeiro
INSERT INTO financeiro (valor, dt_lancamento, financeiro_fk_barberia)
VALUES
(1000.50, NOW(), 1),
(1500.75, NOW(), 2);

-- Inserções para a tabela imgs_galeria
INSERT INTO imgs_galeria (imagem, barbearia_id_barbearia, usuario_id_usuario)
VALUES
('imagem1.jpg', 1, 1),
('imagem2.jpg', 1, 2),
('imagem3.jpg', 2, 1);

-- Inserções para a tabela servico
INSERT INTO servico (preco, descricao, tipo_servico, tempo_estimado, servico_fk_usuario, servico_fk_barbearia, servico_fk_especialidade)
VALUES
(50.00, 'Corte de cabelo masculino', 'Corte', 30, 1, 1, 1),
(40.00, 'Barba completa', 'Barba', 20, 1, 1, 2),
(80.00, 'Coloração de cabelo', 'Coloração', 60, 2, 2, 3);

-- Inserções para a tabela agendamento
INSERT INTO agendamento (data_hora, concluido, ag_fk_servico, ag_fk_cliente, ag_fk_barbeiro, ag_fk_barbearia, ag_fk_especialidade, ag_fk_avaliacao)
VALUES
('2024-04-05 10:00:00', TRUE, 1, 1, 2, 1, 1, 1),
('2024-04-07 15:30:00', FALSE, 2, 2, 1, 2, 2, 2);
