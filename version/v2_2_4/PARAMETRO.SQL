insert into parametro (sigla, descricao, valor) 
select * from (select 'PAER', 'PERÍODO AVALIAÇÃO DA EFICIÊNCIA REPRODUTIVA (ANOS)', '2' ) 
as r where not exists (select 1 from parametro where sigla = 'PAER') limit 1;
