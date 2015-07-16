insert into raca (descricao) 
select * from (select 'HOLANDESA' ) as r where not exists (select 1 from raca where descricao = 'HOLANDESA') limit 1;
insert into raca (descricao) 
select * from (select 'JERSEY' ) as r where not exists (select 1 from raca where descricao = 'JERSEY') limit 1;
insert into raca (descricao) 
select * from (select 'ZEBU' ) as r where not exists (select 1 from raca where descricao = 'ZEBU') limit 1;