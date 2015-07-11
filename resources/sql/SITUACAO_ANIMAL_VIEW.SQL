CREATE OR REPLACE VIEW viewSituacaoAnimal AS

SELECT 'VENDIDO' as situacao, v.animal FROM animalVendido v

UNION ALL

SELECT 'MORTO' as situacao, m.animal FROM morteAnimal m

UNION ALL

SELECT 'SECO' as situacao, e.animal FROM encerramentoLactacao e 
WHERE NOT EXISTS( SELECT 1 FROM parto p INNER JOIN cobertura c ON (c.id = p.cobertura) 
WHERE p.data >= e.data and c.femea = e.animal)

UNION ALL

SELECT 'EM LACTAÇÃO' as situacao, a.id FROM animal a 
INNER JOIN cobertura c on (c.femea = a.id) INNER JOIN parto p on (p.cobertura = c.id)
WHERE NOT EXISTS( SELECT 1 FROM encerramentoLactacao e WHERE e.data > p.data and e.animal = a.id)
