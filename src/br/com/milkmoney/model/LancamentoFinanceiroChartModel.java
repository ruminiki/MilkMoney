package br.com.milkmoney.model;

import java.math.BigDecimal;

public class LancamentoFinanceiroChartModel {

	private String mes;
	private BigDecimal receita;
	private BigDecimal despesa;
	
	public LancamentoFinanceiroChartModel() {
	}

	public LancamentoFinanceiroChartModel(String mes, BigDecimal receita, BigDecimal despesa) {
		setMes(mes);
		setDespesa(despesa);
		setReceita(receita);
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public BigDecimal getReceita() {
		return receita;
	}

	public void setReceita(BigDecimal receita) {
		this.receita = receita;
	}

	public BigDecimal getDespesa() {
		return despesa;
	}

	public void setDespesa(BigDecimal despesa) {
		this.despesa = despesa;
	}

}