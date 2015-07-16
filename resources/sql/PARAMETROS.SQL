insert into parametro (sigla, descricao, valor) 
select * from (select 'PVE', 'PERÍODO VOLUNTÁRIO DE ESPERA (DIAS)', '40' ) as r where not exists (select 1 from parametro where sigla = 'PVE') limit 1;

insert into parametro (sigla, descricao, valor) 
select * from (select 'DEC', 'DIAS ENTRE COBERTURAS (DIAS)', '21' ) as r where not exists (select 1 from parametro where sigla = 'DEC') limit 1;
