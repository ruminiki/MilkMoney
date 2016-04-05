ALTER TABLE animal ADD COLUMN observacao VARCHAR(400) NULL DEFAULT NULL AFTER paiEnseminacaoArtificial;

ALTER TABLE animal ADD COLUMN lote INT(11) NULL DEFAULT NULL AFTER imagem;
ALTER TABLE animal ADD INDEX fk_animal_lote1_idx (lote ASC);
ALTER TABLE milkmoney.animal 
ADD CONSTRAINT fk_animal_lote1
  FOREIGN KEY (lote)
  REFERENCES milkmoney.lote (id)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

UPDATE animal a SET lote = (SELECT la.lote FROM loteAnimal la WHERE la.animal = a.id LIMIT 1);

DROP TABLE IF EXISTS milkmoney.loteanimal;
