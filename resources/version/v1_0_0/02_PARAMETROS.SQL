insert into parametro (sigla, descricao, valor) 
select * from (select 'PVE', 'PERÍODO VOLUNTÁRIO DE ESPERA (DIAS)', '40' ) as r where not exists (select 1 from parametro where sigla = 'PVE') limit 1;

insert into parametro (sigla, descricao, valor) 
select * from (select 'PMEC', 'PERÍODO MINIMO ENTRE COBERTURAS (DIAS)', '21' ) as r where not exists (select 1 from parametro where sigla = 'PMEC') limit 1;

insert into parametro (sigla, descricao, valor) 
select * from (select 'IDMC', 'IDADE MÍNIMA PARA COBERTURA (MESES)', '18' ) as r where not exists (select 1 from parametro where sigla = 'IDMC') limit 1;
