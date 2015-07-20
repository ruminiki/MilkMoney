CREATE OR REPLACE VIEW viewSituacaoAnimal AS
SELECT a.id AS animal,
COALESCE((SELECT 'VENDIDA' FROM animalVendido v WHERE v.animal = a.id),
		(SELECT 'MORTA' FROM morteAnimal m WHERE m.animal = a.id),
        (SELECT 'NÃO DEFINIDA' FROM animal a WHERE NOT EXISTS (SELECT 1 FROM lactacao lc WHERE lc.animal = a.id)),
        (SELECT 'EM LACTAÇÃO' FROM animal a WHERE EXISTS (SELECT 1 FROM lactacao lc WHERE lc.animal = a.id and lc.dataFim is null)),
        'SECO') AS situacao
FROM animal a

