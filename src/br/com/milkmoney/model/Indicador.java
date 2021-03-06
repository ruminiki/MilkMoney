package br.com.milkmoney.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
	private int                         id;
	private StringProperty              descricao = new SimpleStringProperty();
	private StringProperty              sigla = new SimpleStringProperty();
	private StringProperty              definicao = new SimpleStringProperty();
	private StringProperty              formato = new SimpleStringProperty();
	private StringProperty              sufixo = new SimpleStringProperty();
	private StringProperty              classeCalculo = new SimpleStringProperty();
	private StringProperty              menorValorIdeal = new SimpleStringProperty(String.valueOf(BigDecimal.ZERO));
	private StringProperty              maiorValorIdeal = new SimpleStringProperty(String.valueOf(BigDecimal.ZERO));
	private StringProperty              objetivo = new SimpleStringProperty(ObjetivoIndicador.DENTRO_DO_INTERVALO_IDEAL);
	private List<ConfiguracaoIndicador> configuracoesIndicador = new ArrayList<ConfiguracaoIndicador>();
	private List<ValorIndicador>        valores = new ArrayList<ValorIndicador>();
	private int                         ordem;
	
	public Indicador() {
	}
	
	public Indicador(String descricao, String sigla) {
		setDescricao(descricao);
		setSigla(sigla);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Access(AccessType.PROPERTY)
	@FieldRequired(message="descri��o")
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
	@FieldRequired(message="classe de c�lculo")
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
	public String getFormato() {
		return formato.get();
	}

	public void setFormato(String formato) {
		this.formato.set(formato);
	}
	
	public StringProperty formatoProperty(){
		return formato;
	}
	
	@Access(AccessType.PROPERTY)
	public String getSufixo() {
		return sufixo.get();
	}

	public void setSufixo(String sufixo) {
		this.sufixo.set(sufixo);
	}
	
	public StringProperty sufixoProperty(){
		return sufixo;
	}
	
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="menor valor ideal")
	public BigDecimal getMenorValorIdeal() {
		if ( getFormato().equals(FormatoIndicador.INTEIRO_FORMAT) ){
			return NumberFormatUtil.intFromString(menorValorIdeal.get());
		}
		
		if ( getFormato().equals(FormatoIndicador.DECIMAL_FORMAT_DUAS_CASAS) ){
			return NumberFormatUtil.fromString(menorValorIdeal.get(), 2);
		}
		
		if ( getFormato().equals(FormatoIndicador.DECIMAL_FORMAT_UMA_CASA) ){
			return NumberFormatUtil.fromString(menorValorIdeal.get(), 1);
		}
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
		if ( getFormato().equals(FormatoIndicador.INTEIRO_FORMAT) ){
			return NumberFormatUtil.intFromString(maiorValorIdeal.get());
		}
		
		if ( getFormato().equals(FormatoIndicador.DECIMAL_FORMAT_DUAS_CASAS) ){
			return NumberFormatUtil.fromString(maiorValorIdeal.get(), 2);
		}
		
		if ( getFormato().equals(FormatoIndicador.DECIMAL_FORMAT_UMA_CASA) ){
			return NumberFormatUtil.fromString(maiorValorIdeal.get(), 1);
		}
		return NumberFormatUtil.fromString(maiorValorIdeal.get());
	}

	public void setMaiorValorIdeal(BigDecimal maiorValorIdeal) {
		this.maiorValorIdeal.set(NumberFormatUtil.decimalFormat(maiorValorIdeal));
	}
	
	public StringProperty maiorValorIdealProperty(){
		return maiorValorIdeal;
	}
	
	@Access(AccessType.PROPERTY)
	@OneToMany(orphanRemoval=true, targetEntity=ValorIndicador.class, cascade={CascadeType.MERGE, CascadeType.REMOVE}, fetch=FetchType.EAGER)
	@JoinColumn(name="indicador")
	public List<ValorIndicador> getValores() {
		if ( valores == null )
			return new ArrayList<ValorIndicador>();
		return valores;
	}
	
	public void setValores(List<ValorIndicador> valores) {
		this.valores = valores;
	}
	
	@Transient
	public ValorIndicador getValorIndicador(int ano, int mes){
		for ( ValorIndicador valor : getValores() ){
			if ( valor.getAno() == ano && valor.getMes() == mes ){
				return valor;
			}
		}
		return null;
	}
	
	@Access(AccessType.PROPERTY)
	@OneToMany(orphanRemoval=true, targetEntity=ConfiguracaoIndicador.class, cascade={CascadeType.MERGE, CascadeType.REMOVE}, fetch=FetchType.EAGER)
	@JoinColumn(name="indicador")
	public List<ConfiguracaoIndicador> getConfiguracoesIndicador() {
		if ( configuracoesIndicador == null )
			return new ArrayList<ConfiguracaoIndicador>();
		return configuracoesIndicador;
	}
	
	public void setConfiguracoesIndicador(List<ConfiguracaoIndicador> configuracoesIndicador) {
		this.configuracoesIndicador = configuracoesIndicador;
	}
	
	@Transient
	public ConfiguracaoIndicador getConfiguracaoIndicador(int ano){
		for ( ConfiguracaoIndicador ci : getConfiguracoesIndicador() ){
			if ( ci.getAno() == ano ){
				return ci;
			}
		}
		return null;
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