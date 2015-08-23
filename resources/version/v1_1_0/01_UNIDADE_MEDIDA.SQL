CREATE TABLE IF NOT EXISTS milkmoney.unidadeMedida (
  id INT NOT NULL AUTO_INCREMENT,
  descricao VARCHAR(45) NULL,
  sigla VARCHAR(3) NULL,
  PRIMARY KEY (id))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;
