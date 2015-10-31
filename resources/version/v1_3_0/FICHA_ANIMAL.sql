ALTER TABLE milkmoney.fichaAnimal 
ADD COLUMN numeroLactacoes INT(11) NULL DEFAULT 0 AFTER situacaoUltimaCobertura,
ADD COLUMN mediaProducao DECIMAL(19,2) NULL DEFAULT NULL AFTER numeroLactacoes,
ADD COLUMN ultimoTratamento VARCHAR(100) NULL DEFAULT NULL AFTER mediaProducao,
ADD COLUMN lote VARCHAR(100) NULL DEFAULT NULL AFTER ultimoTratamento;

