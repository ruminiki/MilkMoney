package br.com.milkmoney.model;

import java.io.Serializable;
import java.math.BigDecimal;

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
	private StringProperty menorValorIdeal = new SimpleStringProperty(String.valueOf(BigDecimal.ZERO));
	private StringProperty maiorValorIdeal = new SimpleStringProperty(String.valueOf(BigDecimal.ZERO));
	private StringProperty valorApurado = new SimpleStringProperty(String.valueOf(BigDecimal.ZERO));
	private StringProperty definicao = new SimpleStringProperty();
	private StringProperty classeCalculo = new SimpleStringProperty();
	private StringProperty objetivo = new SimpleStringProperty(ObjetivoIndicador.DENTRO_OU_ACIMA_DO_INTERVALO_IDEAL);
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
	@FieldRequired(message="menor valor ideal")
	public BigDecimal getMenorValorIdeal() {
		return NumberFormatUtil.fromString(menorValorIdeal.get());
	}

	public void setMenorValorIdeal(BigDecimal menorValorIdeal) {
		this.menorValorIdeal.set(NumberFormatUtil.decimalFormat(menorValorIdeal));
	}
	
	public StringProperty menorValorIdealProperty(){
		return menorValorIdeal;
	}
	
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="maior valor ideal")
	public BigDecimal getMaiorValorIdeal() {
		return NumberFormatUtil.fromString(maiorValorIdeal.get());
	}

	public void setMaiorValorIdeal(BigDecimal maiorValorIdeal) {
		this.maiorValorIdeal.set(NumberFormatUtil.decimalFormat(maiorValorIdeal));
	}
	
	public StringProperty maiorValorIdealProperty(){
		return maiorValorIdeal;
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
	
	@Override
	public String toString() {
		return getDescricao();
	}
	
}