update indicador set formato = 'DECIMAL DUAS_CASAS' where formato = 'DECIMAL_FORMAT_DUAS_CASAS';
update indicador set formato = 'DECIMAL UMA CASA' where formato = 'DECIMAL_FORMAT_UMA_CASA';
update indicador set formato = 'NÚMERO INTEIRO' where formato = 'INTEIRO_FORMAT';

update indicador set descricao = 'NOVILHAS' where descricao = 'NOILHAS';
update indicador set descricao = 'TAXA DE PRENHEZ (%)' where descricao = 'TAXA DE PRENHES (%)';	


CREATE TABLE IF NOT EXISTS milkmoney.valorIndicador (
  id INT(11) NOT NULL AUTO_INCREMENT,
  ano INT(11) NOT NULL DEFAULT 0,
  mes INT(11) NOT NULL DEFAULT 0,
  valor DECIMAL(19,2) NOT NULL DEFAULT 0,
  indicador INT(11) NOT NULL,
  PRIMARY KEY (id),
  INDEX fk_valorIndicador_indicador1_idx (indicador ASC),
  CONSTRAINT fk_valorIndicador_indicador1
    FOREIGN KEY (indicador)
    REFERENCES milkmoney.indicador (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci

CREATE TABLE IF NOT EXISTS milkmoney.configuracaoIndicador (
  id INT(11) NOT NULL AUTO_INCREMENT,
  ano INT(11) NOT NULL DEFAULT 0,
  menorValorIdeal DECIMAL(19,2) NOT NULL DEFAULT 0,
  maiorValorIdeal DECIMAL(19,2) NOT NULL DEFAULT 0,
  objetivo varchar(45) NOT NULL, 
  indicador INT(11) NOT NULL,
  PRIMARY KEY (id),
  INDEX fk_valorIndicador_indicador1_idx (indicador ASC),
  CONSTRAINT fk_configuracaoIndicador_indicador1
    FOREIGN KEY (indicador)
    REFERENCES milkmoney.indicador (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;

alter table indicador drop column valorApurado;
alter table indicador drop column menorValorIdeal;
alter table indicador drop column maiorValorIdeal;
alter table indicador drop column objetivo;
alter table indicador drop column ano;

