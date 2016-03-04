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
(1,12,15,'INTERVALO ENTRE PARTOS','br.com.milkmoney.service.indicadores.IntervaloEntrePartos',1,'IEP','Este dado reflete o insucesso do manejo reprodutivo anteriormente aos partos, mas n�o aponta as causas das falhas. O intervalo entre partos atual � o c�lculo do n�mero de meses entre o parto mais recente e o parto anterior das vacas com mais de um parto. Neste dado n�o entram as vacas de primeira lacta��o. Em rebanhos com alta persist�ncia de lacta��o, os dados mostram que rebanhos com intervalo entre partos atual menor que 11,7 meses produzem menos leite do que rebanhos com intervalos entre partos entre 11,8 e 13 meses.','Z','N�MERO INTEIRO','m','DENTRO OU ABAIXO DO INTERVALO IDEAL');

INSERT INTO milkmoney.indicador (id,menorValorIdeal,maiorValorIdeal,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo,objetivo)VALUES
(2,85,115,'PER�ODO DE SERVI�O','br.com.milkmoney.service.indicadores.PeriodoServico',2,'PS','O intervalo do parto at� a concep��o, tamb�m conhecido como Dias em Aberto e Per�odo de Servi�o, � o intervalo do parto at� a concep��o. Caso n�o tenha sido realizado a confirma��o da concep��o, � calculado o intervalo do parto at� a data da �ltima cobertura. Se a vaca n�o tiver cobertura cadastrada ap�s o parto, � calculado o intervalo do parto at� a data atual. Esse per�odo est� diretamente ligado a data do primeiro servi�o ap�s o parto.','Z','N�MERO INTEIRO','d','DENTRO OU ABAIXO DO INTERVALO IDEAL');

INSERT INTO milkmoney.indicador (id,menorValorIdeal,maiorValorIdeal,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo,objetivo)VALUES
(3,50,70,'TAXA DE SERVI�O (%)','br.com.milkmoney.service.indicadores.TaxaServico',3,'TS','A Taxa de Servi�o, tamb�m conhecida como Taxa de Detec��o de Cio, mede o n�mero de vacas inseminadas em um per�odo de 21 dias dentre os animais que estavam dispon�veis para inseminar. A taxa de servi�o � reflexo da taxa de detec��o de cio, pois para a vaca ser inseminada, o cio tem que ser detectado.','Z','DECIMAL UMA CASA','%','DENTRO OU ACIMA DO INTERVALO IDEAL');

INSERT INTO milkmoney.indicador (id,menorValorIdeal,maiorValorIdeal,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo,objetivo)VALUES
(4,70,80,'TAXA DE CONCEP��O (%)','br.com.milkmoney.service.indicadores.TaxaConcepcao',4,'TC','A taxa de concep��o mede o n�mero de animais que ficaram gestantes no per�odo de 21 dias em rela��o ao n�mero de animais que foram enseminados no per�odo.','Z','DECIMAL UMA CASA','%','DENTRO OU ACIMA DO INTERVALO IDEAL');

INSERT INTO milkmoney.indicador (id,menorValorIdeal,maiorValorIdeal,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo,objetivo)VALUES
(5,35,50,'TAXA DE PRENHEZ (%)','br.com.milkmoney.service.indicadores.TaxaPrenhez',5,'TP','Mede o percentual de animais que ficam gestantes a cada 21 dias em rela��o ao n�mero total de vacas que estavam dispon�veis para enseminar no mesmo per�odo. Vale lembrar que mesmo com uma Taxa de Concep��o alta a Taxa de Prenhes (TP) pode ser baixa, pois a Taxa de Concep��o (TC) se refere apenas aos animais inseminados no per�odo, enquanto que a Taxa de Prenhez (TP) se refere a todos os animais que estavam dispon�veis para serem inseminados. Por isso no geral ela � mais importante. Os valores ideias giram em torno de 25 a 35%.','Z','DECIMAL UMA CASA','%','DENTRO OU ACIMA DO INTERVALO IDEAL');

