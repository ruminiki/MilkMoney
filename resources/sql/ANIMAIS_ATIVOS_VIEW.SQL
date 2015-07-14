create or replace view viewAnimaisAtivos as
select * from ANIMAL a where
not exists (select 1 from animalVendido av where av.animal = a.id) and 
not exists (select 1 from morteAnimal m where m.animal = a.id)