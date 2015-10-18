package br.com.milkmoney.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.milkmoney.components.FieldRequired;
import br.com.milkmoney.util.DateUtil;


/**
 * The persistent class for the cobertura database table.
 * 
 */
@Entity
@Table(name="cobertura")
@NamedQuery(name="Cobertura.findAll", query="SELECT a FROM Cobertura a")
public class Cobertura extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private ObjectProperty<LocalDate>   data                         = new SimpleObjectProperty<LocalDate>(LocalDate.now());  
	private ObjectProperty<LocalDate>   previsaoParto                = new SimpleObjectProperty<LocalDate>(LocalDate.now().plusDays(282));  
	private StringProperty              tipoCobertura                = new SimpleStringProperty(TipoCobertura.INSEMINACAO_ARTIFICIAL);
	private ObjectProperty<Animal>      femea                        = new SimpleObjectProperty<Animal>();
	private ObjectProperty<Animal>      touroMontaNatural            = new SimpleObjectProperty<Animal>();
	private ObjectProperty<Touro>       touroInseminacaoArtificial   = new SimpleObjectProperty<Touro>();
	private StringProperty              quantidadeDosesUtilizadas    = new SimpleStringProperty();
	private StringProperty              nomeResponsavel              = new SimpleStringProperty();
	private ObjectProperty<Funcionario> funcionarioResponsavel       = new SimpleObjectProperty<Funcionario>();
	private ObjectProperty<Servico>     servico                      = new SimpleObjectProperty<Servico>();
	private StringProperty              observacao                   = new SimpleStringProperty();
	private ObjectProperty<LocalDate>   dataConfirmacaoPrenhes       = new SimpleObjectProperty<LocalDate>();  
	private StringProperty              metodoConfirmacaoPrenhes     = new SimpleStringProperty();
	private StringProperty              observacaoConfirmacaoPrenhes = new SimpleStringProperty();
	private Parto                       parto;
	private StringProperty              situacaoCobertura            = new SimpleStringProperty(SituacaoCobertura.NAO_CONFIRMADA);
	private StringProperty              situacaoConfirmacaoPrenhes   = new SimpleStringProperty(SituacaoCobertura.PRENHA);
	
	public Cobertura() {
	}
	
	public Cobertura(Animal femea) {
		setFemea(femea);
	}

	public Cobertura(Animal femea, Date proximoServico) {
		setFemea(femea);
		setData(proximoServico);
		setPrevisaoParto(DateUtil.asDate(DateUtil.asLocalDate(proximoServico).plusDays(282)));
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="data")
	public Date getData() {
		return DateUtil.asDate(this.data.get());
	}
	
	public void setData(Date data) {
		this.data.set(DateUtil.asLocalDate(data));
	}
	
	public ObjectProperty<LocalDate> dataProperty(){
		return data;
	}
	
	@Access(AccessType.PROPERTY)
	public String getObservacao() {
		return this.observacao.get();
	}

	public void setObservacao(String descricao) {
		this.observacao.set(descricao);
	}

	public StringProperty observacaoProperty(){
		return observacao;
	}

	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	public Date getPrevisaoParto() {
		return DateUtil.asDate(this.previsaoParto.get());
	}
	
	public void setPrevisaoParto(Date previsaoParto) {
		this.previsaoParto.set(DateUtil.asLocalDate(previsaoParto));
	}
	
	public ObjectProperty<LocalDate> previsaoPartoProperty(){
		return previsaoParto;
	}
	
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="tipo de cobertura")
	public String getTipoCobertura() {
		return tipoCobertura.get();
	}
	
	public void setTipoCobertura(String tipoCobertura) {
		this.tipoCobertura.set(tipoCobertura);
	}
	
	public StringProperty tipoCoberturaProperty(){
		return tipoCobertura;
	}
	
	@Access(AccessType.PROPERTY)
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="femea")
	@FieldRequired(message="fêmea")
	public Animal getFemea() {
		return femea.get();
	}
	
	public void setFemea(Animal femea) {
		this.femea.set(femea);
	}
	
	public ObjectProperty<Animal> femeaProperty(){
		return femea;
	}
	
	@Access(AccessType.PROPERTY)
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="touroMontaNatural")
	public Animal getTouroMontaNatural() {
		return touroMontaNatural.get();
	}
	
	public void setTouroMontaNatural(Animal touroMontaNatural) {
		this.touroMontaNatural.set(touroMontaNatural);
	}
	
	public ObjectProperty<Animal> touroMontaNaturalProperty(){
		return touroMontaNatural;
	}
	
	@Access(AccessType.PROPERTY)
	public String getSituacaoCobertura() {
		if ( this.getParto() != null ){
			return SituacaoCobertura.PARIDA; 
		}
		return situacaoCobertura.get() == null ? SituacaoCobertura.NAO_CONFIRMADA : situacaoCobertura.get();
	}

	public void setSituacaoCobertura(String situacaoCobertura) {
		this.situacaoCobertura.set(situacaoCobertura);
	}
	
	public StringProperty situacaoCoberturaProperty(){
		return situacaoCobertura;
	}
	
	@Access(AccessType.PROPERTY)
	public String getSituacaoConfirmacaoPrenhes() {
		return situacaoConfirmacaoPrenhes.get() == null ? SituacaoCobertura.NAO_CONFIRMADA : situacaoConfirmacaoPrenhes.get();
	}

	public void setSituacaoConfirmacaoPrenhes(String situacaoConfirmacaoPrenhes) {
		this.situacaoConfirmacaoPrenhes.set(situacaoConfirmacaoPrenhes);
	}
	
	public StringProperty situacaoConfirmacaoPrenhesProperty(){
		return situacaoConfirmacaoPrenhes;
	}

	@Access(AccessType.PROPERTY)
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="touroInseminacaoArtificial")
	public Touro getTouroInseminacaoArtificial() {
		return touroInseminacaoArtificial.get();
	}
	
	public void setTouroInseminacaoArtificial(Touro touroInseminacaoArtificial) {
		this.touroInseminacaoArtificial.set(touroInseminacaoArtificial);
	}
	
	public ObjectProperty<Touro> touroInseminacaoArtificialProperty(){
		return touroInseminacaoArtificial;
	}
	
	@Access(AccessType.PROPERTY)
	public int getQuantidadeDosesUtilizadas() {
		if ( quantidadeDosesUtilizadas.get() != null )
			return Integer.valueOf(this.quantidadeDosesUtilizadas.get());
		return 0;
	}
	
	public void setQuantidadeDosesUtilizadas(int quantidadeDosesUtilizadas) {
		this.quantidadeDosesUtilizadas.set(String.valueOf(quantidadeDosesUtilizadas));
	}
	
	public StringProperty quantidadeDosesUtilizadasProperty(){
		return quantidadeDosesUtilizadas;
	}
	
	@Access(AccessType.PROPERTY)
	public String getNomeResponsavel() {
		return this.nomeResponsavel.get();
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel.set(nomeResponsavel);
	}

	public StringProperty nomeResponsavelProperty(){
		return nomeResponsavel;
	}
	
	@Access(AccessType.PROPERTY)
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="funcionarioResponsavel")
	public Funcionario getFuncionarioResponsavel() {
		return funcionarioResponsavel.get();
	}
	
	public void setFuncionarioResponsavel(Funcionario funcionarioResponsavel) {
		this.funcionarioResponsavel.set(funcionarioResponsavel);
	}
	
	public ObjectProperty<Funcionario> funcionarioResponsavelProperty(){
		return funcionarioResponsavel;
	}
	
	@Access(AccessType.PROPERTY)
	@OneToOne(orphanRemoval=true, targetEntity=Servico.class, cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	@JoinColumn(name="servico")
	public Servico getServico() {
		return servico.get();
	}
	
	public void setServico(Servico servico) {
		this.servico.set(servico);
	}
	
	public ObjectProperty<Servico> servicoProperty(){
		return servico;
	}
	
	@Access(AccessType.PROPERTY)
	@OneToOne(orphanRemoval=true, targetEntity=Parto.class, cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	@JoinColumn(name="parto")
	public Parto getParto() {
		return parto;
	}

	public void setParto(Parto parto) {
		this.parto = parto;
	}
	
	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	public Date getDataConfirmacaoPrenhes() {
		return DateUtil.asDate(this.dataConfirmacaoPrenhes.get());
	}
	
	public void setDataConfirmacaoPrenhes(Date dataConfirmacaoPrenhes) {
		this.dataConfirmacaoPrenhes.set(DateUtil.asLocalDate(dataConfirmacaoPrenhes));
	}
	
	public ObjectProperty<LocalDate> dataConfirmacaoPrenhesProperty(){
		return dataConfirmacaoPrenhes;
	}
	
	@Access(AccessType.PROPERTY)
	public String getMetodoConfirmacaoPrenhes() {
		return metodoConfirmacaoPrenhes.get();
	}

	public void setMetodoConfirmacaoPrenhes(String metodoConfirmacaoPrenhes) {
		this.metodoConfirmacaoPrenhes.set(metodoConfirmacaoPrenhes);
	}
	
	public StringProperty metodoConfirmacaoPrenhesProperty(){
		return metodoConfirmacaoPrenhes;
	}
	
	@Access(AccessType.PROPERTY)
	public String getObservacaoConfirmacaoPrenhes() {
		return observacaoConfirmacaoPrenhes.get();
	}

	public void setObservacaoConfirmacaoPrenhes(String observacaoConfirmacaoPrenhes) {
		this.observacaoConfirmacaoPrenhes.set(observacaoConfirmacaoPrenhes);
	}
	
	public StringProperty observacaoConfirmacaoPrenhesProperty(){
		return observacaoConfirmacaoPrenhes;
	}
	
	public String getReprodutor(){
		if ( this.getTouroMontaNatural() != null )
			return this.getTouroMontaNatural().getNumeroNome();
		
		if ( this.getTouroInseminacaoArtificial() != null )
			return this.getTouroInseminacaoArtificial().toString();
		
		return null;
	}
	
	public Date getDataParto(){
		if ( getParto() != null ){
			return getParto().getData();	
		}
		return null;
	}
	@Override
	public String toString() {
		return " FÊMEA: " + getFemea().getNumeroNome() + " DATA: " + DateUtil.format(getData()) + 
				" PREVISÃO PARTO: " + DateUtil.format(DateUtil.asLocalDate(getData()).plusDays(282)); 
	}
	
}