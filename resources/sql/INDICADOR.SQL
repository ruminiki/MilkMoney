INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo, definicao)
SELECT * FROM ( SELECT 'DIAS EM LACTAÇÃO', 'DEL', 1, '200', 'br.com.milksys.service.indicadores.DiasEmLactacao',
'Dias em lactação é um indicador que apresenta a média dos dias de lactação do rebanho. O cálculo consiste na soma dos dias de lactação após cada parto, dividido pelo número de lactações encontrado. Os valores de referência são...') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'DEL');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo) 
SELECT * FROM (SELECT 'DIAS EM ABERTO', 'DEA', 2, '200', 'br.com.milksys.service.indicadores.DiasEmAberto') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'DEA');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo, definicao) 
SELECT * FROM (SELECT 'INTERVALO ENTRE PARTOS', 'IEP', 3, '200', 'br.com.milksys.service.indicadores.IntervaloEntrePartos',
'Este dado reflete o insucesso do manejo reprodutivo anteriormente aos partos, mas não aponta as causas das falhas. O intervalo entre partos atual é o cálculo do número de meses entre o parto mais recente e o parto anterior das vacas com mais de um parto. Neste dado não entram as vacas de primeira lactação.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'IEP');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo) 
SELECT * FROM (SELECT 'TAMANHO DO REBANHO', 'TR', 4, '0', 'br.com.milksys.service.indicadores.TamanhoRebanho') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'TR');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo) 
SELECT * FROM (SELECT 'TOTAL FÊMEAS NO REBANHO', 'FE', 5, '0', 'br.com.milksys.service.indicadores.TotalFemeas') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'FE');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo) 
SELECT * FROM (SELECT 'TOTAL DE VACAS NO REBANHO', 'VC', 6, '200', 'br.com.milksys.service.indicadores.TotalVacas') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'VC');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo) 
SELECT * FROM (SELECT '% DE VACAS NO REBANHO', '%VC', 7, '200', 'br.com.milksys.service.indicadores.PercentualVacasRebanho') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = '%VC');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo) 
SELECT * FROM (SELECT 'NOVILHAS', 'NV', 8, '200', 'br.com.milksys.service.indicadores.TotalNovilhas') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'NV');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo) 
SELECT * FROM (SELECT 'NOVILHAS ATÉ 12 MESES', 'NV12', 9, '0', 'br.com.milksys.service.indicadores.TotalNovilhas12Meses') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'NV12');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo) 
SELECT * FROM (SELECT 'NOVILHAS DE 12 A 18 MESES', 'NV18', 10, '0', 'br.com.milksys.service.indicadores.TotalNovilhas12a18Meses') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'NV18');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo) 
SELECT * FROM (SELECT 'NOVILHAS DE 18 A 24 MESES', 'NV24', 11, '0', 'br.com.milksys.service.indicadores.TotalNovilhas18a24Meses') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'NV24');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo) 
SELECT * FROM (SELECT 'NOVILHAS MAIS DE 24 MESES', 'NV>24', 12, '0', 'br.com.milksys.service.indicadores.TotalNovilhasMais24Meses') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'NV>24');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo, definicao) 
SELECT * FROM (SELECT 'VACAS EM LACTAÇÃO', 'VCL', 13, '200', 'br.com.milksys.service.indicadores.TotalVacasLactacao',
'A relação vacas secas/lactação mede a eficiência reprodutiva do rebanho e determina lucro ou prejuízo, sendo o desequilíbrio dessa relação uma das causas de maiores prejuízos na atividade leiteira. Existem dois fatores básicos que determinam a porcentagem de vacas secas e de vacas em lactação. São eles a persistência de lactação, definida pela raça ou grau de sangue, e o período de serviço (tempo gasto para emprenhar a vaca depois do parto), determinado pela ocorrência ou não de cios pós-parto e a eficiência da monta ou IA (Inseminação Artificial).') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'VCL');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo) 
SELECT * FROM (SELECT 'VACAS SECAS', 'VCS', 14, '200', 'br.com.milksys.service.indicadores.TotalVacasSecas') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'VCS');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo) 
SELECT * FROM (SELECT 'TAXA DE DETECÇÃO DE CIO OU TAXA DE SERVIÇO (%)', 'TDC', 15, '200', 'br.com.milksys.service.indicadores.TaxaDeteccaoCio') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'TDC');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo) 
SELECT * FROM (SELECT 'TAXA DE PRENHEZ (%)', 'TP', 16, '200', 'br.com.milksys.service.indicadores.TaxaPrenhez') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'TP');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo, definicao) 
SELECT * FROM (SELECT 'RELAÇÃO VACAS SECAS x VACAS EM LACTAÇÃO', 'VSVL', 17, '20', 'br.com.milksys.service.indicadores.RelacaoVacasSecasXVacasLactacao',
'A relação vacas secas/lactação mede a eficiência reprodutiva do rebanho e determina lucro ou prejuízo, sendo o desequilíbrio dessa relação uma das causas de maiores prejuízos na atividade leiteira. Existem dois fatores básicos que determinam a porcentagem de vacas secas e de vacas em lactação. São eles a persistência de lactação, definida pela raça ou grau de sangue, e o período de serviço (tempo gasto para emprenhar a vaca depois do parto), determinado pela ocorrência ou não de cios pós-parto e a eficiência da monta ou IA (Inseminação Artificial).') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'VSVL');

