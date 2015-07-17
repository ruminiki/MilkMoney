package br.com.milksys.model;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import br.com.milksys.util.DateUtil;

public class Lactacao {

	private Date  dataInicio;
	private Date  dataFim;
	
	public Date getDataInicio() {
		return dataInicio;
	}
	
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}
	
	public Date getDataFim() {
		return dataFim;
	}
	
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	
	public int getDuracaoLactacaoDias() {
		return (int) ChronoUnit.DAYS.between(DateUtil.asLocalDate(dataInicio), DateUtil.asLocalDate(dataFim));
	}
	
	public int getDuracaoLactacaoMeses() {
		return (int) ChronoUnit.MONTHS.between(DateUtil.asLocalDate(dataInicio), DateUtil.asLocalDate(dataFim));
	}
	
	@Override
	public String toString() {
		return DateUtil.format(dataInicio) + " - " + DateUtil.format(dataFim);
	}
	
}
