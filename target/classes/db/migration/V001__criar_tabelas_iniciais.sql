CREATE TABLE `declaracao` (
  `idDec` bigint(20) NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  `tipoPeriodo` varchar(20) NOT NULL,
  PRIMARY KEY (`idDec`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `empresa` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `codEmp` varchar(10) NOT NULL,
  `codEmpFilial` varchar(2) NOT NULL,
  `cnpj` varchar(14) NOT NULL,
  `nome` varchar(100) NOT NULL,
  `ativa` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `grupo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(20) NOT NULL,
  `descricao` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `grupoempresa` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idgrup` int(11) NOT NULL,
  `idemp` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `grupoempresa_ibfk_1` (`idgrup`),
  KEY `grupoempresa_ibfk_2` (`idemp`),
  CONSTRAINT `grupoempresa_ibfk_1` FOREIGN KEY (`idgrup`) REFERENCES `grupo` (`id`),
  CONSTRAINT `grupoempresa_ibfk_2` FOREIGN KEY (`idemp`) REFERENCES `empresa` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(20) NOT NULL,
  `descricao` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




CREATE TABLE `usuario` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login` varchar(30) NOT NULL,
  `nome` varchar(25) NOT NULL,
  `senha` varchar(255) NOT NULL,
  `ativo` tinyint(4) NOT NULL,
  `email` varchar(60) DEFAULT NULL,
  `sobrenome` varchar(50) DEFAULT NULL,
  `token_reset_senha` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `usuarios_roles` (
  `usuario_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`usuario_id`,`role_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `usuarios_roles_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `usuarios_roles_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `solicitacao` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `competencia` varchar(11) NOT NULL,
  `tipoDec` varchar(11) NOT NULL,
  `observacao` varchar(50) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `id_declaracao` bigint(20) NOT NULL,
  `id_empresa` bigint(20) NOT NULL,
  `dataSolicitacao` datetime DEFAULT NULL,
  `id_usuario_solicita` int(11) DEFAULT NULL,
  `dataRetorno` datetime DEFAULT NULL,
  `id_usuario_retorno` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_declaracao` (`id_declaracao`),
  KEY `id_empresa` (`id_empresa`),
  KEY `id_usuario_solicita` (`id_usuario_solicita`),
  KEY `id_usuario_retorno` (`id_usuario_retorno`),
  CONSTRAINT `solicitacao_ibfk_4` FOREIGN KEY (`id_usuario_retorno`) REFERENCES `usuario` (`id`),
  CONSTRAINT `solicitacao_ibfk_1` FOREIGN KEY (`id_declaracao`) REFERENCES `declaracao` (`idDec`),
  CONSTRAINT `solicitacao_ibfk_2` FOREIGN KEY (`id_empresa`) REFERENCES `empresa` (`id`),
  CONSTRAINT `solicitacao_ibfk_3` FOREIGN KEY (`id_usuario_solicita`) REFERENCES `usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;