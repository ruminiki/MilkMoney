ALTER TABLE milkmoney.cobertura CHANGE COLUMN dataConfirmacaoPrenhes dataConfirmacaoPrenhez DATE NULL DEFAULT NULL COMMENT 'data confirmação da prenhez';
ALTER TABLE milkmoney.cobertura CHANGE COLUMN metodoConfirmacaoPrenhes metodoConfirmacaoPrenhez VARCHAR(45) NULL DEFAULT NULL COMMENT 'método confirmação da prenhez';
ALTER TABLE milkmoney.cobertura CHANGE COLUMN observacaoConfirmacaoPrenhes observacaoConfirmacaoPrenhez VARCHAR(100) NULL DEFAULT NULL COMMENT 'observação confirmação da prenhez';
ALTER TABLE milkmoney.cobertura CHANGE COLUMN situacaoConfirmacaoPrenhes situacaoConfirmacaoPrenhez VARCHAR(45) NULL DEFAULT NULL;
