create table valorIndicador(
	id integer not null auto_increment primary key, 
	menorValorIdeal decimal(19,2) null,
	maiorValorIdeal decimal(19,2) null,
	valor decimal(19,2) null,
	objetivo varchar(45) null,
	mesReferencia varchar(3),
	anoReferencia integer);
