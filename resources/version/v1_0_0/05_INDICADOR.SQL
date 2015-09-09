INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao)
SELECT * FROM ( SELECT 'DIAS EM LACTAÇÃO', 'DEL', 1, '290', '310', 'br.com.milkmoney.service.indicadores.DiasEmLactacao',
'Dias em lactação é um indicador que apresenta a média dos dias de lactação do rebanho. O cálculo consiste na soma dos dias de lactação após cada parto, dividido pelo número de lactações encontrado.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'DEL');

INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao) 
SELECT * FROM (SELECT 'DIAS EM ABERTO', 'DEA', 2, '50', '100', 'br.com.milkmoney.service.indicadores.DiasEmAberto',
'Dias em aberto é o período que a vaca fica seca, sem produzir.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'DEA');

INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao) 
SELECT * FROM (SELECT 'INTERVALO ENTRE PARTOS', 'IEP', 3, '12', '15', 'br.com.milkmoney.service.indicadores.IntervaloEntrePartos',
'Este dado reflete o insucesso do manejo reprodutivo anteriormente aos partos, mas não aponta as causas das falhas. O intervalo entre partos atual é o cálculo do número de meses entre o parto mais recente e o parto anterior das vacas com mais de um parto. Neste dado não entram as vacas de primeira lactação. Em rebanhos com alta persistência de lactação, os dados mostram que rebanhos com intervalo entre partos atual menor que 11,7 meses produzem menos leite do que rebanhos com intervalos entre partos entre 11,8 e 13 meses.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'IEP');

INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao) 
SELECT * FROM (SELECT 'TAMANHO DO REBANHO', 'TR', 4, '0' as menor, '0' as maior, 'br.com.milkmoney.service.indicadores.TamanhoRebanho',
'Tamanho total do rebanho.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'TR');

INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao) 
SELECT * FROM (SELECT 'TOTAL FÊMEAS NO REBANHO', 'FE', 5, '0' as menor, '0' as maior, 'br.com.milkmoney.service.indicadores.TotalFemeas',
'Total de fêmeas no rebanho.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'FE');

INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao) 
SELECT * FROM (SELECT 'TOTAL DE VACAS NO REBANHO', 'VC', 6, '0' as menor, '0' as maior, 'br.com.milkmoney.service.indicadores.TotalVacas',
'Total de vacas no rebanho. O sistema considera vaca o animal que tenha pelo menos um parto cadastrado.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'VC');

INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao) 
SELECT * FROM (SELECT '% DE VACAS NO REBANHO', '%VC', 7, '0' as menor, '0' as maior, 'br.com.milkmoney.service.indicadores.PercentualVacasRebanho',
'Percentual de vacas no rebanho. O sistema considera vaca o animal que tenha pelo menos um parto cadastrado.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = '%VC');

INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao) 
SELECT * FROM (SELECT 'NOVILHAS', 'NV', 8, '0' as menor, '0' as maior, 'br.com.milkmoney.service.indicadores.TotalNovilhas',
'Total de novilhas no rebanho.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'NV');

INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao) 
SELECT * FROM (SELECT 'NOVILHAS ATÉ 12 MESES', 'NV12', 9, '0' as menor, '0' as maior, 'br.com.milkmoney.service.indicadores.TotalNovilhas12Meses',
'Total de novilhas com até 1 (um) ano de idade.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'NV12');

INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao) 
SELECT * FROM (SELECT 'NOVILHAS DE 12 A 18 MESES', 'NV18', 10, '0' as menor, '0' as maior, 'br.com.milkmoney.service.indicadores.TotalNovilhas12a18Meses',
'Total de novilhas com idade entre 12 e 18 meses.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'NV18');

INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao) 
SELECT * FROM (SELECT 'NOVILHAS DE 18 A 24 MESES', 'NV24', 11, '0' as menor, '0' as maior, 'br.com.milkmoney.service.indicadores.TotalNovilhas18a24Meses',
'Total de novilhas com idade entre 18 e 24 meses.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'NV24');

INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao) 
SELECT * FROM (SELECT 'NOVILHAS MAIS DE 24 MESES', 'NV>24', 12, '0' as menor, '0' as maior, 'br.com.milkmoney.service.indicadores.TotalNovilhasMais24Meses',
'Total e novilhas com idade maior que 24 meses.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'NV>24');

INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao) 
SELECT * FROM (SELECT 'VACAS EM LACTAÇÃO', 'VCL', 13, '0' as menor, '0' as maior, 'br.com.milkmoney.service.indicadores.TotalVacasLactacao',
'Número de vacas em lactação no rebanho. Para considerar que a vaca está em lactação é necessário existir pelo menos um parto cadastrado.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'VCL');

INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao) 
SELECT * FROM (SELECT 'VACAS SECAS', 'VCS', 14, '0' as menor, '0' as maior, 'br.com.milkmoney.service.indicadores.TotalVacasSecas',
'Número de vacas secas no rebanho. Para considerar a vaca seca o sistema exige que tenha sido registrado o encerramento da lactação do animal.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'VCS');

INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao) 
SELECT * FROM (SELECT 'TAXA DE SERVIÇO (%)', 'TDC', 15, '40', '60', 'br.com.milkmoney.service.indicadores.TaxaDeteccaoCio',
'A porcentagem de vacas aptas a serem inseminadas no período de 21 dias que são realmente inseminadas. A taxa de serviço é reflexo da taxa de detecção de cio, pois para a vaca ser inseminada, tem que ser detectada em cio antes.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'TDC');

INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao) 
SELECT * FROM (SELECT 'TAXA DE PRENHES (%)', 'TP', 16, '45', '80', 'br.com.milkmoney.service.indicadores.TaxaPrenhes',
'O intervalo da primeira inseminação após o parto, ou primeira inseminação no caso de novilhas, até a concepção.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'TP');

INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao) 
SELECT * FROM (SELECT 'DIAS PÓS PARTO NO PRIMEIRO SERVIÇO', 'DPPPS', 17, '60', '90', 'br.com.milkmoney.service.indicadores.DiasPosPartoNoPrimeiroServico',
'O número de dias pós-parto no primeiro serviço é influenciado pelo período voluntário de espera (PVE) e é o cálculo do número de dias do parto até o primeiro serviço para as vacas inseminadas.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'DPPPS');

INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao) 
SELECT * FROM (SELECT 'VACAS SECAS x VACAS EM LACTAÇÃO', 'VSVL', 18, '17', '20', 'br.com.milkmoney.service.indicadores.RelacaoVacasSecasXVacasLactacao',
'A relação vacas secas/lactação mede a eficiência reprodutiva do rebanho e determina lucro ou prejuízo, sendo o desequilíbrio dessa relação uma das causas de maiores prejuízos na atividade leiteira. Existem dois fatores básicos que determinam a porcentagem de vacas secas e de vacas em lactação. São eles a persistência de lactação, definida pela raça ou grau de sangue, e o período de serviço (tempo gasto para emprenhar a vaca depois do parto), determinado pela ocorrência ou não de cios pós-parto e a eficiência da monta ou IA (Inseminação Artificial).') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'VSVL');

INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao) 
SELECT * FROM (SELECT 'NÚMERO DE SERVIÇOS POR CONCEPÇÃO', 'NSPC', 19, '1', '3', 'br.com.milkmoney.service.indicadores.NumeroServicosPorConcepcao',
'Número de serviços necessários até que se atinja a concepção.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'NSPC');

INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao) 
SELECT * FROM (SELECT 'PERÍODO DE SERVIÇO', 'PS', 20, '85', '120', 'br.com.milkmoney.service.indicadores.PeriodoDeServico',
'Para obter um intervalo entre partos entre 12 e 13 meses, é necessário que o Período de Serviço de um rebanho varie de 85 a 120 dias.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'PS');

INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao) 
SELECT * FROM (SELECT 'INTERVALO ENTRE PARTOS PROJETADO', 'IEPP', 21, '12', '15', 'br.com.milkmoney.service.indicadores.IntervaloEntrePartosProjetado',
'O objetivo dessa comparação é saber se a eficiência reprodutiva da propriedade nos últimos 9 meses esta melhorando, se mantendo ou piorando.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'IEPP');

INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao) 
SELECT * FROM (SELECT 'MÉDIA DIAS PÓS PARTO', 'MDPP', 22, '100', '200', 'br.com.milkmoney.service.indicadores.MediaDiasPosParto',
'Nesse indicador é registrado a média de dias pós parto das vacas em lactação. Uma média entre 100 e 200 dias indica a maioria dos animais no pico da produção na lactação.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'MDPP');
