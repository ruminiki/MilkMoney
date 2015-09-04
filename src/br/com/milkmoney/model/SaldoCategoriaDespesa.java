package br.com.milkmoney.model;

import java.math.BigDecimal;

import br.com.milkmoney.util.NumberFormatUtil;

public class SaldoCategoriaDespesa {

	private String categoria;
	private String saldo;
	private String percentual;
	
	public SaldoCategoriaDespesa(String categoria, BigDecimal saldo, BigDecimal percentual) {
		setCategoria(categoria);
		setSaldo(NumberFormatUtil.decimalFormat(saldo));
		setPercentual(NumberFormatUtil.decimalFormat(percentual));
	}
	
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getSaldo() {
		return saldo;
	}
	public void setSaldo(String saldo) {
		this.saldo = saldo;
	}
	public String getPercentual() {
		return percentual;
	}
	public void setPercentual(String percentual) {
		this.percentual = percentual;
	}
}