INSERT INTO milkmoney.indicador (id,menorValorIdeal,maiorValorIdeal,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo,objetivo)VALUES
(6,50,60,'% DE VACAS PRENHES','br.com.milkmoney.service.indicadores.PercentualVacasPrenhes',6,'%VP','Nesse indicador � registrado o percentual de vacas prenhes em rela�ao ao total de vacas do rebanho.','Z','N�MERO INTEIRO','%','DENTRO OU ACIMA DO INTERVALO IDEAL');

INSERT INTO milkmoney.indicador (id,menorValorIdeal,maiorValorIdeal,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo,objetivo)VALUES
(7,80,100,'% DE VACAS EM LACTA��O','br.com.milkmoney.service.indicadores.PercentualVacasLactacao',7,'%VL','Obtido dividindo-se o n�mero de vacas em lacta��o pelo n�mero total de vacas do rebanho, multiplicado por 100. A %VL ideal � de 83%, o que somente pode ser obtido com IP de 12 meses e dura��o da lacta��o de 305 dias. Valores abaixo de 80% s�o representativos de uma indica��o de desempenho reprodutivo inadequado ou baixa persist�ncia de lacta��o.','Z','N�MERO INTEIRO','%','DENTRO OU ACIMA DO INTERVALO IDEAL');

INSERT INTO milkmoney.indicador (id,menorValorIdeal,maiorValorIdeal,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo,objetivo)VALUES
(8,60,115,'PRIMEIRO SERVI�O AP�S O PARTO','br.com.milkmoney.service.indicadores.PrimeiroServicoAposParto',8,'PSPP','O n�mero de dias p�s-parto no primeiro servi�o � influenciado pelo per�odo volunt�rio de espera (PVE). É o c�lculo do n�mero de dias do parto at� o primeiro servi�o para as vacas inseminadas. Para um intervalo entre partos de 12 meses esse per�odo deve ser no m�ximo de 85 dias.','Z','N�MERO INTEIRO','d','DENTRO OU ABAIXO DO INTERVALO IDEAL');

INSERT INTO milkmoney.indicador (id,menorValorIdeal,maiorValorIdeal,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo,objetivo)VALUES
(9,1.0,1.7,'N�MERO DE SERVI�OS POR CONCEP��O','br.com.milkmoney.service.indicadores.NumeroServicosPorConcepcao',9,'NSPC','N�mero de servi�os necess�rios at� que se atinja a concep��o. Cada repeti��o de cio necessitar� de um novo servi�o. No geral esse indicador se refere a quantidade de coberturas e insemina��es que s�o necess�rias at� que o animal fique gestante. Como cada ensemina��o tem um intervalo de pelo menos 21 dias, significa que a cada repeti��o o animal tende a ampliar em 21 dias pelo menos, o seu intervalo entre partos.','Z','DECIMAL UMA CASA','','DENTRO OU ABAIXO DO INTERVALO IDEAL');

INSERT INTO milkmoney.indicador (id,menorValorIdeal,maiorValorIdeal,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo,objetivo)VALUES
(10,100,200,'DIAS EM LACTA��O','br.com.milkmoney.service.indicadores.DiasEmLactacao',10,'DEL','Nesse indicador � registrado a m�dia de dias p�s parto das vacas em lacta��o. Uma m�dia entre 100 e 200 dias indica a maioria dos animais no pico da produ��o na lacta��o.','Z','N�MERO INTEIRO','d','DENTRO DO INTERVALO IDEAL');

INSERT INTO milkmoney.indicador (id,menorValorIdeal,maiorValorIdeal,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo,objetivo)VALUES
(11,15,20,'VACAS SECAS x VACAS EM LACTA��O','br.com.milkmoney.service.indicadores.RelacaoVacasSecasXVacasLactacao',11,'VSVL','A rela��o vacas secas/lacta��o mede a efici�ncia reprodutiva do rebanho e determina lucro ou preju�zo, sendo o desequil�brio dessa rela��o uma das causas de maiores preju�zos na atividade leiteira. ','Z','DECIMAL UMA CASA','%', 'DENTRO OU ABAIXO DO INTERVALO IDEAL');