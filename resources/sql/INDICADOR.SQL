INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo, definicao)
SELECT * FROM ( SELECT 'DIAS EM LACTAÇÃO', 'DEL', 1, '200', 'br.com.milkmoney.service.indicadores.DiasEmLactacao',
'Dias em lactação é um indicador que apresenta a média dos dias de lactação do rebanho. O cálculo consiste na soma dos dias de lactação após cada parto, dividido pelo número de lactações encontrado. Os valores de referência são...') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'DEL');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo) 
SELECT * FROM (SELECT 'DIAS EM ABERTO', 'DEA', 2, '200', 'br.com.milkmoney.service.indicadores.DiasEmAberto') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'DEA');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo, definicao) 
SELECT * FROM (SELECT 'INTERVALO ENTRE PARTOS', 'IEP', 3, '200', 'br.com.milkmoney.service.indicadores.IntervaloEntrePartos',
'Este dado reflete o insucesso do manejo reprodutivo anteriormente aos partos, mas não aponta as causas das falhas. O intervalo entre partos atual é o cálculo do número de meses entre o parto mais recente e o parto anterior das vacas com mais de um parto. Neste dado não entram as vacas de primeira lactação. Em rebanhos com alta persistência de lactação, os dados mostram que rebanhos com intervalo entre partos atual menor que 11,7 meses produzem menos leite do que rebanhos com intervalos entre partos entre 11,8 e 13 meses.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'IEP');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo) 
SELECT * FROM (SELECT 'TAMANHO DO REBANHO', 'TR', 4, '0', 'br.com.milkmoney.service.indicadores.TamanhoRebanho') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'TR');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo) 
SELECT * FROM (SELECT 'TOTAL FÊMEAS NO REBANHO', 'FE', 5, '0', 'br.com.milkmoney.service.indicadores.TotalFemeas') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'FE');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo) 
SELECT * FROM (SELECT 'TOTAL DE VACAS NO REBANHO', 'VC', 6, '200', 'br.com.milkmoney.service.indicadores.TotalVacas') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'VC');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo) 
SELECT * FROM (SELECT '% DE VACAS NO REBANHO', '%VC', 7, '200', 'br.com.milkmoney.service.indicadores.PercentualVacasRebanho') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = '%VC');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo) 
SELECT * FROM (SELECT 'NOVILHAS', 'NV', 8, '200', 'br.com.milkmoney.service.indicadores.TotalNovilhas') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'NV');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo) 
SELECT * FROM (SELECT 'NOVILHAS ATÉ 12 MESES', 'NV12', 9, '0', 'br.com.milkmoney.service.indicadores.TotalNovilhas12Meses') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'NV12');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo) 
SELECT * FROM (SELECT 'NOVILHAS DE 12 A 18 MESES', 'NV18', 10, '0', 'br.com.milkmoney.service.indicadores.TotalNovilhas12a18Meses') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'NV18');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo) 
SELECT * FROM (SELECT 'NOVILHAS DE 18 A 24 MESES', 'NV24', 11, '0', 'br.com.milkmoney.service.indicadores.TotalNovilhas18a24Meses') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'NV24');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo) 
SELECT * FROM (SELECT 'NOVILHAS MAIS DE 24 MESES', 'NV>24', 12, '0', 'br.com.milkmoney.service.indicadores.TotalNovilhasMais24Meses') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'NV>24');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo, definicao) 
SELECT * FROM (SELECT 'VACAS EM LACTAÇÃO', 'VCL', 13, '200', 'br.com.milkmoney.service.indicadores.TotalVacasLactacao',
'A relação vacas secas/lactação mede a eficiência reprodutiva do rebanho e determina lucro ou prejuízo, sendo o desequilíbrio dessa relação uma das causas de maiores prejuízos na atividade leiteira. Existem dois fatores básicos que determinam a porcentagem de vacas secas e de vacas em lactação. São eles a persistência de lactação, definida pela raça ou grau de sangue, e o período de serviço (tempo gasto para emprenhar a vaca depois do parto), determinado pela ocorrência ou não de cios pós-parto e a eficiência da monta ou IA (Inseminação Artificial).') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'VCL');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo) 
SELECT * FROM (SELECT 'VACAS SECAS', 'VCS', 14, '200', 'br.com.milkmoney.service.indicadores.TotalVacasSecas') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'VCS');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo) 
SELECT * FROM (SELECT 'TAXA DE DETECÇÃO DE CIO OU TAXA DE SERVIÇO (%)', 'TDC', 15, '200', 'br.com.milkmoney.service.indicadores.TaxaDeteccaoCio') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'TDC');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo, definicao) 
SELECT * FROM (SELECT 'TAXA DE PRENHEZ (%)', 'TP', 16, '200', 'br.com.milkmoney.service.indicadores.TaxaPrenhes',
'O intervalo da primeira IA até a concepção representa a taxa de prenhes, ou seja, a velocidade em que a vaca fica gestante. Esse intervalo varia muito entre vacas do mesmo rebanho. Dois fatores determinam a taxa de prenhes: 1 - Taxa de concepção; 2 - Taxa de detecção de cio ou taxa de serviço. ') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'TP');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo, definicao) 
SELECT * FROM (SELECT 'RELAÇÃO VACAS SECAS x VACAS EM LACTAÇÃO', 'VSVL', 17, '20', 'br.com.milkmoney.service.indicadores.RelacaoVacasSecasXVacasLactacao',
'A relação vacas secas/lactação mede a eficiência reprodutiva do rebanho e determina lucro ou prejuízo, sendo o desequilíbrio dessa relação uma das causas de maiores prejuízos na atividade leiteira. Existem dois fatores básicos que determinam a porcentagem de vacas secas e de vacas em lactação. São eles a persistência de lactação, definida pela raça ou grau de sangue, e o período de serviço (tempo gasto para emprenhar a vaca depois do parto), determinado pela ocorrência ou não de cios pós-parto e a eficiência da monta ou IA (Inseminação Artificial).') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'VSVL');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo, definicao) 
SELECT * FROM (SELECT 'DIAS PÓS PARTO NO PRIMEIRO SERVIÇO', 'DPPPS', 18, '60', 'br.com.milkmoney.service.indicadores.RelacaoVacasSecasXVacasLactacao',
'O número de dias pós-parto no primeiro serviço é influenciado pelo período voluntário de espera (PVE) e é o cálculo do número de dias do parto até o primeiro serviço para as vacas inseminadas.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'DPPPS');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo, definicao) 
SELECT * FROM (SELECT 'NÚMERO DE SERVIÇOS POR CONCEPÇÃO', 'NSPC', 19, '2', 'br.com.milkmoney.service.indicadores.RelacaoVacasSecasXVacasLactacao',
'Número de serviços necessários até que se atinja a concepção.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'NSPC');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, classeCalculo, definicao) 
SELECT * FROM (SELECT 'PERÍODO DE SERVIÇO', 'PS', 20, '2', 'br.com.milkmoney.service.indicadores.RelacaoVacasSecasXVacasLactacao',
'Para obter um intervalo entre partos entre 12 e 13 meses, é necessário que o Período de Serviço de um rebanho varie de 85 a 120 dias.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'PS');



Dias em aberto = 123 dias
Período de gestação = 279 dias
Intervalo entre parto projetado (IEPP) = (123 + 279)/30,25 = 13,3 meses