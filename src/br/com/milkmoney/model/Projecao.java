package br.com.milkmoney.model;

import br.com.milkmoney.util.Util;


public class Projecao {

	private int    mesReferencia;
	private int    anoReferencia;
	private int    numeroAnimaisLactacao;
	private double percentualVariacaoNumeroAnimaisLactacao;
	private int    numeroAnimaisSecos;
	private double percentualVariacaoNumeroAnimaisSecos;
	private double producaoDiaria;
	private double percentualVariacaoProducaoDiaria;
	private double producaoMensal;
	private double faturamentoMensal;
	private double percentualVariacaFaturamentoMensal;
	

	public Projecao() {
	}

	public Projecao(int mesReferencia, int anoReferencia) {
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
	
	public double getPercentualVariacaoNumeroAnimaisLactacao() {
		return percentualVariacaoNumeroAnimaisLactacao;
	}

	public void setPercentualVariacaoNumeroAnimaisLactacao(
			double percentualVariacaoNumeroAnimaisLactacao) {
		this.percentualVariacaoNumeroAnimaisLactacao = percentualVariacaoNumeroAnimaisLactacao;
	}

	public double getPercentualVariacaoNumeroAnimaisSecos() {
		return percentualVariacaoNumeroAnimaisSecos;
	}

	public void setPercentualVariacaoNumeroAnimaisSecos(
			double percentualVariacaoNumeroAnimaisSecos) {
		this.percentualVariacaoNumeroAnimaisSecos = percentualVariacaoNumeroAnimaisSecos;
	}

	public double getPercentualVariacaoProducaoDiaria() {
		return percentualVariacaoProducaoDiaria;
	}

	public void setPercentualVariacaoProducaoDiaria(
			double percentualVariacaoProducaoDiaria) {
		this.percentualVariacaoProducaoDiaria = percentualVariacaoProducaoDiaria;
	}

	public double getPercentualVariacaFaturamentoMensal() {
		return percentualVariacaFaturamentoMensal;
	}

	public void setPercentualVariacaFaturamentoMensal(
			double percentualVariacaFaturamentoMensal) {
		this.percentualVariacaFaturamentoMensal = percentualVariacaFaturamentoMensal;
	}

	public String getPeriodo(){
		return Util.generateListMonthsAbrev().get(mesReferencia - 1) + "/" + anoReferencia;
	}

}
