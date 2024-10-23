-- db_naregua.avaliacao definição

CREATE TABLE `avaliacao` (
  `id_avaliacao` int NOT NULL AUTO_INCREMENT,
  `resultado_avaliacao` decimal(3,2) DEFAULT NULL,
  `comentario` text,
  PRIMARY KEY (`id_avaliacao`),
  CONSTRAINT `avaliacao_chk_1` CHECK (((`resultado_avaliacao` <= 5) and (`resultado_avaliacao` >= 0)))
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- db_naregua.endereco definição

CREATE TABLE `endereco` (
  `id_endereco` int NOT NULL AUTO_INCREMENT,
  `cep` varchar(9) DEFAULT NULL,
  `logradouro` varchar(250) DEFAULT NULL,
  `numero` int DEFAULT NULL,
  `complemento` varchar(50) DEFAULT NULL,
  `cidade` varchar(60) DEFAULT NULL,
  `estado` varchar(60) DEFAULT NULL,
  `localizacao` point NOT NULL /*!80003 SRID 4326 */,
  PRIMARY KEY (`id_endereco`),
  SPATIAL KEY `idx_localizacao` (`localizacao`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- db_naregua.especialidade definição

CREATE TABLE `especialidade` (
  `id_especialidade` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(180) DEFAULT NULL,
  PRIMARY KEY (`id_especialidade`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- db_naregua.tipo_notificacao definição

CREATE TABLE `tipo_notificacao` (
  `id_tipo_notificacao` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  `data_criacao` datetime NOT NULL,
  PRIMARY KEY (`id_tipo_notificacao`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- db_naregua.barbearia definição

CREATE TABLE `barbearia` (
  `id_barbearia` int NOT NULL AUTO_INCREMENT,
  `nome_negocio` varchar(100) DEFAULT NULL,
  `email_negocio` varchar(100) DEFAULT NULL,
  `celular_negocio` varchar(15) DEFAULT NULL,
  `cnpj` varchar(18) DEFAULT NULL,
  `cpf` varchar(14) DEFAULT NULL,
  `descricao` text,
  `barbearia_fk_endereco` int NOT NULL,
  `img_perfil` text,
  `img_banner` text,
  PRIMARY KEY (`id_barbearia`),
  KEY `barbearia_fk_endereco` (`barbearia_fk_endereco`),
  CONSTRAINT `barbearia_ibfk_1` FOREIGN KEY (`barbearia_fk_endereco`) REFERENCES `endereco` (`id_endereco`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- db_naregua.dia_semana definição

CREATE TABLE `dia_semana` (
  `id_dia_semana` int NOT NULL AUTO_INCREMENT,
  `nome` enum('SEG','TER','QUA','QUI','SEX','SAB','DOM') DEFAULT NULL,
  `hora_abertura` time DEFAULT NULL,
  `hora_fechamento` time DEFAULT NULL,
  `ds_id_barbearia` int NOT NULL,
  PRIMARY KEY (`id_dia_semana`),
  KEY `ds_id_barbearia` (`ds_id_barbearia`),
  CONSTRAINT `dia_semana_ibfk_1` FOREIGN KEY (`ds_id_barbearia`) REFERENCES `barbearia` (`id_barbearia`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- db_naregua.financeiro definição

CREATE TABLE `financeiro` (
  `id_financeiro` int NOT NULL AUTO_INCREMENT,
  `dt_lancamento` datetime(6) DEFAULT NULL,
  `valor` float DEFAULT NULL,
  `saldo` decimal(10,2) DEFAULT NULL,
  `despesas` bit(1) DEFAULT NULL,
  `financeiro_fk_barbearia` int DEFAULT NULL,
  PRIMARY KEY (`id_financeiro`),
  KEY `financeiro_fk_barbearia` (`financeiro_fk_barbearia`),
  CONSTRAINT `financeiro_ibfk_1` FOREIGN KEY (`financeiro_fk_barbearia`) REFERENCES `barbearia` (`id_barbearia`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- db_naregua.servico definição

CREATE TABLE `servico` (
  `id_servico` int NOT NULL AUTO_INCREMENT,
  `preco` float DEFAULT NULL,
  `descricao` text,
  `tipo_servico` varchar(180) DEFAULT NULL,
  `tempo_estimado` int NOT NULL,
  `servico_fk_barbearia` int NOT NULL,
  `nome_barbeiro` varchar(255) DEFAULT NULL,
  `status` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id_servico`),
  KEY `servico_fk_barbearia` (`servico_fk_barbearia`),
  CONSTRAINT `servico_ibfk_1` FOREIGN KEY (`servico_fk_barbearia`) REFERENCES `barbearia` (`id_barbearia`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- db_naregua.usuario definição

CREATE TABLE `usuario` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `dtype` varchar(50) NOT NULL,
  `nome` varchar(120) NOT NULL,
  `email` varchar(250) NOT NULL,
  `senha` varchar(80) NOT NULL,
  `celular` varchar(15) NOT NULL,
  `img_perfil` varchar(255) DEFAULT NULL,
  `usuario_admin` bit(1) DEFAULT NULL,
  `usuario_fk_barbearia` int DEFAULT NULL,
  `usuario_fk_endereco` int DEFAULT NULL,
  `barbeiro_fk_especialidade` int DEFAULT NULL,
  `usuario_fk_especialidade` int DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `UK_usuario_especialidade` (`usuario_fk_especialidade`),
  KEY `usuario_fk_barbearia` (`usuario_fk_barbearia`),
  KEY `usuario_fk_endereco` (`usuario_fk_endereco`),
  KEY `barbeiro_fk_especialidade` (`barbeiro_fk_especialidade`),
  CONSTRAINT `usuario_ibfk_1` FOREIGN KEY (`usuario_fk_especialidade`) REFERENCES `especialidade` (`id_especialidade`),
  CONSTRAINT `usuario_ibfk_2` FOREIGN KEY (`usuario_fk_barbearia`) REFERENCES `barbearia` (`id_barbearia`),
  CONSTRAINT `usuario_ibfk_3` FOREIGN KEY (`usuario_fk_endereco`) REFERENCES `endereco` (`id_endereco`),
  CONSTRAINT `usuario_ibfk_4` FOREIGN KEY (`barbeiro_fk_especialidade`) REFERENCES `especialidade` (`id_especialidade`),
  CONSTRAINT `usuario_chk_1` CHECK ((`dtype` in (_utf8mb4'Cliente',_utf8mb4'Barbeiro')))
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- db_naregua.usuario_servico definição

CREATE TABLE `usuario_servico` (
  `servico_id_servico` int NOT NULL,
  `servico_fk_barbearia` int DEFAULT NULL,
  `usuario_id_usuario` int NOT NULL,
  PRIMARY KEY (`servico_id_servico`,`usuario_id_usuario`),
  KEY `servico_fk_barbearia` (`servico_fk_barbearia`),
  KEY `usuario_id_usuario` (`usuario_id_usuario`),
  CONSTRAINT `usuario_servico_ibfk_1` FOREIGN KEY (`servico_fk_barbearia`) REFERENCES `barbearia` (`id_barbearia`),
  CONSTRAINT `usuario_servico_ibfk_2` FOREIGN KEY (`servico_id_servico`) REFERENCES `servico` (`id_servico`),
  CONSTRAINT `usuario_servico_ibfk_3` FOREIGN KEY (`usuario_id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- db_naregua.agendamento definição

CREATE TABLE `agendamento` (
  `id_agendamento` int NOT NULL AUTO_INCREMENT,
  `data_hora` datetime NOT NULL,
  `status` varchar(50) DEFAULT NULL,
  `ag_fk_avaliacao` int DEFAULT NULL,
  `barbearia_id_barbearia` int NOT NULL,
  `barbeiro_id_usuario` int NOT NULL,
  `cliente_id_usuario` int NOT NULL,
  `servico_id_servico` int NOT NULL,
  `especialidade_id_especialidade` int DEFAULT NULL,
  `data_hora_concluido` datetime DEFAULT NULL,
  `data_hora_fim_prevista` datetime DEFAULT NULL,
  `avaliado` bit(1) DEFAULT NULL,
  `id_avaliacao` int DEFAULT NULL,
  `concluido` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id_agendamento`),
  KEY `servico_id_servico` (`servico_id_servico`),
  KEY `barbeiro_id_usuario` (`barbeiro_id_usuario`),
  KEY `id_avaliacao` (`id_avaliacao`),
  KEY `cliente_id_usuario` (`cliente_id_usuario`),
  KEY `ag_fk_avaliacao` (`ag_fk_avaliacao`),
  KEY `especialidade_id_especialidade` (`especialidade_id_especialidade`),
  KEY `barbearia_id_barbearia` (`barbearia_id_barbearia`),
  CONSTRAINT `agendamento_ibfk_1` FOREIGN KEY (`servico_id_servico`) REFERENCES `servico` (`id_servico`),
  CONSTRAINT `agendamento_ibfk_2` FOREIGN KEY (`barbeiro_id_usuario`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `agendamento_ibfk_3` FOREIGN KEY (`id_avaliacao`) REFERENCES `avaliacao` (`id_avaliacao`),
  CONSTRAINT `agendamento_ibfk_4` FOREIGN KEY (`cliente_id_usuario`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `agendamento_ibfk_5` FOREIGN KEY (`ag_fk_avaliacao`) REFERENCES `avaliacao` (`id_avaliacao`),
  CONSTRAINT `agendamento_ibfk_6` FOREIGN KEY (`especialidade_id_especialidade`) REFERENCES `especialidade` (`id_especialidade`),
  CONSTRAINT `agendamento_ibfk_7` FOREIGN KEY (`barbearia_id_barbearia`) REFERENCES `barbearia` (`id_barbearia`),
  CONSTRAINT `agendamento_chk_1` CHECK ((`status` in (_utf8mb4'Agendado',_utf8mb4'Pendente',_utf8mb4'Concluido',_utf8mb4'Cancelado')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- db_naregua.chat definição

CREATE TABLE `chat` (
  `id_chat` int NOT NULL AUTO_INCREMENT,
  `data_criacao` datetime NOT NULL,
  `barbearia_id_barbearia` int DEFAULT NULL,
  `usuario_id_usuario` int DEFAULT NULL,
  PRIMARY KEY (`id_chat`),
  KEY `FK2ahurspt0jxplu4jlj9e5d4si` (`barbearia_id_barbearia`),
  KEY `FKdi1ir9kfe88qhkcvvl5bydk3c` (`usuario_id_usuario`),
  CONSTRAINT `FK2ahurspt0jxplu4jlj9e5d4si` FOREIGN KEY (`barbearia_id_barbearia`) REFERENCES `barbearia` (`id_barbearia`),
  CONSTRAINT `FKdi1ir9kfe88qhkcvvl5bydk3c` FOREIGN KEY (`usuario_id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- db_naregua.imgs_galeria definição

CREATE TABLE `imgs_galeria` (
  `id_imgs_galeria` int NOT NULL AUTO_INCREMENT,
  `imagem` text NOT NULL,
  `descricao` varchar(255) DEFAULT NULL,
  `usuario_id_usuario` int DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id_imgs_galeria`),
  KEY `usuario_id_usuario` (`usuario_id_usuario`),
  CONSTRAINT `imgs_galeria_ibfk_1` FOREIGN KEY (`usuario_id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- db_naregua.mensagem definição

CREATE TABLE `mensagem` (
  `id_mensagem` int NOT NULL AUTO_INCREMENT,
  `conteudo` text,
  `data_criacao` datetime NOT NULL,
  `status_mensagem` varchar(45) DEFAULT NULL,
  `mensagem_id_usuario` int NOT NULL,
  `mensagem_id_chat` int NOT NULL,
  `filename` text,
  `tipo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_mensagem`,`mensagem_id_usuario`,`mensagem_id_chat`),
  KEY `fk_mensagem_usuario_idx` (`mensagem_id_usuario`),
  KEY `fk_mensagem_chat_history1_idx` (`mensagem_id_chat`),
  CONSTRAINT `FK7tu1hlxoywt5f7m6adct06cwt` FOREIGN KEY (`mensagem_id_chat`) REFERENCES `chat` (`id_chat`),
  CONSTRAINT `fk_mensagem_usuario` FOREIGN KEY (`mensagem_id_usuario`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `ck_status` CHECK ((`status_mensagem` in (_utf8mb4'Enviada',_utf8mb4'Entregue',_utf8mb4'Lida')))
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- db_naregua.notificacao definição

CREATE TABLE `notificacao` (
  `id_notificacao` int NOT NULL AUTO_INCREMENT,
  `notificacao_id_usuario` int NOT NULL,
  `notificacao_id_tipo` int NOT NULL,
  `referencia_id` int DEFAULT NULL,
  `mensagem` text,
  `data_criacao` datetime DEFAULT NULL,
  `lida` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id_notificacao`),
  KEY `notificacao_id_usuario` (`notificacao_id_usuario`),
  KEY `notificacao_id_tipo` (`notificacao_id_tipo`),
  CONSTRAINT `notificacao_ibfk_1` FOREIGN KEY (`notificacao_id_usuario`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `notificacao_ibfk_2` FOREIGN KEY (`notificacao_id_tipo`) REFERENCES `tipo_notificacao` (`id_tipo_notificacao`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- db_naregua.postagem definição

CREATE TABLE `postagem` (
  `id_postagem` int NOT NULL AUTO_INCREMENT,
  `conteudo` text NOT NULL,
  `data_criacao` datetime NOT NULL,
  `postagem_id_usuario` int NOT NULL,
  PRIMARY KEY (`id_postagem`,`postagem_id_usuario`),
  KEY `fk_postagem_usuario` (`postagem_id_usuario`),
  CONSTRAINT `fk_postagem_usuario` FOREIGN KEY (`postagem_id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- db_naregua.comentario definição

CREATE TABLE `comentario` (
  `id_comentario` int NOT NULL AUTO_INCREMENT,
  `conteudo` text NOT NULL,
  `data_criacao` datetime NOT NULL,
  `comentario_id_postagem` int NOT NULL,
  `comentario_id_usuario` int NOT NULL,
  PRIMARY KEY (`id_comentario`,`comentario_id_postagem`,`comentario_id_usuario`),
  KEY `fk_comentario_postagem` (`comentario_id_postagem`),
  KEY `fk_comentario_usuario` (`comentario_id_usuario`),
  CONSTRAINT `fk_comentario_postagem` FOREIGN KEY (`comentario_id_postagem`) REFERENCES `postagem` (`id_postagem`),
  CONSTRAINT `fk_comentario_usuario` FOREIGN KEY (`comentario_id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- db_naregua.curtida definição

CREATE TABLE `curtida` (
  `id_curtida` int NOT NULL,
  `data_criacao` datetime NOT NULL,
  `curtida_id_usuario` int NOT NULL,
  `curtida_id_comentario` int DEFAULT NULL,
  `curtida_id_postagem` int DEFAULT NULL,
  PRIMARY KEY (`id_curtida`,`curtida_id_usuario`),
  KEY `fk_curtida_postagem` (`curtida_id_postagem`),
  KEY `fk_curtida_comentario` (`curtida_id_comentario`),
  KEY `fk_curtida_usuario` (`curtida_id_usuario`),
  CONSTRAINT `fk_curtida_comentario` FOREIGN KEY (`curtida_id_comentario`) REFERENCES `comentario` (`id_comentario`),
  CONSTRAINT `fk_curtida_postagem` FOREIGN KEY (`curtida_id_postagem`) REFERENCES `postagem` (`id_postagem`),
  CONSTRAINT `fk_curtida_usuario` FOREIGN KEY (`curtida_id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- db_naregua.midia definição

CREATE TABLE `midia` (
  `id_midia` int NOT NULL,
  `arquivo` text NOT NULL,
  `data_criacao` datetime NOT NULL,
  `midia_id_postagem` int DEFAULT NULL,
  `midia_id_comentario` int DEFAULT NULL,
  PRIMARY KEY (`id_midia`),
  KEY `fk_midia_postagem` (`midia_id_postagem`),
  KEY `fk_midia_comentario` (`midia_id_comentario`),
  CONSTRAINT `fk_midia_comentario` FOREIGN KEY (`midia_id_comentario`) REFERENCES `comentario` (`id_comentario`),
  CONSTRAINT `fk_midia_postagem` FOREIGN KEY (`midia_id_postagem`) REFERENCES `postagem` (`id_postagem`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;