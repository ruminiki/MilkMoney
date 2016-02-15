package br.com.milkmoney.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.milkmoney.components.FieldRequired;
import br.com.milkmoney.util.NumberFormatUtil;


@Entity
@Table(name="configuracaoIndicador")
@NamedQuery(name="ConfiguracaoIndicador.findAll", query="SELECT r FROM ConfiguracaoIndicador r")
public class ConfiguracaoIndicador extends AbstractEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private StringProperty ano                  = new SimpleStringProperty(String.valueOf(BigDecimal.ZERO));
	private StringProperty menorValorEsperado   = new SimpleStringProperty(String.valueOf(BigDecimal.ZERO));
	private StringProperty maiorValorEsperado   = new SimpleStringProperty(String.valueOf(BigDecimal.ZERO));
	private StringProperty objetivo             = new SimpleStringProperty(ObjetivoIndicador.DENTRO_DO_INTERVALO_IDEAL);
	private ObjectProperty<Indicador> indicador = new SimpleObjectProperty<Indicador>();
	
	public ConfiguracaoIndicador() {
	}
	
	public ConfiguracaoIndicador(Indicador indicador) {
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
		return Integer.parseInt(this.ano.get());
	}

	public void setAno(int ano) {
		this.ano.set(String.valueOf(ano));
	}
	
	public StringProperty anoProperty(){
		return ano;
	}

	@Access(AccessType.PROPERTY)
	@FieldRequired(message="menor valor esperado")
	public BigDecimal getMenorValorEsperado() {
		if ( getIndicador() != null ){
			if ( getIndicador().getFormato().equals(FormatoIndicador.INTEIRO_FORMAT) ){
				return NumberFormatUtil.intFromString(menorValorEsperado.get());
			}
			
			if ( getIndicador().getFormato().equals(FormatoIndicador.DECIMAL_FORMAT_DUAS_CASAS) ){
				return NumberFormatUtil.fromString(menorValorEsperado.get(), 2);
			}
			
			if ( getIndicador().getFormato().equals(FormatoIndicador.DECIMAL_FORMAT_UMA_CASA) ){
				return NumberFormatUtil.fromString(menorValorEsperado.get(), 1);
			}
		}
		
		return NumberFormatUtil.fromString(menorValorEsperado.get());
	}

	public void setMenorValorEsperado(BigDecimal menorValorEsperado) {
		this.menorValorEsperado.set(NumberFormatUtil.decimalFormat(menorValorEsperado));
	}
	
	public StringProperty menorValorEsperadoProperty(){
		return menorValorEsperado;
	}
	
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="maior valor esperado")
	public BigDecimal getMaiorValorEsperado() {
		if ( getIndicador() != null ){
			if ( getIndicador().getFormato().equals(FormatoIndicador.INTEIRO_FORMAT) ){
				return NumberFormatUtil.intFromString(maiorValorEsperado.get());
			}
			
			if ( getIndicador().getFormato().equals(FormatoIndicador.DECIMAL_FORMAT_DUAS_CASAS) ){
				return NumberFormatUtil.fromString(maiorValorEsperado.get(), 2);
			}
			
			if ( getIndicador().getFormato().equals(FormatoIndicador.DECIMAL_FORMAT_UMA_CASA) ){
				return NumberFormatUtil.fromString(maiorValorEsperado.get(), 1);
			}
		}
		return NumberFormatUtil.fromString(maiorValorEsperado.get());
	}

	public void setMaiorValorEsperado(BigDecimal maiorValorEsperado) {
		this.maiorValorEsperado.set(NumberFormatUtil.decimalFormat(maiorValorEsperado));
	}
	
	public StringProperty maiorValorEsperadoProperty(){
		return maiorValorEsperado;
	}

	@Access(AccessType.PROPERTY)
	@FieldRequired(message="objetivo")
	public String getObjetivo() {
		return objetivo.get();
	}

	public void setObjetivo(String objetivo) {
		this.objetivo.set(objetivo);
	}
	
	public StringProperty objetivoProperty(){
		return objetivo;
	}
	
	@Access(AccessType.PROPERTY)
	@OneToOne(targetEntity=Indicador.class)
	@JoinColumn(name="indicador")
	public Indicador getIndicador() {
		return indicador.get();
	}
	
	public void setIndicador(Indicador indicador) {
		this.indicador.set(indicador);
	}
	
	public ObjectProperty<Indicador> indicadorProperty(){
		return indicador;
	}
	
}