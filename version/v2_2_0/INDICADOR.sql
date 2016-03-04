CREATE TABLE IF NOT EXISTS milkmoney.valorIndicador (
  id INT(11) NOT NULL AUTO_INCREMENT,
  ano INT(11) NOT NULL DEFAULT 0,
  mes INT(11) NOT NULL DEFAULT 0,
  valor DECIMAL(19,2) NOT NULL DEFAULT 0,
  indicador INT(11) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE INDEX UN (ano, mes, indicador),
  INDEX fk_valorIndicador_indicador1_idx (indicador ASC),
  CONSTRAINT fk_valorIndicador_indicador1
    FOREIGN KEY (indicador)
    REFERENCES milkmoney.indicador (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;

CREATE TABLE IF NOT EXISTS milkmoney.configuracaoIndicador (
  id INT(11) NOT NULL AUTO_INCREMENT,
  ano INT(11) NOT NULL DEFAULT 0,
  menorValorEsperado DECIMAL(19,2) NOT NULL DEFAULT 0,
  maiorValorEsperado DECIMAL(19,2) NOT NULL DEFAULT 0,
  objetivo varchar(45) NOT NULL, 
  indicador INT(11) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE INDEX UN (ano, indicador),
  INDEX fk_valorIndicador_indicador1_idx (indicador ASC),
  CONSTRAINT fk_configuracaoIndicador_indicador1
    FOREIGN KEY (indicador)
    REFERENCES milkmoney.indicador (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;

alter table indicador drop column valorApurado;

delete from configuracaoIndicador;
delete from valorIndicador;
delete from indicador;

INSERT INTO milkmoney.indicador (id,menorValorIdeal,maiorValorIdeal,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo,objetivo)VALUES
(1,12,15,'INTERVALO ENTRE PARTOS','br.com.milkmoney.service.indicadores.IntervaloEntrePartos',1,'IEP','Este dado reflete o insucesso do manejo reprodutivo anteriormente aos partos, mas não aponta as causas das falhas. O intervalo entre partos atual é o cálculo do número de meses entre o parto mais recente e o parto anterior das vacas com mais de um parto. Neste dado não entram as vacas de primeira lactação. Em rebanhos com alta persistência de lactação, os dados mostram que rebanhos com intervalo entre partos atual menor que 11,7 meses produzem menos leite do que rebanhos com intervalos entre partos entre 11,8 e 13 meses.','Z','NÚMERO INTEIRO','m','DENTRO OU ABAIXO DO INTERVALO IDEAL');

INSERT INTO milkmoney.indicador (id,menorValorIdeal,maiorValorIdeal,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo,objetivo)VALUES
(2,85,115,'PERÍODO DE SERVIÇO','br.com.milkmoney.service.indicadores.PeriodoServico',2,'PS','O intervalo do parto até a concepção, também conhecido como Dias em Aberto e Período de Serviço, é o intervalo do parto até a concepção. Caso não tenha sido realizado a confirmação da concepção, é calculado o intervalo do parto até a data da última cobertura. Se a vaca não tiver cobertura cadastrada após o parto, é calculado o intervalo do parto até a data atual. Esse período está diretamente ligado a data do primeiro serviço após o parto.','Z','NÚMERO INTEIRO','d','DENTRO OU ABAIXO DO INTERVALO IDEAL');

INSERT INTO milkmoney.indicador (id,menorValorIdeal,maiorValorIdeal,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo,objetivo)VALUES
(3,50,70,'TAXA DE SERVIÇO (%)','br.com.milkmoney.service.indicadores.TaxaServico',3,'TS','A Taxa de Serviço, também conhecida como Taxa de Detecção de Cio, mede o número de vacas inseminadas em um período de 21 dias dentre os animais que estavam disponíveis para inseminar. A taxa de serviço é reflexo da taxa de detecção de cio, pois para a vaca ser inseminada, o cio tem que ser detectado.','Z','DECIMAL UMA CASA','%','DENTRO OU ACIMA DO INTERVALO IDEAL');

INSERT INTO milkmoney.indicador (id,menorValorIdeal,maiorValorIdeal,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo,objetivo)VALUES
(4,70,80,'TAXA DE CONCEPÇÃO (%)','br.com.milkmoney.service.indicadores.TaxaConcepcao',4,'TC','A taxa de concepção mede o número de animais que ficaram gestantes no período de 21 dias em relação ao número de animais que foram enseminados no período.','Z','DECIMAL UMA CASA','%','DENTRO OU ACIMA DO INTERVALO IDEAL');

INSERT INTO milkmoney.indicador (id,menorValorIdeal,maiorValorIdeal,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo,objetivo)VALUES
(5,35,50,'TAXA DE PRENHEZ (%)','br.com.milkmoney.service.indicadores.TaxaPrenhez',5,'TP','Mede o percentual de animais que ficam gestantes a cada 21 dias em relação ao número total de vacas que estavam disponíveis para enseminar no mesmo período. Vale lembrar que mesmo com uma Taxa de Concepção alta a Taxa de Prenhes (TP) pode ser baixa, pois a Taxa de Concepção (TC) se refere apenas aos animais inseminados no período, enquanto que a Taxa de Prenhez (TP) se refere a todos os animais que estavam disponíveis para serem inseminados. Por isso no geral ela é mais importante. Os valores ideias giram em torno de 25 a 35%.','Z','DECIMAL UMA CASA','%','DENTRO OU ACIMA DO INTERVALO IDEAL');

INSERT INTO milkmoney.indicador (id,menorValorIdeal,maiorValorIdeal,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo,objetivo)VALUES
(6,50,60,'% DE VACAS PRENHES','br.com.milkmoney.service.indicadores.PercentualVacasPrenhes',6,'%VP','Nesse indicador é registrado o percentual de vacas prenhes em relaçao ao total de vacas do rebanho.','Z','NÚMERO INTEIRO','%','DENTRO OU ACIMA DO INTERVALO IDEAL');

INSERT INTO milkmoney.indicador (id,menorValorIdeal,maiorValorIdeal,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo,objetivo)VALUES
(7,80,100,'% DE VACAS EM LACTAÇÃO','br.com.milkmoney.service.indicadores.PercentualVacasLactacao',7,'%VL','Obtido dividindo-se o número de vacas em lactação pelo número total de vacas do rebanho, multiplicado por 100. A %VL ideal é de 83%, o que somente pode ser obtido com IP de 12 meses e duração da lactação de 305 dias. Valores abaixo de 80% são representativos de uma indicação de desempenho reprodutivo inadequado ou baixa persistência de lactação.','Z','NÚMERO INTEIRO','%','DENTRO OU ACIMA DO INTERVALO IDEAL');

INSERT INTO milkmoney.indicador (id,menorValorIdeal,maiorValorIdeal,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo,objetivo)VALUES
(8,60,115,'PRIMEIRO SERVIÇO APÓS O PARTO','br.com.milkmoney.service.indicadores.PrimeiroServicoAposParto',8,'PSPP','O número de dias pós-parto no primeiro serviço é influenciado pelo período voluntário de espera (PVE). Ã‰ o cálculo do número de dias do parto até o primeiro serviço para as vacas inseminadas. Para um intervalo entre partos de 12 meses esse período deve ser no máximo de 85 dias.','Z','NÚMERO INTEIRO','d','DENTRO OU ABAIXO DO INTERVALO IDEAL');

INSERT INTO milkmoney.indicador (id,menorValorIdeal,maiorValorIdeal,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo,objetivo)VALUES
(9,1.0,1.7,'NÚMERO DE SERVIÇOS POR CONCEPÇÃO','br.com.milkmoney.service.indicadores.NumeroServicosPorConcepcao',9,'NSPC','Número de serviços necessários até que se atinja a concepção. Cada repetição de cio necessitará de um novo serviço. No geral esse indicador se refere a quantidade de coberturas e inseminações que são necessárias até que o animal fique gestante. Como cada enseminação tem um intervalo de pelo menos 21 dias, significa que a cada repetição o animal tende a ampliar em 21 dias pelo menos, o seu intervalo entre partos.','Z','DECIMAL UMA CASA','','DENTRO OU ABAIXO DO INTERVALO IDEAL');

INSERT INTO milkmoney.indicador (id,menorValorIdeal,maiorValorIdeal,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo,objetivo)VALUES
(10,100,200,'DIAS EM LACTAÇÃO','br.com.milkmoney.service.indicadores.DiasEmLactacao',10,'DEL','Nesse indicador é registrado a média de dias pós parto das vacas em lactação. Uma média entre 100 e 200 dias indica a maioria dos animais no pico da produção na lactação.','Z','NÚMERO INTEIRO','d','DENTRO DO INTERVALO IDEAL');

INSERT INTO milkmoney.indicador (id,menorValorIdeal,maiorValorIdeal,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo,objetivo)VALUES
(11,15,20,'VACAS SECAS x VACAS EM LACTAÇÃO','br.com.milkmoney.service.indicadores.RelacaoVacasSecasXVacasLactacao',11,'VSVL','A relação vacas secas/lactação mede a eficiência reprodutiva do rebanho e determina lucro ou prejuízo, sendo o desequilíbrio dessa relação uma das causas de maiores prejuízos na atividade leiteira. ','Z','DECIMAL UMA CASA','%', 'DENTRO OU ABAIXO DO INTERVALO IDEAL');