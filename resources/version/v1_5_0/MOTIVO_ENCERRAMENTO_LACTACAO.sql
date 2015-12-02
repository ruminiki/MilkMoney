CREATE TABLE IF NOT EXISTS milkmoney.motivoEncerramentoLactacao (
  id INT(11) NOT NULL AUTO_INCREMENT,
  descricao VARCHAR(100) NULL DEFAULT NULL,
  PRIMARY KEY (id))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;

ALTER TABLE milkmoney.lactacao ADD COLUMN idMotivoEncerramentoLactacao INT(11) NULL DEFAULT NULL AFTER parto;

INSERT INTO MOTIVOENCERRAMENTOLACTACAO (ID, DESCRICAO) VALUES (1, 'PREPARAÇÃO PARA PARTO');
INSERT INTO MOTIVOENCERRAMENTOLACTACAO (ID, DESCRICAO) VALUES (2, 'DOENÇA');
INSERT INTO MOTIVOENCERRAMENTOLACTACAO (ID, DESCRICAO) VALUES (3, 'ACIDENTE');
INSERT INTO MOTIVOENCERRAMENTOLACTACAO (ID, DESCRICAO) VALUES (4, 'ENGORDA');
INSERT INTO MOTIVOENCERRAMENTOLACTACAO (ID, DESCRICAO) VALUES (5, 'BAIXA PRODUÇÃO');

UPDATE LACTACAO SET idMotivoEncerramentoLactacao = 1 where motivoEncerramentoLactacao = 'PREPARAÇÃO PARA PARTO' and dataFim is not null;
UPDATE LACTACAO SET idMotivoEncerramentoLactacao = 2 where motivoEncerramentoLactacao = 'DOENÇA' and dataFim is not null;
UPDATE LACTACAO SET idMotivoEncerramentoLactacao = 3 where motivoEncerramentoLactacao = 'ACIDENTE' and dataFim is not null;
UPDATE LACTACAO SET idMotivoEncerramentoLactacao = 4 where motivoEncerramentoLactacao = 'ENGORDA' and dataFim is not null;

ALTER TABLE milkmoney.lactacao DROP COLUMN motivoEncerramentoLactacao;
ALTER TABLE milkmoney.lactacao CHANGE idMotivoEncerramentoLactacao motivoEncerramentoLactacao INT NULL DEFAULT NULL AFTER parto;
ALTER TABLE milkmoney.lactacao ADD INDEX fk_lactacao_motivoEncerramentoLactacao1_idx (motivoEncerramentoLactacao ASC);

ALTER TABLE milkmoney.lactacao 
ADD CONSTRAINT fk_lactacao_motivoEncerramentoLactacao1
  FOREIGN KEY (motivoEncerramentoLactacao)
  REFERENCES milkmoney.motivoEncerramentoLactacao (id)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;