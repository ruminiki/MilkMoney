INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, query)
SELECT * FROM ( SELECT 'DIAS EM LACTAÇÃO', 'DEL', 1, '200', 'select 
round((select sum(datediff(coalesce(e.data, current_date()), p.data)) from viewanimaisativos a
inner join cobertura c inner join parto p on (p.cobertura = c.id)
left join encerramentolactacao e on (e.animal = a.id and e.data > p.data)
where c.femea = a.id)
/
(select count(distinct a.id) from viewanimaisativos a
inner join cobertura c inner join parto p on (p.cobertura = c.id)
left join encerramentolactacao e on (e.animal = a.id)
where c.femea = a.id),0) as s') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'DEL');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, query) 
SELECT * FROM (SELECT 'DIAS EM ABERTO', 'DEA', 2, '200', 'select 

(select sum(datediff(p.data, e.data)) from viewanimaisativos a
inner join cobertura c inner join parto p on (p.cobertura = c.id)
inner join encerramentolactacao e on (e.animal = a.id)
where c.femea = a.id and e.data < p.data)

/

(select count(*) from viewanimaisativos a
inner join cobertura c inner join parto p on (p.cobertura = c.id)
inner join encerramentolactacao e on (e.animal = a.id)
where c.femea = a.id)') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'DEA');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, query) 
SELECT * FROM (SELECT 'INTERVALO ENTRE PARTOS', 'IEP', 3, '200', '') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'IEP');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, query) 
SELECT * FROM (SELECT 'TAMANHO DO REBANHO', 'TR', 4, '--', 'select count(*) from viewAnimaisAtivos a') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'TR');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, query) 
SELECT * FROM (SELECT 'TOTAL FÊMEAS', 'FE', 5, '--', 'select count(*) from viewAnimaisAtivos where sexo = \'FÊMEA\\' ') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'FE');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, query) 
SELECT * FROM (SELECT 'VACAS', 'VC', 6, '200', 'select 
(select count(*) from viewAnimaisAtivos a where a.sexo = \'FÊMEA\\' and exists 
(select 1 from cobertura c inner join parto p on (p.cobertura = c.id) where c.femea = a.id))') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'VC');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, query) 
SELECT * FROM (SELECT 'NOVILHAS', 'NV', 7, '200', 'select 
(select count(*) from viewAnimaisAtivos a where a.sexo = \'FÊMEA\\' and
not exists (select 1 from parto p inner join cobertura c on (p.cobertura = c.id) where c.femea = a.id))') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'NV');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, query) 
SELECT * FROM (SELECT 'NOVILHAS ATÉ 1 ANO', 'NV1', 8, '--', 
'select count(*) from viewAnimaisAtivos a 
where DATEDIFF(a.dataNascimento, current_date()) <= 360 
and a.sexo = \'FÊMEA\\'  and not exists (select 1 from cobertura c inner join parto p on (p.cobertura = c.id) where c.femea = a.id)') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'NV1');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, query) 
SELECT * FROM (SELECT 'NOVILHAS ATÉ 2 ANOS', 'NV2', 9, '--', 'select count(*) from viewAnimaisAtivos a 
where DATEDIFF(a.dataNascimento, current_date()) between 360 and 720 
and a.sexo = \'FÊMEA\\'  and not exists (select 1 from cobertura c inner join parto p on (p.cobertura = c.id) where c.femea = a.id)') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'NV2');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, query) 
SELECT * FROM (SELECT 'VACAS EM LACTAÇÃO', 'VCL', 10, '200', 'select 
(select count(*) from viewAnimaisAtivos a inner join cobertura c inner join parto p on (p.cobertura = c.id)
where c.femea = a.id and not exists (select 1 from encerramentoLactacao e where e.data > p.data and e.animal = a.id))') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'VCL');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, query) 
SELECT * FROM (SELECT 'VACAS SECAS', 'VCS', 11, '200', 'select 
(select count(*) from viewAnimaisAtivos a inner join encerramentoLactacao e on (e.animal = a.id)
where not exists (select 1 from parto p inner join cobertura c on (c.id = p.cobertura) where p.data > e.data and c.femea = a.id))') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'VCS');

INSERT INTO INDICADOR (descricao, sigla, ordem, valorReferencia, query) 
SELECT * FROM (SELECT 'TAXA DE PRENHEZ (%)', 'TP', 12, '200', '') AS T WHERE NOT EXISTS (SELECT 1 FROM INDICADOR WHERE SIGLA = 'TP');