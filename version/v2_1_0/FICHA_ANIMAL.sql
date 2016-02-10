ALTER TABLE milkmoney.fichaAnimal CHANGE COLUMN numeroLactacoes numeroLactacoes INT(11) NULL DEFAULT 0;
ALTER TABLE milkmoney.fichaAnimal ADD COLUMN mesesProduzindo INT(11) NULL DEFAULT 0 AFTER eficienciaReprodutiva;
ALTER TABLE milkmoney.fichaAnimal ADD COLUMN mesesProducaoIdeal DECIMAL(19,2) NULL DEFAULT 0 AFTER mesesProduzindo;
ALTER TABLE milkmoney.fichaAnimal ADD COLUMN eficienciaTempoProducao DECIMAL(19,2) NULL DEFAULT 0 AFTER mesesProducaoIdeal;
