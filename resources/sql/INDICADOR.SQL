INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, query) 
VALUES ('DIAS EM LACTAÇÃO', 'DEL', 1, '200', 'select 

(select sum(datediff(p.data, e.data)) from viewanimaisativos a
inner join cobertura c inner join parto p on (p.cobertura = c.id)
inner join encerramentolactacao e on (e.animal = a.id)
where c.femea = a.id)

/

(select count(*) from viewanimaisativos a
inner join cobertura c inner join parto p on (p.cobertura = c.id)
inner join encerramentolactacao e on (e.animal = a.id)
where c.femea = a.id)');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, query) 
VALUES ('DIAS EM ABERTO', 'DEA', 2, '200', 'select 

(select sum(datediff(e.data, p.data)) from viewanimaisativos a
inner join cobertura c inner join parto p on (p.cobertura = c.id)
inner join encerramentolactacao e on (e.animal = a.id)
where c.femea = a.id)

/

(select count(*) from viewanimaisativos a
inner join cobertura c inner join parto p on (p.cobertura = c.id)
inner join encerramentolactacao e on (e.animal = a.id)
where c.femea = a.id)
');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, query) 
VALUES ('INTERVALO ENTRE PARTOS', 'IEP', 3, '200', '');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, query) 
VALUES ('TAMANHO DO REBANHO', 'TR', 4, '--', 'select count(*) from viewAnimaisAtivos a');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, query) 
VALUES ('FÊMEAS', '--', 5, '--', 'select count(*) from viewAnimaisAtivos where sexo = \''FÊMEA\''');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, query) 
VALUES ('NOVILHAS ATÉ 1 ANO', '--', 6, '--', 
'select count(*) from viewAnimaisAtivos a 
where DATEDIFF(a.dataNascimento, current_date()) <= 360 
and a.sexo = \''FÊMEA\'' and not exists (select 1 from cobertura c inner join parto p on (p.cobertura = c.id) where c.femea = a.id)');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, query) 
VALUES ('NOVILHAS ATÉ 2 ANOS', '--', 7, '--', 'select count(*) from viewAnimaisAtivos a 
where DATEDIFF(a.dataNascimento, current_date()) <= 720 
and a.sexo = \''FÊMEA\'' and not exists (select 1 from cobertura c inner join parto p on (p.cobertura = c.id) where c.femea = a.id)');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, query) 
VALUES ('% VACAS NO REBANHO', '--', 9, '200', 'select 

(select count(*) from viewAnimaisAtivos a where a.sexo = \''FÊMEA\'' and exists 
(select 1 from cobertura c inner join parto p on (p.cobertura = c.id) where c.femea = a.id)) * 100

/ 

(select count(*) from viewAnimaisAtivos b)');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, query) 
VALUES ('% VACAS EM LACTAÇÃO', '--', 10, '200', 'select 

(select count(*) from viewAnimaisAtivos a inner join cobertura c inner join parto p on (p.cobertura = c.id)
where c.femea = a.id and not exists (select 1 from encerramentoLactacao e where e.data > p.data and e.animal = a.id)) * 100

/

(select count(*) from viewAnimaisAtivos a where a.sexo = \''FÊMEA\'' and exists 
(select 1 from cobertura c inner join parto p on (p.cobertura = c.id) where c.femea = a.id))');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, query) 
VALUES ('% VACAS SECAS', '--', 11, '200', 'select 

(select count(*) from viewAnimaisAtivos a inner join encerramentoLactacao e on (e.animal = a.id)
where not exists (select 1 from parto p inner join cobertura c on (c.id = p.cobertura) where p.data > e.data and c.femea = a.id)) * 100

/

(select count(*) from viewAnimaisAtivos a where a.sexo = \''FÊMEA\'' and exists 
(select 1 from cobertura c inner join parto p on (p.cobertura = c.id) where c.femea = a.id))');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, query) 
VALUES ('% NOVILHAS', '--', 12, '200', '');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, query) 
VALUES ('TAXA DE PRENHEZ (%)', '--', 13, '200', '');