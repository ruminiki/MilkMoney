package br.com.milkmoney.model;

import java.math.BigDecimal;

import br.com.milkmoney.util.Util;


public class Projecao {

	private int        mesReferencia;
	private int        anoReferencia;
	private int        numeroAnimaisLactacao;
	private BigDecimal percentualVariacaoNumeroAnimaisLactacao;
	private int        numeroAnimaisSecos;
	private BigDecimal percentualVariacaoNumeroAnimaisSecos;
	private BigDecimal producaoDiaria;
	private BigDecimal percentualVariacaoProducaoDiaria;
	private BigDecimal producaoMensal;
	private BigDecimal faturamentoMensal;
	private BigDecimal percentualVariacaFaturamentoMensal;
	

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

	public BigDecimal getPercentualVariacaoNumeroAnimaisLactacao() {
		return percentualVariacaoNumeroAnimaisLactacao;
	}

	public void setPercentualVariacaoNumeroAnimaisLactacao(
			BigDecimal percentualVariacaoNumeroAnimaisLactacao) {
		this.percentualVariacaoNumeroAnimaisLactacao = percentualVariacaoNumeroAnimaisLactacao;
	}

	public BigDecimal getPercentualVariacaoNumeroAnimaisSecos() {
		return percentualVariacaoNumeroAnimaisSecos;
	}

	public void setPercentualVariacaoNumeroAnimaisSecos(
			BigDecimal percentualVariacaoNumeroAnimaisSecos) {
		this.percentualVariacaoNumeroAnimaisSecos = percentualVariacaoNumeroAnimaisSecos;
	}

	public BigDecimal getProducaoDiaria() {
		return producaoDiaria;
	}

	public void setProducaoDiaria(BigDecimal producaoDiaria) {
		this.producaoDiaria = producaoDiaria;
	}

	public BigDecimal getPercentualVariacaoProducaoDiaria() {
		return percentualVariacaoProducaoDiaria;
	}

	public void setPercentualVariacaoProducaoDiaria(
			BigDecimal percentualVariacaoProducaoDiaria) {
		this.percentualVariacaoProducaoDiaria = percentualVariacaoProducaoDiaria;
	}

	public BigDecimal getProducaoMensal() {
		return producaoMensal;
	}

	public void setProducaoMensal(BigDecimal producaoMensal) {
		this.producaoMensal = producaoMensal;
	}

	public BigDecimal getFaturamentoMensal() {
		return faturamentoMensal;
	}

	public void setFaturamentoMensal(BigDecimal faturamentoMensal) {
		this.faturamentoMensal = faturamentoMensal;
	}

	public BigDecimal getPercentualVariacaFaturamentoMensal() {
		return percentualVariacaFaturamentoMensal;
	}

	public void setPercentualVariacaFaturamentoMensal(
			BigDecimal percentualVariacaFaturamentoMensal) {
		this.percentualVariacaFaturamentoMensal = percentualVariacaFaturamentoMensal;
	}

	public String getPeriodo(){
		return Util.generateListMonthsAbrev().get(mesReferencia - 1) + "/" + anoReferencia;
	}

}
