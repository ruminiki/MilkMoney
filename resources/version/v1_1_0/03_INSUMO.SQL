ALTER TABLE milkmoney.insumo DROP COLUMN dataVencimento;

ALTER TABLE milkmoney.insumo 
ADD COLUMN unidadeMedida INT(11) NULL DEFAULT NULL COMMENT 'KG, L, TN' AFTER quantidade,
ADD COLUMN tipoInsumo INT(11) NOT NULL AFTER despesa,
ADD INDEX fk_insumo_tipoInsumo1_idx (tipoInsumo ASC),
ADD INDEX fk_insumo_unidadeMedida1_idx (unidadeMedida ASC);

ALTER TABLE milkmoney.insumo 
ADD CONSTRAINT fk_insumo_tipoInsumo1
  FOREIGN KEY (tipoInsumo)
  REFERENCES milkmoney.tipoInsumo (id)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT fk_insumo_unidadeMedida1
  FOREIGN KEY (unidadeMedida)
  REFERENCES milkmoney.unidadeMedida (id)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
