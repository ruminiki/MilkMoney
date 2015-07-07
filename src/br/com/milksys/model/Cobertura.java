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
	private ObjectProperty<LocalDate> previsaoSecagem = new SimpleObjectProperty<LocalDate>(LocalDate.now().plusMonths(7));  
	private ObjectProperty<LocalDate> dataPrimeiroToque = new SimpleObjectProperty<LocalDate>();  
	private StringProperty situacaoPrimeiroToque = new SimpleStringProperty();
	private StringProperty observacaoPrimeiroToque = new SimpleStringProperty();
	private ObjectProperty<LocalDate> dataReconfirmacao = new SimpleObjectProperty<LocalDate>();  
	private StringProperty situacaoReconfirmacao = new SimpleStringProperty();
	private StringProperty observacaoReconfirmacao = new SimpleStringProperty();
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
	@FieldRequired(message="previsão do parto")
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
	@FieldRequired(message="previsão de secagem")
	public Date getPrevisaoSecagem() {
		return DateUtil.asDate(this.previsaoSecagem.get());
	}
	
	public void setPrevisaoSecagem(Date previsaoSecagem) {
		this.previsaoSecagem.set(DateUtil.asLocalDate(previsaoSecagem));
	}
	
	public ObjectProperty<LocalDate> previsaoSecagemProperty(){
		return previsaoSecagem;
	}
	
	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	public Date getDataPrimeiroToque() {
		return DateUtil.asDate(this.dataPrimeiroToque.get());
	}
	
	public void setDataPrimeiroToque(Date dataPrimeiroToque) {
		this.dataPrimeiroToque.set(DateUtil.asLocalDate(dataPrimeiroToque));
	}
	
	public ObjectProperty<LocalDate> dataPrimeiroToqueProperty(){
		return dataPrimeiroToque;
	}
	
	@Access(AccessType.PROPERTY)
	public String getSituacaoPrimeiroToque() {
		return this.situacaoPrimeiroToque.get();
	}

	public void setSituacaoPrimeiroToque(String situacaoPrimeiroToque) {
		this.situacaoPrimeiroToque.set(situacaoPrimeiroToque);
	}

	public StringProperty situacaoPrimeiroToqueToqueProperty(){
		return situacaoPrimeiroToque;
	}
	
	@Access(AccessType.PROPERTY)
	public String getObservacaoPrimeiroToque() {
		return this.observacaoPrimeiroToque.get();
	}

	public void setObservacaoPrimeiroToque(String observacaoPrimeiroToque) {
		this.situacaoPrimeiroToque.set(observacaoPrimeiroToque);
	}

	public StringProperty observacaoPrimeiroToqueProperty(){
		return observacaoPrimeiroToque;
	}
	
	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	public Date getDataReconfirmacao() {
		return DateUtil.asDate(this.dataReconfirmacao.get());
	}
	
	public void setDataReconfirmacao(Date dataReconfirmacao) {
		this.dataReconfirmacao.set(DateUtil.asLocalDate(dataReconfirmacao));
	}
	
	public ObjectProperty<LocalDate> dataReconfirmacaoProperty(){
		return dataReconfirmacao;
	}
	
	@Access(AccessType.PROPERTY)
	public String getSituacaoReconfirmacao() {
		return this.situacaoReconfirmacao.get();
	}

	public void setSituacaoReconfirmacao(String situacaoReconfirmacao) {
		this.situacaoReconfirmacao.set(situacaoReconfirmacao);
	}

	public StringProperty situacaoReconfirmacaoProperty(){
		return situacaoReconfirmacao;
	}
	
	@Access(AccessType.PROPERTY)
	public String getObservacaoReconfirmacao() {
		return this.observacaoReconfirmacao.get();
	}

	public void setObservacaoReconfirmacao(String observacaoReconfirmacao) {
		this.observacaoReconfirmacao.set(observacaoReconfirmacao);
	}

	public StringProperty observacaoReconfirmacaoProperty(){
		return observacaoReconfirmacao;
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
	@OneToOne(targetEntity=Parto.class, cascade=CascadeType.ALL)
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
			return this.getSemen().getTouro();
		
		return null;
	}
	
	public String getPrimeiroToque(){
		if ( getSituacaoPrimeiroToque() == null || getSituacaoPrimeiroToque().isEmpty() )
			return "--";
		return DateUtil.format(getDataPrimeiroToque()) + " - " + getSituacaoPrimeiroToque();
	}
	
	public String getReconfirmacao(){
		if ( getSituacaoReconfirmacao() == null || getSituacaoReconfirmacao().isEmpty() )
			return "--";
		return DateUtil.format(getDataReconfirmacao()) + " - " + getSituacaoReconfirmacao();
	}
	
	@Override
	public String toString() {
		return " FÊMEA: " + getFemea().getNumeroNome() + " DATA: " + DateUtil.format(getData()) + " PREVISÃO PARTO: " + DateUtil.format(getPrevisaoParto()); 
	}
	
}