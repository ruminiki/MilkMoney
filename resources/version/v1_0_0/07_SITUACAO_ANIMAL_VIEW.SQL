CREATE OR REPLACE VIEW viewSituacaoAnimal AS
SELECT a.id AS animal,
COALESCE((SELECT 'VENDIDA' FROM animalVendido v WHERE v.animal = a.id),
		(SELECT 'MORTA' FROM morteAnimal m WHERE m.animal = a.id),
        (SELECT 'NÃO DEFINIDA' FROM animal a1 WHERE a1.id = a.id GROUP BY 1 HAVING (SELECT COUNT(*) FROM lactacao lc WHERE lc.animal = a.id) <= 0),
        (SELECT 'EM LACTAÇÃO' FROM animal a2 WHERE  a2.id = a.id AND EXISTS (SELECT 1 FROM lactacao lc WHERE lc.animal = a.id and lc.dataFim is null)),
        'SECA') AS situacao
FROM animal a

