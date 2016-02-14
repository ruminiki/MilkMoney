package br.com.milkmoney.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import br.com.milkmoney.components.FieldRequired;
import br.com.milkmoney.util.NumberFormatUtil;


@Entity
@Table(name="valorIndicador")
@NamedQuery(name="ValorIndicador.findAll", query="SELECT r FROM ValorIndicador r ")
public class ValorIndicador extends AbstractEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private BigDecimal valor;
	private int        ano;
	private int        mes;
	private Indicador  indicador;
	
	public ValorIndicador() {
		
	}
	
	public ValorIndicador(Indicador indicador) {
		setIndicador(indicador);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Access(AccessType.PROPERTY)
	@FieldRequired(message="ano")
	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}
	
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="mês")
	public int getMes() {
		return mes;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}
	
	@Access(AccessType.PROPERTY)
	public BigDecimal getValor() {
		return valor;
	}

	@Access(AccessType.PROPERTY)
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	public String getValorFormatado(){
		if ( getIndicador().getFormato().equals(FormatoIndicador.INTEIRO_FORMAT) ){
			return NumberFormatUtil.intFormat(valor);
		}
		
		if ( getIndicador().getFormato().equals(FormatoIndicador.DECIMAL_FORMAT_DUAS_CASAS) ){
			return NumberFormatUtil.decimalFormat(valor, 2);
		}
		
		if ( getIndicador().getFormato().equals(FormatoIndicador.DECIMAL_FORMAT_UMA_CASA) ){
			return NumberFormatUtil.decimalFormat(valor, 1);
		}
		
		return NumberFormatUtil.decimalFormat(valor);
	}

	@Access(AccessType.PROPERTY)
	@ManyToOne(targetEntity=Indicador.class)
	@JoinColumn(name="indicador")
	public Indicador getIndicador() {
		return indicador;
	}
	
	public void setIndicador(Indicador indicador) {
		this.indicador = indicador;
	}
	
}