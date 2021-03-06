INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao)
SELECT * FROM ( SELECT 'DIAS EM LACTA��O', 'DEL', 1, '290', '310', 'br.com.milkmoney.service.indicadores.DiasEmLactacao',
'Dias em lacta��o � um indicador que apresenta a m�dia dos dias de lacta��o do rebanho. O c�lculo consiste na soma dos dias de lacta��o ap�s cada parto, dividido pelo n�mero de lacta��es encontrado.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'DEL');

INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao) 
SELECT * FROM (SELECT 'DIAS EM ABERTO', 'DEA', 2, '50', '100', 'br.com.milkmoney.service.indicadores.DiasEmAberto',
'Dias em aberto � o per�odo que a vaca fica seca, sem produzir.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'DEA');

INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao) 
SELECT * FROM (SELECT 'INTERVALO ENTRE PARTOS', 'IEP', 3, '12', '15', 'br.com.milkmoney.service.indicadores.IntervaloEntrePartos',
'Este dado reflete o insucesso do manejo reprodutivo anteriormente aos partos, mas n�o aponta as causas das falhas. O intervalo entre partos atual � o c�lculo do n�mero de meses entre o parto mais recente e o parto anterior das vacas com mais de um parto. Neste dado n�o entram as vacas de primeira lacta��o. Em rebanhos com alta persist�ncia de lacta��o, os dados mostram que rebanhos com intervalo entre partos atual menor que 11,7 meses produzem menos leite do que rebanhos com intervalos entre partos entre 11,8 e 13 meses.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'IEP');

INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao) 
SELECT * FROM (SELECT 'TAMANHO DO REBANHO', 'TR', 4, '0' as menor, '0' as maior, 'br.com.milkmoney.service.indicadores.TamanhoRebanho',
'Tamanho total do rebanho.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'TR');

INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao) 
SELECT * FROM (SELECT 'TOTAL F�MEAS NO REBANHO', 'FE', 5, '0' as menor, '0' as maior, 'br.com.milkmoney.service.indicadores.TotalFemeas',
'Total de f�meas no rebanho.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'FE');

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
SELECT * FROM (SELECT 'NOVILHAS AT� 12 MESES', 'NV12', 9, '0' as menor, '0' as maior, 'br.com.milkmoney.service.indicadores.TotalNovilhas12Meses',
'Total de novilhas com at� 1 (um) ano de idade.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'NV12');

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
SELECT * FROM (SELECT 'VACAS EM LACTA��O', 'VCL', 13, '0' as menor, '0' as maior, 'br.com.milkmoney.service.indicadores.TotalVacasLactacao',
'N�mero de vacas em lacta��o no rebanho. Para considerar que a vaca est� em lacta��o � necess�rio existir pelo menos um parto cadastrado.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'VCL');

INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao) 
SELECT * FROM (SELECT 'VACAS SECAS', 'VCS', 14, '0' as menor, '0' as maior, 'br.com.milkmoney.service.indicadores.TotalVacasSecas',
'N�mero de vacas secas no rebanho. Para considerar a vaca seca o sistema exige que tenha sido registrado o encerramento da lacta��o do animal.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'VCS');

INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao) 
SELECT * FROM (SELECT 'TAXA DE SERVI�O (%)', 'TDC', 15, '40', '60', 'br.com.milkmoney.service.indicadores.TaxaDeteccaoCio',
'A porcentagem de vacas aptas a serem inseminadas no per�odo de 21 dias que s�o realmente inseminadas. A taxa de servi�o � reflexo da taxa de detec��o de cio, pois para a vaca ser inseminada, tem que ser detectada em cio antes.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'TDC');

INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao) 
SELECT * FROM (SELECT 'TAXA DE PRENHES (%)', 'TP', 16, '45', '80', 'br.com.milkmoney.service.indicadores.TaxaPrenhes',
'O intervalo da primeira insemina��o ap�s o parto, ou primeira insemina��o no caso de novilhas, at� a concep��o.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'TP');

INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao) 
SELECT * FROM (SELECT 'DIAS P�S PARTO NO PRIMEIRO SERVI�O', 'DPPPS', 17, '60', '90', 'br.com.milkmoney.service.indicadores.DiasPosPartoNoPrimeiroServico',
'O n�mero de dias p�s-parto no primeiro servi�o � influenciado pelo per�odo volunt�rio de espera (PVE) e � o c�lculo do n�mero de dias do parto at� o primeiro servi�o para as vacas inseminadas.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'DPPPS');

INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao) 
SELECT * FROM (SELECT 'VACAS SECAS x VACAS EM LACTA��O', 'VSVL', 18, '17', '20', 'br.com.milkmoney.service.indicadores.RelacaoVacasSecasXVacasLactacao',
'A rela��o vacas secas/lacta��o mede a efici�ncia reprodutiva do rebanho e determina lucro ou preju�zo, sendo o desequil�brio dessa rela��o uma das causas de maiores preju�zos na atividade leiteira. Existem dois fatores b�sicos que determinam a porcentagem de vacas secas e de vacas em lacta��o. S�o eles a persist�ncia de lacta��o, definida pela ra�a ou grau de sangue, e o per�odo de servi�o (tempo gasto para emprenhar a vaca depois do parto), determinado pela ocorr�ncia ou n�o de cios p�s-parto e a efici�ncia da monta ou IA (Insemina��o Artificial).') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'VSVL');

INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao) 
SELECT * FROM (SELECT 'N�MERO DE SERVI�OS POR CONCEP��O', 'NSPC', 19, '1', '3', 'br.com.milkmoney.service.indicadores.NumeroServicosPorConcepcao',
'N�mero de servi�os necess�rios at� que se atinja a concep��o.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'NSPC');

INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao) 
SELECT * FROM (SELECT 'INTERVALO ENTRE PARTOS PROJETADO', 'IEPP', 21, '12', '15', 'br.com.milkmoney.service.indicadores.IntervaloEntrePartosProjetado',
'O objetivo dessa compara��o � saber se a efici�ncia reprodutiva da propriedade nos �ltimos 9 meses esta melhorando, se mantendo ou piorando.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'IEPP');

INSERT INTO INDICADOR (descricao, sigla, ordem, menorValorIdeal, maiorValorIdeal, classeCalculo, definicao) 
SELECT * FROM (SELECT 'M�DIA DIAS P�S PARTO', 'MDPP', 22, '100', '200', 'br.com.milkmoney.service.indicadores.MediaDiasPosParto',
'Nesse indicador � registrado a m�dia de dias p�s parto das vacas em lacta��o. Uma m�dia entre 100 e 200 dias indica a maioria dos animais no pico da produ��o na lacta��o.') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'MDPP');
