insert into tipoProcedimento (descricao) 
select * from (select 'VACINA��O' ) as r where not exists (select 1 from tipoProcedimento where descricao = 'VACINA��O') limit 1;
insert into tipoProcedimento (descricao) 
select * from (select 'MOCHA' ) as r where not exists (select 1 from tipoProcedimento where descricao = 'MOCHA') limit 1;
insert into tipoProcedimento (descricao) 
select * from (select 'CASTRA��O' ) as r where not exists (select 1 from tipoProcedimento where descricao = 'CASTRA��O') limit 1;
