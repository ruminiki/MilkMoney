package br.com.milkmoney.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import br.com.milkmoney.components.FieldRequired;
import br.com.milkmoney.util.NumberFormatUtil;


@Entity
@Table(name="indicador")
@NamedQuery(name="Indicador.findAll", query="SELECT r FROM Indicador r order by r.ordem")
public class Indicador extends AbstractEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private StringProperty descricao = new SimpleStringProperty();
	private StringProperty sigla = new SimpleStringProperty();
	private StringProperty valorReferencia = new SimpleStringProperty(String.valueOf(BigDecimal.ZERO));
	private StringProperty valorApurado = new SimpleStringProperty(String.valueOf(BigDecimal.ZERO));
	private StringProperty definicao = new SimpleStringProperty();
	private StringProperty classeCalculo = new SimpleStringProperty();
	private StringProperty objetivo = new SimpleStringProperty(ObjetivoIndicador.ACIMA_VALOR_REFERENCIA);
	private int ordem;
	
	public Indicador() {
	}
	
	public Indicador(String descricao, String sigla, BigDecimal valorApurado) {
		setDescricao(descricao);
		setSigla(sigla);
		setValorApurado(valorApurado);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Access(AccessType.PROPERTY)
	@FieldRequired(message="descrição")
	public String getDescricao() {
		return descricao.get();
	}

	public void setDescricao(String descricao) {
		this.descricao.set(descricao);
	}
	
	public StringProperty descricaoProperty(){
		return descricao;
	}

	@Access(AccessType.PROPERTY)
	@FieldRequired(message="sigla")
	public String getSigla() {
		return sigla.get();
	}

	public void setSigla(String sigla) {
		this.sigla.set(sigla);
	}
	
	public StringProperty siglaProperty(){
		return sigla;
	}

	@Access(AccessType.PROPERTY)
	@FieldRequired(message="valor de referência")
	public BigDecimal getValorReferencia() {
		return NumberFormatUtil.fromString(valorReferencia.get());
	}

	public void setValorReferencia(BigDecimal valorReferencia) {
		this.valorReferencia.set(NumberFormatUtil.decimalFormat(valorReferencia));
	}
	
	public StringProperty valorReferenciaProperty(){
		return valorReferencia;
	}

	@Access(AccessType.PROPERTY)
	@FieldRequired(message="classe de cálculo")
	public String getClasseCalculo() {
		return classeCalculo.get();
	}

	public void setClasseCalculo(String classeCalculo) {
		this.classeCalculo.set(classeCalculo);
	}
	
	public StringProperty classeCalculoProperty(){
		return classeCalculo;
	}

	@Access(AccessType.PROPERTY)
	public BigDecimal getValorApurado() {
		return NumberFormatUtil.fromString(valorApurado.get());
	}

	@Access(AccessType.PROPERTY)
	public void setValorApurado(BigDecimal valorApurado) {
		this.valorApurado.set(NumberFormatUtil.decimalFormat(valorApurado));
	}

	public StringProperty valorApuradoProperty(){
		return valorApurado;
	}
	
	@Access(AccessType.PROPERTY)
	public int getOrdem() {
		return ordem;
	}

	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}
	
	@Access(AccessType.PROPERTY)
	@Type(type="text")
	public String getDefinicao() {
		return definicao.get();
	}

	public void setDefinicao(String definicao) {
		this.definicao.set(definicao);
	}
	
	public StringProperty definicaoProperty(){
		return definicao;
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
	@Transient
	public BigDecimal getResultado() {
		
		BigDecimal resultado = BigDecimal.ZERO;
		if ( getValorApurado().compareTo(resultado) > 0 && getValorReferencia().compareTo(resultado) > 0 ){
			resultado = getValorApurado().multiply(BigDecimal.valueOf(100)).divide(getValorReferencia(), 2, RoundingMode.HALF_EVEN);
		}
		
		if ( resultado.compareTo(BigDecimal.valueOf(100)) > 0 ){
			resultado = resultado.subtract(BigDecimal.valueOf(100));
		}
		
		if ( resultado.compareTo(BigDecimal.valueOf(100)) < 0 ){
			resultado = BigDecimal.valueOf(100).subtract(resultado);
		}
		
		if ( null != getObjetivo() && getObjetivo().equals(ObjetivoIndicador.ACIMA_VALOR_REFERENCIA) ){
			if ( getValorApurado().compareTo(getValorReferencia()) < 0 ){
				resultado = resultado.multiply(BigDecimal.valueOf(-1));
			}
		}
		
		if ( null != getObjetivo() &&  getObjetivo().equals(ObjetivoIndicador.ABAIXO_VALOR_REFERENCIA) ){
			if ( getValorApurado().compareTo(getValorReferencia()) > 0 ){
				resultado = resultado.multiply(BigDecimal.valueOf(-1));
			}
		}
		
		return resultado;
		
	}
	
	@Override
	public String toString() {
		return getDescricao();
	}
	
}