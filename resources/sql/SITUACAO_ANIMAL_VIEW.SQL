CREATE OR REPLACE VIEW viewSituacaoAnimal AS
SELECT a.id as animal,
COALESCE((SELECT 'VENDIDO' FROM animalVendido v WHERE v.animal = a.id),
		(SELECT 'MORTO' FROM morteAnimal m WHERE m.animal = a.id),
		(SELECT 'SECO' FROM encerramentoLactacao e 
		WHERE NOT EXISTS( SELECT 1 FROM parto p INNER JOIN cobertura c ON (c.id = p.cobertura) 
		WHERE p.data >= e.data and c.femea = e.animal) and e.animal = a.id),
		(SELECT 'EM LACTAÇÃO' FROM animal a 
		INNER JOIN cobertura c on (c.femea = a.id) INNER JOIN parto p on (p.cobertura = c.id)
		WHERE NOT EXISTS( SELECT 1 FROM encerramentoLactacao e WHERE e.data > p.data and e.animal = a.id)),'NÃO DEFINIDO') as situacao
FROM animal a