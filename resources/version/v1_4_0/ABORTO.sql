ALTER TABLE milkmoney.cobertura CHANGE COLUMN situacaoConfirmacaoPrenhes situacaoConfirmacaoPrenhes VARCHAR(45) NULL DEFAULT NULL;
ALTER TABLE milkmoney.cobertura ADD COLUMN aborto INT(11) NULL AFTER situacaoConfirmacaoPrenhes;
ALTER TABLE milkmoney.cobertura ADD INDEX fk_cobertura_aborto1_idx (aborto ASC);

CREATE TABLE IF NOT EXISTS milkmoney.aborto (
  id INT(11) NOT NULL AUTO_INCREMENT,
  data DATE NULL DEFAULT NULL,
  sexoCria VARCHAR(45) NOT NULL,
  observacao VARCHAR(100) NULL DEFAULT NULL,
  PRIMARY KEY (id))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;

ALTER TABLE milkmoney.cobertura ADD CONSTRAINT fk_cobertura_aborto1
  FOREIGN KEY (aborto)
  REFERENCES milkmoney.aborto (id)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

