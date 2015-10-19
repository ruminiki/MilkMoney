drop table if exists indicador;

CREATE TABLE indicador (
  id int(11) NOT NULL AUTO_INCREMENT,
  descricao varchar(100) DEFAULT NULL,
  menorValorIdeal decimal(19,2) DEFAULT NULL,
  maiorValorIdeal decimal(19,2) DEFAULT NULL,
  valorApurado decimal(19,2) DEFAULT NULL,
  classeCalculo varchar(100) DEFAULT NULL,
  ordem int(11) DEFAULT NULL,
  sigla varchar(45) DEFAULT NULL,
  definicao text,
  objetivo varchar(45) DEFAULT NULL COMMENT 'Indica se o objetivo é superar o valor de referência ou ficar abaixo.',
  tipo varchar(3) DEFAULT NULL,
  formato varchar(30) DEFAULT NULL,
  sufixo varchar(1) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY descricao_UNIQUE (descricao),
  UNIQUE KEY sigla_UNIQUE (sigla)
) ENGINE=InnoDB AUTO_INCREMENT=186 DEFAULT CHARSET=latin1;
SELECT * FROM milkmoney.indicador;




