package br.com.milkmoney.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import javax.persistence.OneToMany;
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
	private StringProperty              tipoCobertura                = new SimpleStringProperty(TipoCobertura.ENSEMINACAO_ARTIFICIAL);
	private ObjectProperty<Animal>      femea                        = new SimpleObjectProperty<Animal>();
	private ObjectProperty<Animal>      touro                        = new SimpleObjectProperty<Animal>();
	private ObjectProperty<Semen>       semen                        = new SimpleObjectProperty<Semen>();
	private StringProperty              quantidadeDosesUtilizadas    = new SimpleStringProperty();
	private StringProperty              nomeResponsavel              = new SimpleStringProperty();
	private ObjectProperty<Funcionario> funcionarioResponsavel       = new SimpleObjectProperty<Funcionario>();
	private ObjectProperty<Servico>     servico                      = new SimpleObjectProperty<Servico>();
	private StringProperty              observacao                   = new SimpleStringProperty();
	//jpa
	private Parto                       parto;
	private List<ConfirmacaoPrenhes>    confirmacoesPrenhes;
	private String                      situacaoCobertura            = SituacaoCobertura.INDEFINIDA;
	
	public Cobertura() {
	}
	
	public Cobertura(Animal femea) {
		setFemea(femea);
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
	@JoinColumn(name="touro")
	public Animal getTouro() {
		return touro.get();
	}
	
	public void setTouro(Animal touro) {
		this.touro.set(touro);
	}
	
	public ObjectProperty<Animal> touroProperty(){
		return touro;
	}
	
	public String getSituacaoCobertura() {
		if ( this.getParto() != null ){
			return SituacaoCobertura.PARIDA; 
		}
		
		return situacaoCobertura == null ? SituacaoCobertura.INDEFINIDA : situacaoCobertura;
	}

	public void setSituacaoCobertura(String situacaoCobertura) {
		this.situacaoCobertura = situacaoCobertura;
	}

	@Access(AccessType.PROPERTY)
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="semen")
	public Semen getSemen() {
		return semen.get();
	}
	
	public void setSemen(Semen semen) {
		this.semen.set(semen);
	}
	
	public ObjectProperty<Semen> semenProperty(){
		return semen;
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
	
	@Access(AccessType.PROPERTY)
	@OneToMany(orphanRemoval=true, targetEntity=ConfirmacaoPrenhes.class, cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	@JoinColumn(name="cobertura")
	public List<ConfirmacaoPrenhes> getConfirmacoesPrenhes() {
		return confirmacoesPrenhes == null ? new ArrayList<ConfirmacaoPrenhes>() : confirmacoesPrenhes;
	}

	public void setConfirmacoesPrenhes(List<ConfirmacaoPrenhes> confirmacoesPrenhes) {
		this.confirmacoesPrenhes = confirmacoesPrenhes;
	}

	public String getReprodutor(){
		if ( this.getTouro() != null )
			return this.getTouro().getNumeroNome();
		
		if ( this.getSemen() != null )
			return this.getSemen().getTouro().toString();
		
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