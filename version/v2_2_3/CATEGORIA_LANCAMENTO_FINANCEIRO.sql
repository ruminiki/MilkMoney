﻿ALTER TABLE milkmoney.categoriaLancamentoFinanceiro DROP FOREIGN KEY fk_categoriaDespesa_categoriaDespesa1;

ALTER TABLE milkmoney.categoriaLancamentoFinanceiro DROP COLUMN categoriaSuperiora, DROP INDEX fk_categoriaDespesa_categoriaDespesa1_idx;
