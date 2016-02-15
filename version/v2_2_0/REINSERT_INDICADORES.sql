INSERT INTO milkmoney.indicador (id,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo)VALUES
(1,'INTERVALO ENTRE PARTOS','br.com.milkmoney.service.indicadores.IntervaloEntrePartos',1,'IEP','Este dado reflete o insucesso do manejo reprodutivo anteriormente aos partos, mas não aponta as causas das falhas. O intervalo entre partos atual é o cálculo do número de meses entre o parto mais recente e o parto anterior das vacas com mais de um parto. Neste dado não entram as vacas de primeira lactação. Em rebanhos com alta persistência de lactação, os dados mostram que rebanhos com intervalo entre partos atual menor que 11,7 meses produzem menos leite do que rebanhos com intervalos entre partos entre 11,8 e 13 meses.','Z','NÚMERO INTEIRO','m');

INSERT INTO milkmoney.indicador (id,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo)VALUES
(2,'PERÍODO DE SERVIÇO','br.com.milkmoney.service.indicadores.PeriodoServico',2,'IPC','O intervalo do parto até a concepção, também conhecido como Dias em Aberto e Período de Serviço, é o intervalo do parto até a confirmação da concepção. Caso não tenha sido realizado a confirmação da concepção, é calculado o intervalo do parto até a data da última cobertura. Se a vaca não tiver cobertura cadastrada após o parto, é calculado o intervalo do parto até a data atual. Esse período está diretamente ligado a data do primeiro serviço após o parto.','Z','NÚMERO INTEIRO','d');

INSERT INTO milkmoney.indicador (id,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo)VALUES
(3,'TAXA DE SERVIÇO (%)','br.com.milkmoney.service.indicadores.TaxaServico',3,'TS','A Taxa de Serviço, também conhecida como Taxa de Detecção de Cio, mede o número de vacas enseminadas em um período de 21 dias dentre os animais que estavam disponíveis para enseminar. A taxa de serviço é reflexo da taxa de detecção de cio, pois para a vaca ser inseminada, o cio tem que ser detectado.','Z','DECIMAL UMA CASA','%');

INSERT INTO milkmoney.indicador (id,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo)VALUES
(4,'TAXA DE CONCEPÇÃO (%)','br.com.milkmoney.service.indicadores.TaxaConcepcao',4,'TC','A taxa de concepção mede o número de animais que ficaram gestantes no período de 21 dias em relação ao número de animais que foram enseminados no período.','Z','DECIMAL UMA CASA','%')

INSERT INTO milkmoney.indicador (id,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo)VALUES
(5,'TAXA DE PRENHES (%)','br.com.milkmoney.service.indicadores.TaxaPrenhes',5,'TP','Mede o porcentual de animais que ficam gestantes a cada 21 dias em relação ao número total de vacas que estavam disponíveis para enseminar no mesmo período. Vale lembrar que mesmo com uma Taxa de Concepção alta a Taxa de Prenhes (TP) pode ser baixa, pois a Taxa de Concepção (TC) se refere apenas aos animais inseminados no período, enquanto que a Taxa de Prenhes (TP) se refere a todos os animais que estavam disponíveis para serem enseminados. Por isso no geral ela é mais importante. Os valores ideias giram em torno de 25 a 35%.','Z','DECIMAL UMA CASA','%');

INSERT INTO milkmoney.indicador (id,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo)VALUES
(6,'PRIMEIRO SERVIÇO APÓS O PARTO','br.com.milkmoney.service.indicadores.PrimeiroServicoAposParto',6,'PSPP','O número de dias pós-parto no primeiro serviço é influenciado pelo período voluntário de espera (PVE). É o cálculo do número de dias do parto até o primeiro serviço para as vacas inseminadas. Para um intervalo entre partos de 12 meses esse período deve ser no máximo de 85 dias.','Z','NÚMERO INTEIRO','d');

INSERT INTO milkmoney.indicador (id,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo)VALUES
(7,'NÚMERO DE SERVIÇOS POR CONCEPÇÃO','br.com.milkmoney.service.indicadores.NumeroServicosPorConcepcao',7,'NSPC','Número de serviços necessários até que se atinja a concepção. Cada repetição de cio necessitará de um novo serviço. No geral esse indicador se refere a quantidade de coberturas e inseminações que são necessárias até que o animal fique gestante. Como cada enseminação tem um intervalo de pelo menos 21 dias, significa que a cada repetição o animal tende a ampliar em 21 dias pelo menos, o seu intervalo entre partos.','Z','DECIMAL UMA CASA','');

