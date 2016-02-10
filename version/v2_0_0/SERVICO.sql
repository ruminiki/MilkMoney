ALTER TABLE milkmoney.servico ADD COLUMN lancamentoFinanceiro INT(11) NULL DEFAULT NULL AFTER historico;
ALTER TABLE milkmoney.servico ADD INDEX fk_servico_lancamentoFinanceiro1_idx (lancamentoFinanceiro ASC);
ALTER TABLE milkmoney.servico ADD CONSTRAINT fk_servico_lancamentoFinanceiro1
  FOREIGN KEY (lancamentoFinanceiro)
  REFERENCES milkmoney.lancamentoFinanceiro (id)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE milkmoney.lancamentoFinanceiro CHANGE COLUMN parcela parcela VARCHAR(45) NULL DEFAULT NULL COMMENT 'Hash que liga todas as parcelas de um movimento';
ALTER TABLE milkmoney.lancamentoFinanceiro ADD COLUMN servico INT(11) NULL DEFAULT NULL AFTER parcela;
ALTER TABLE milkmoney.lancamentoFinanceiro ADD INDEX fk_lancamentoFinanceiro_servico1_idx (servico ASC);
ALTER TABLE milkmoney.lancamentoFinanceiro 
ADD CONSTRAINT fk_lancamentoFinanceiro_servico1
  FOREIGN KEY (servico)
  REFERENCES milkmoney.servico (id)
  ON DELETE SET NULL
  ON UPDATE NO ACTION;
