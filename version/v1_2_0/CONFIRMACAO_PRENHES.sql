ALTER TABLE milkmoney.cobertura ADD COLUMN dataConfirmacaoPrenhes DATE NULL DEFAULT NULL COMMENT 'data confirmação da prenhes' AFTER touroInseminacaoArtificial;
ALTER TABLE milkmoney.cobertura ADD COLUMN metodoConfirmacaoPrenhes VARCHAR(45) NULL DEFAULT NULL COMMENT 'método confirmação da prenhes' AFTER dataConfirmacaoPrenhes;
ALTER TABLE milkmoney.cobertura ADD COLUMN observacaoConfirmacaoPrenhes VARCHAR(100) NULL DEFAULT NULL COMMENT 'observação confirmação da prenhes' AFTER metodoConfirmacaoPrenhes;
ALTER TABLE milkmoney.cobertura ADD COLUMN situacaoConfirmacaoPrenhes VARCHAR(45) NULL DEFAULT NULL COMMENT 'situação da cobertura no momento da confirmação' AFTER observacaoConfirmacaoPrenhes;

DROP TABLE IF EXISTS milkmoney.confirmacaoprenhes ;
update cobertura set situacaoCobertura = 'NÃO CONFIRMADA';