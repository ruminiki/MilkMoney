insert into tipoProcedimento (descricao) 
select * from (select 'VACINA플O' ) as r where not exists (select 1 from tipoProcedimento where descricao = 'VACINA플O') limit 1;
insert into tipoProcedimento (descricao) 
select * from (select 'MOCHA' ) as r where not exists (select 1 from tipoProcedimento where descricao = 'MOCHA') limit 1;
insert into tipoProcedimento (descricao) 
select * from (select 'CASTRA플O' ) as r where not exists (select 1 from tipoProcedimento where descricao = 'CASTRA플O') limit 1;
