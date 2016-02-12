package br.com.milkmoney.service.indicadores;

import java.math.BigDecimal;
import java.util.Date;

public abstract class AbstractCalculadorIndicador {
	
	public static final String DECIMAL_FORMAT_UMA_CASA = "DECIMAL_FORMAT_UMA_CASA";
	public static final String DECIMAL_FORMAT_DUAS_CASAS = "DECIMAL_FORMAT_DUAS_CASAS";
	public static final String INTEIRO_FORMAT = "INTEIRO_FORMAT";
	
	public abstract BigDecimal getValue(Date data);

}
