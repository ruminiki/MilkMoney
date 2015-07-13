package br.com.milksys.model;

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

import br.com.milksys.components.FieldRequired;
import br.com.milksys.util.DateUtil;


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
	
	private StringProperty observacao = new SimpleStringProperty();
	private ObjectProperty<LocalDate> data = new SimpleObjectProperty<LocalDate>(LocalDate.now());  
	private ObjectProperty<LocalDate> previsaoParto = new SimpleObjectProperty<LocalDate>(LocalDate.now().plusMonths(9));  
	private ObjectProperty<LocalDate> previsaoEncerramentoLactacao = new SimpleObjectProperty<LocalDate>(LocalDate.now().plusMonths(7));  
	private ObjectProperty<LocalDate> dataConfirmacaoPrenhez = new SimpleObjectProperty<LocalDate>();  
	private StringProperty situacaoConfirmacaoPrenhez = new SimpleStringProperty();
	private StringProperty observacaoConfirmacaoPrenhez = new SimpleStringProperty();
	private ObjectProperty<LocalDate> dataReconfirmacaoPrenhez = new SimpleObjectProperty<LocalDate>();  
	private StringProperty situacaoReconfirmacaoPrenhez = new SimpleStringProperty();
	private StringProperty observacaoReconfirmacaoPrenhez = new SimpleStringProperty();
	private ObjectProperty<LocalDate> dataRepeticaoCio = new SimpleObjectProperty<LocalDate>();  
	private StringProperty observacaoRepeticaoCio = new SimpleStringProperty();
	private StringProperty tipoCobertura = new SimpleStringProperty(TipoCobertura.ENSEMINACAO_ARTIFICIAL);
	private ObjectProperty<Animal> femea = new SimpleObjectProperty<Animal>();
	private ObjectProperty<Animal> touro = new SimpleObjectProperty<Animal>();
	private StringProperty situacaoCobertura = new SimpleStringProperty();
	private ObjectProperty<Semen> semen = new SimpleObjectProperty<Semen>();
	private StringProperty quantidadeDosesUtilizadas = new SimpleStringProperty();
	private StringProperty nomeResponsavel = new SimpleStringProperty();
	private ObjectProperty<Funcionario> funcionarioResponsavel = new SimpleObjectProperty<Funcionario>();
	private ObjectProperty<Servico> servico = new SimpleObjectProperty<Servico>();
	private Parto parto;
	
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
	
	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	public Date getPrevisaoEncerramentoLactacao() {
		return DateUtil.asDate(this.previsaoEncerramentoLactacao.get());
	}
	
	public void setPrevisaoEncerramentoLactacao(Date previsaoEncerramentoLactacao) {
		this.previsaoEncerramentoLactacao.set(DateUtil.asLocalDate(previsaoEncerramentoLactacao));
	}
	
	public ObjectProperty<LocalDate> previsaoEncerramentoLactacaoProperty(){
		return previsaoEncerramentoLactacao;
	}
	
	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	public Date getDataConfirmacaoPrenhez() {
		return DateUtil.asDate(this.dataConfirmacaoPrenhez.get());
	}
	
	public void setDataConfirmacaoPrenhez(Date dataConfirmacaoPrenhez) {
		this.dataConfirmacaoPrenhez.set(DateUtil.asLocalDate(dataConfirmacaoPrenhez));
	}
	
	public ObjectProperty<LocalDate> dataConfirmacaoPrenhezProperty(){
		return dataConfirmacaoPrenhez;
	}
	
	@Access(AccessType.PROPERTY)
	public String getSituacaoConfirmacaoPrenhez() {
		return this.situacaoConfirmacaoPrenhez.get();
	}

	public void setSituacaoConfirmacaoPrenhez(String situacaoConfirmacaoPrenhez) {
		this.situacaoConfirmacaoPrenhez.set(situacaoConfirmacaoPrenhez);
	}

	public StringProperty situacaoConfirmacaoPrenhezToqueProperty(){
		return situacaoConfirmacaoPrenhez;
	}
	
	@Access(AccessType.PROPERTY)
	public String getObservacaoConfirmacaoPrenhez() {
		return this.observacaoConfirmacaoPrenhez.get();
	}

	public void setObservacaoConfirmacaoPrenhez(String observacaoConfirmacaoPrenhez) {
		this.situacaoConfirmacaoPrenhez.set(observacaoConfirmacaoPrenhez);
	}

	public StringProperty observacaoConfirmacaoPrenhezProperty(){
		return observacaoConfirmacaoPrenhez;
	}
	
	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	public Date getDataReconfirmacaoPrenhez() {
		return DateUtil.asDate(this.dataReconfirmacaoPrenhez.get());
	}
	
	public void setDataReconfirmacaoPrenhez(Date dataReconfirmacaoPrenhez) {
		this.dataReconfirmacaoPrenhez.set(DateUtil.asLocalDate(dataReconfirmacaoPrenhez));
	}
	
	public ObjectProperty<LocalDate> dataReconfirmacaoPrenhezProperty(){
		return dataReconfirmacaoPrenhez;
	}
	
	@Access(AccessType.PROPERTY)
	public String getSituacaoReconfirmacaoPrenhez() {
		return this.situacaoReconfirmacaoPrenhez.get();
	}

	public void setSituacaoReconfirmacaoPrenhez(String situacaoReconfirmacaoPrenhez) {
		this.situacaoReconfirmacaoPrenhez.set(situacaoReconfirmacaoPrenhez);
	}

	public StringProperty situacaoReconfirmacaoPrenhezProperty(){
		return situacaoReconfirmacaoPrenhez;
	}
	
	@Access(AccessType.PROPERTY)
	public String getObservacaoReconfirmacaoPrenhez() {
		return this.observacaoReconfirmacaoPrenhez.get();
	}

	public void setObservacaoReconfirmacaoPrenhez(String observacaoReconfirmacaoPrenhez) {
		this.observacaoReconfirmacaoPrenhez.set(observacaoReconfirmacaoPrenhez);
	}

	public StringProperty observacaoReconfirmacaoPrenhezProperty(){
		return observacaoReconfirmacaoPrenhez;
	}
	
	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	public Date getDataRepeticaoCio() {
		return DateUtil.asDate(this.dataRepeticaoCio.get());
	}
	
	public void setDataRepeticaoCio(Date dataRepeticaoCio) {
		this.dataRepeticaoCio.set(DateUtil.asLocalDate(dataRepeticaoCio));
	}
	
	public ObjectProperty<LocalDate> dataRepeticaoCioProperty(){
		return dataRepeticaoCio;
	}
	
	@Access(AccessType.PROPERTY)
	public String getObservacaoRepeticaoCio() {
		return this.observacaoRepeticaoCio.get();
	}

	public void setObservacaoRepeticaoCio(String observacaoRepeticaoCio) {
		this.observacaoRepeticaoCio.set(observacaoRepeticaoCio);
	}

	public StringProperty observacaoRepeticaoCioProperty(){
		return observacaoRepeticaoCio;
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
	
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="situação cobertura")
	public String getSituacaoCobertura() {
		return situacaoCobertura.get();
	}
	
	public void setSituacaoCobertura(String situacaoCobertura) {
		this.situacaoCobertura.set(situacaoCobertura);
	}
	
	public StringProperty situacaoCoberturaProperty(){
		return situacaoCobertura;
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
	@ManyToOne(cascade=CascadeType.ALL)
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
	@OneToOne(orphanRemoval=true, targetEntity=Parto.class, cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinColumn(name="parto")
	public Parto getParto() {
		return parto;
	}

	public void setParto(Parto parto) {
		this.parto = parto;
	}

	public String getReprodutor(){
		if ( this.getTouro() != null )
			return this.getTouro().getNumeroNome();
		
		if ( this.getSemen() != null )
			return this.getSemen().getTouro().toString();
		
		return null;
	}
	
	public String getConfirmacaoPrenhez(){
		if ( getSituacaoConfirmacaoPrenhez() == null || getSituacaoConfirmacaoPrenhez().isEmpty() )
			return "Registrar";
		return DateUtil.format(getDataConfirmacaoPrenhez()) + " - " + getSituacaoConfirmacaoPrenhez();
	}
	
	public String getReconfirmacaoPrenhez(){
		if ( getSituacaoReconfirmacaoPrenhez() == null || getSituacaoReconfirmacaoPrenhez().isEmpty() )
			return "Registrar";
		return DateUtil.format(getDataReconfirmacaoPrenhez()) + " - " + getSituacaoReconfirmacaoPrenhez();
	}
	
	public String getRepeticao(){
		if ( getDataRepeticaoCio() == null || !getSituacaoCobertura().equals(SituacaoCobertura.REPETIDA) )
			return "Registrar";
		return DateUtil.format(getDataRepeticaoCio());
	}
	
	@Override
	public String toString() {
		return " FÊMEA: " + getFemea().getNumeroNome() + " DATA: " + DateUtil.format(getData()) + " PREVISÃO PARTO: " + DateUtil.format(getPrevisaoParto()); 
	}
	
}