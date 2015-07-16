CREATE OR REPLACE VIEW viewSituacaoAnimal AS
SELECT a.id AS animal,
COALESCE((SELECT 'VENDIDA' FROM animalVendido v WHERE v.animal = a.id),
		(SELECT 'MORTA' FROM morteAnimal m WHERE m.animal = a.id),
		(SELECT 'SECA' FROM encerramentoLactacao e 
		WHERE NOT EXISTS( SELECT 1 FROM parto p INNER JOIN cobertura c ON (c.id = p.cobertura) 
		WHERE p.data > e.data AND c.femea = e.animal) AND e.animal = a.id LIMIT 1),
		(SELECT 'EM LACTAÇÃO' FROM cobertura c INNER JOIN parto p ON (p.cobertura = c.id)
		WHERE c.femea = a.id AND NOT EXISTS( SELECT 1 FROM encerramentoLactacao e WHERE e.data > p.data and e.animal = a.id) LIMIT 1),'NÃO DEFINIDA') AS situacao
FROM animal a