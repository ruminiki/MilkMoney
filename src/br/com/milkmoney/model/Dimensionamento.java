package br.com.milkmoney.model;


public class Dimensionamento {

	private int    mesReferencia;
	private int    anoReferencia;
	private int    numeroAnimaisLactacao;
	private int    numeroAnimaisSecos;
	private double producaoDiaria;
	private double producaoMensal;
	private double faturamentoMensal;

	public Dimensionamento() {
	}

	public Dimensionamento(int mesReferencia, int anoReferencia) {
		setMesReferencia(mesReferencia);
		setAnoReferencia(anoReferencia);
	}

	public int getMesReferencia() {
		return mesReferencia;
	}

	public void setMesReferencia(int mesReferencia) {
		this.mesReferencia = mesReferencia;
	}

	public int getAnoReferencia() {
		return anoReferencia;
	}

	public void setAnoReferencia(int anoReferencia) {
		this.anoReferencia = anoReferencia;
	}

	public int getNumeroAnimaisLactacao() {
		return numeroAnimaisLactacao;
	}

	public void setNumeroAnimaisLactacao(int numeroAnimaisLactacao) {
		this.numeroAnimaisLactacao = numeroAnimaisLactacao;
	}

	public int getNumeroAnimaisSecos() {
		return numeroAnimaisSecos;
	}

	public void setNumeroAnimaisSecos(int numeroAnimaisSecos) {
		this.numeroAnimaisSecos = numeroAnimaisSecos;
	}

	public double getProducaoDiaria() {
		return producaoDiaria;
	}

	public void setProducaoDiaria(double producaoDiaria) {
		this.producaoDiaria = producaoDiaria;
	}

	public double getProducaoMensal() {
		return producaoMensal;
	}

	public void setProducaoMensal(double producaoMensal) {
		this.producaoMensal = producaoMensal;
	}

	public double getFaturamentoMensal() {
		return faturamentoMensal;
	}

	public void setFaturamentoMensal(double faturamentoMensal) {
		this.faturamentoMensal = faturamentoMensal;
	}

}
