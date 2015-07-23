insert into propriedade (descricao, endereco, area) 
select * from (select 'MINHA PROPRIEDADE', 'EM ALGUM LUGAR NO MUNDO', 0,0 ) as r where not exists (select 1 from propriedade where descricao = 'MINHA PROPRIEDADE') limit 1;
