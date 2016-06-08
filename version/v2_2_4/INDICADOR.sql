delete from configuracaoIndicador;
delete from valorIndicador;

UPDATE milkmoney.indicador set id = id + 1, ordem = ordem + 1 order by id desc;

INSERT INTO milkmoney.indicador (id,menorValorIdeal,maiorValorIdeal,descricao,classeCalculo,ordem,sigla,definicao,tipo,formato,sufixo,objetivo)VALUES
(1,80,100,'EFICI�NCIA REPRODUTIVA (%)','br.com.milkmoney.service.indicadores.EficienciaReprodutiva',1,'EF',
'Indicador que avalia o percentual de efici�ncia reprodutiva do rebanho, considerando como 100% de efici�ncia o animal que apresenta 1 (um) parto a cada 12 (doze) meses.','Z','DECIMAL UMA CASA','%', 'DENTRO OU ACIMA DO INTERVALO IDEAL');