INSERT INTO milkmoney.indicador (id,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo)VALUES
(8,'DIAS EM LACTAÇÃO','br.com.milkmoney.service.indicadores.DiasEmLactacao',8,'DEL','Nesse indicador é registrado a média de dias pós parto das vacas em lactação. Uma média entre 100 e 200 dias indica a maioria dos animais no pico da produção na lactação.','Z','NÚMERO INTEIRO','d');

INSERT INTO milkmoney.indicador (id,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo)VALUES
(9,'TAMANHO DO REBANHO','br.com.milkmoney.service.indicadores.TamanhoRebanho',9,'TR','Tamanho total do rebanho.','R','NÚMERO INTEIRO','');

INSERT INTO milkmoney.indicador (id,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo)VALUES
(10,'VACAS EM LACTAÇÃO','br.com.milkmoney.service.indicadores.TotalVacasLactacao',10,'VCL','Número de vacas em lactação no rebanho. Para considerar que a vaca está em lactação é necessário existir pelo menos um parto cadastrado.','R','NÚMERO INTEIRO','');

INSERT INTO milkmoney.indicador (id,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo)VALUES
(11,'VACAS SECAS','br.com.milkmoney.service.indicadores.TotalVacasSecas',11,'VCS','Número de vacas secas no rebanho. Para considerar a vaca seca o sistema exige que tenha sido registrado o encerramento da lactação do animal.','R','NÚMERO INTEIRO','');

INSERT INTO milkmoney.indicador (id,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo)VALUES
(12,'VACAS SECAS x VACAS EM LACTAÇÃO','br.com.milkmoney.service.indicadores.RelacaoVacasSecasXVacasLactacao',12,'VSVL','A relação vacas secas/lactação mede a eficiência reprodutiva do rebanho e determina lucro ou prejuízo, sendo o desequilíbrio dessa relação uma das causas de maiores prejuízos na atividade leiteira. ','Z','DECIMAL UMA CASA','%');

INSERT INTO milkmoney.indicador (id,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo)VALUES
(13,'TOTAL FÊMEAS NO REBANHO','br.com.milkmoney.service.indicadores.TotalFemeas',13,'FE','Total de fêmeas no rebanho.','R','NÚMERO INTEIRO','');

INSERT INTO milkmoney.indicador (id,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo)VALUES
(14,'TOTAL DE VACAS NO REBANHO','br.com.milkmoney.service.indicadores.TotalVacas',14,'VC','Total de vacas no rebanho. O sistema considera vaca o animal que tenha pelo menos um parto cadastrado.','R','NÚMERO INTEIRO','%');

INSERT INTO milkmoney.indicador (id,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo)VALUES
(15,'% DE VACAS NO REBANHO','br.com.milkmoney.service.indicadores.PercentualVacasRebanho',15,'%VC','Percentual de vacas no rebanho. O sistema considera vaca o animal que tenha pelo menos um parto cadastrado.','R','DECIMAL UMA CASA','');

INSERT INTO milkmoney.indicador (id,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo)VALUES
(16,'NOVILHAS','br.com.milkmoney.service.indicadores.TotalNovilhas',16,'NV','Total de novilhas no rebanho.','R','NÚMERO INTEIRO','');

INSERT INTO milkmoney.indicador (id,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo)VALUES
(17,'NOVILHAS ATÉ 12 MESES','br.com.milkmoney.service.indicadores.TotalNovilhas12Meses',17,'NV12','Total de novilhas com até 1 (um) ano de idade.','R','NÚMERO INTEIRO','');

INSERT INTO milkmoney.indicador (id,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo)VALUES
(18,'NOVILHAS DE 12 A 18 MESES','br.com.milkmoney.service.indicadores.TotalNovilhas12a18Meses',18,'NV18','Total de novilhas com idade entre 12 e 18 meses.','R','NÚMERO INTEIRO','');

INSERT INTO milkmoney.indicador (id,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo)VALUES
(19,'NOVILHAS DE 18 A 24 MESES','br.com.milkmoney.service.indicadores.TotalNovilhas18a24Meses',19,'NV24','Total de novilhas com idade entre 18 e 24 meses.','R','NÚMERO INTEIRO','');

INSERT INTO milkmoney.indicador (id,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo)VALUES
(20,'NOVILHAS MAIS DE 24 MESES','br.com.milkmoney.service.indicadores.TotalNovilhasMais24Meses',20,'NV>24','Total e novilhas com idade maior que 24 meses.','R','NÚMERO INTEIRO','');
