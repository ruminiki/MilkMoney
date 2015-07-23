package br.com.milkmoney.model;

public enum State {

	INSERT, 
	UPDATE, 
	LIST, 
	INSERT_TO_SELECT, 
	CREATE_TO_SELECT,//CREATE_TO_SELECT utilizado para os casos em que o objeto deve ser salvo em cascata
	PRIMEIRO_TOQUE,
	RECONFIRMACAO,
	REPETICAO;
	
}
