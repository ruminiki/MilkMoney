package br.com.milksys.model;

public class Mes {

	public String nome;
	public int mesDoAno;
	
	public Mes(int mesDoAno, String nome) {
		this.mesDoAno = mesDoAno;
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getMesDoAno() {
		return mesDoAno;
	}

	public void setMesDoAno(int mesDoAno) {
		this.mesDoAno = mesDoAno;
	}
	
}
