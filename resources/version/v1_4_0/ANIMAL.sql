ALTER TABLE milkmoney.animal DROP FOREIGN KEY fk_animal_animal1;
ALTER TABLE milkmoney.animal DROP FOREIGN KEY fk_animal_animal2;

ALTER TABLE milkmoney.animal 
ADD CONSTRAINT fk_animal_animal1
  FOREIGN KEY (mae)
  REFERENCES milkmoney.animal (id)
  ON DELETE SET NULL
  ON UPDATE NO ACTION;

ALTER TABLE milkmoney.animal
ADD CONSTRAINT fk_animal_animal2
  FOREIGN KEY (paiMontaNatural)
  REFERENCES milkmoney.animal (id)
  ON DELETE SET NULL
  ON UPDATE NO ACTION;
