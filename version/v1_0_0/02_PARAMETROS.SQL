insert into parametro (sigla, descricao, valor) 
select * from (select 'PVE', 'PER�ODO VOLUNT�RIO DE ESPERA (DIAS)', '40' ) as r where not exists (select 1 from parametro where sigla = 'PVE') limit 1;

insert into parametro (sigla, descricao, valor) 
select * from (select 'PMEC', 'PER�ODO MINIMO ENTRE COBERTURAS (DIAS)', '21' ) as r where not exists (select 1 from parametro where sigla = 'PMEC') limit 1;

insert into parametro (sigla, descricao, valor) 
select * from (select 'IDMC', 'IDADE M�NIMA PARA COBERTURA (MESES)', '18' ) as r where not exists (select 1 from parametro where sigla = 'IDMC') limit 1;
