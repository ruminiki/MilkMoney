package br.com.milkmoney.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.FetchType;
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
import javax.persistence.Transient;

import br.com.milkmoney.components.FieldRequired;
import br.com.milkmoney.util.DateUtil;
import br.com.milkmoney.util.NumberFormatUtil;


@Entity
@Table(name="lancamentoFinanceiro")
@NamedQuery(name="LancamentoFinanceiro.findAll", query="SELECT a FROM LancamentoFinanceiro a")
public class LancamentoFinanceiro extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private ObjectProperty<LocalDate>                     dataEmissao           = new SimpleObjectProperty<LocalDate>(LocalDate.now());
	private ObjectProperty<LocalDate>                     dataVencimento        = new SimpleObjectProperty<LocalDate>(LocalDate.now());
	private ObjectProperty<LocalDate>                     dataPagamento         = new SimpleObjectProperty<LocalDate>();
	private StringProperty                                tipoLancamento        = new SimpleStringProperty(TipoLancamentoFinanceiro.DESPESA);
	private ObjectProperty<CentroCusto>                   centroCusto           = new SimpleObjectProperty<CentroCusto>();
	private ObjectProperty<CategoriaLancamentoFinanceiro> categoria             = new SimpleObjectProperty<CategoriaLancamentoFinanceiro>();
	private StringProperty                                valor                 = new SimpleStringProperty();
	private StringProperty                                juros                 = new SimpleStringProperty();
	private StringProperty                                multa                 = new SimpleStringProperty();
	private StringProperty                                descricao             = new SimpleStringProperty();
	private StringProperty                                observacao            = new SimpleStringProperty();
	private StringProperty								  parcela               = new SimpleStringProperty();
	private Servico                                       servico               = null;
	
	public LancamentoFinanceiro() {
	}

	@Override
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="data emiss�o")
	public Date getDataEmissao() {
		return DateUtil.asDate(this.dataEmissao.get());
	}
	
	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao.set(DateUtil.asLocalDate(dataEmissao));
	}
	
	public ObjectProperty<LocalDate> dataEmissaoProperty(){
		return dataEmissao;
	}
	
	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="data vencimento")
	public Date getDataVencimento() {
		return DateUtil.asDate(this.dataVencimento.get());
	}
	
	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento.set(DateUtil.asLocalDate(dataVencimento));
	}
	
	public ObjectProperty<LocalDate> dataVencimentoProperty(){
		return dataVencimento;
	}
	
	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	public Date getDataPagamento() {
		return DateUtil.asDate(this.dataPagamento.get());
	}
	
	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento.set(DateUtil.asLocalDate(dataPagamento));
	}
	
	public ObjectProperty<LocalDate> dataPagamentoProperty(){
		return dataPagamento;
	}
	
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="tipo lan�amento")
	public String getTipoLancamento() {
		return this.tipoLancamento.get();
	}

	public void setTipoLancamento(String tipoLancamento) {
		this.tipoLancamento.set(tipoLancamento);
	}

	public StringProperty tipoLancamentoProperty(){
		return tipoLancamento;
	}
	
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="descri��o")
	public String getDescricao() {
		return this.descricao.get();
	}

	public void setDescricao(String descricao) {
		this.descricao.set(descricao);
	}

	public StringProperty descricaoProperty(){
		return descricao;
	}

	@Access(AccessType.PROPERTY)
	public String getObservacao() {
		return this.observacao.get();
	}

	public void setObservacao(String observacao) {
		this.observacao.set(observacao);
	}

	public StringProperty observacaoProperty(){
		return observacao;
	}

	@Access(AccessType.PROPERTY)
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.REFRESH)
	@JoinColumn(name="centroCusto")
	@FieldRequired(message="centro de custo")
	public CentroCusto getCentroCusto() {
		return centroCusto.get();
	}
	
	public void setCentroCusto(CentroCusto centroCusto) {
		this.centroCusto.set(centroCusto);
	}
	
	public ObjectProperty<CentroCusto> centroCustoProperty(){
		return centroCusto;
	}
	
	@Access(AccessType.PROPERTY)
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.REFRESH)
	@JoinColumn(name="categoria")
	@FieldRequired(message="categoria do lan�amento")
	public CategoriaLancamentoFinanceiro getCategoria() {
		return categoria.get();
	}
	
	public void setCategoria(CategoriaLancamentoFinanceiro categoria) {
		this.categoria.set(categoria);
	}
	
	public ObjectProperty<CategoriaLancamentoFinanceiro> categoriaProperty(){
		return categoria;
	}
	
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="valor do lan�amento")
	public BigDecimal getValor() {
		return NumberFormatUtil.fromString(this.valor.get());
	}

	public void setValor(BigDecimal valor) {
		this.valor.set(NumberFormatUtil.decimalFormat(valor));
	}
	
	public StringProperty valorProperty(){
		return valor;
	}
	
	@Access(AccessType.PROPERTY)
	public BigDecimal getJuros() {
		return NumberFormatUtil.fromString(this.juros.get());
	}

	public void setJuros(BigDecimal juros) {
		this.juros.set(NumberFormatUtil.decimalFormat(juros));
	}
	
	public StringProperty jurosProperty(){
		return juros;
	}
	
	@Access(AccessType.PROPERTY)
	public BigDecimal getMulta() {
		return NumberFormatUtil.fromString(this.multa.get());
	}

	public void setMulta(BigDecimal multa) {
		this.multa.set(NumberFormatUtil.decimalFormat(multa));
	}
	
	public StringProperty multaProperty(){
		return multa;
	}
	
	@Access(AccessType.PROPERTY)
	public String getParcela() {
		return this.parcela.get();
	}

	public void setParcela(String parcela) {
		this.parcela.set(parcela);
	}

	public StringProperty parcelaProperty(){
		return parcela;
	}
	
	@Access(AccessType.PROPERTY)
	@OneToOne(fetch = FetchType.LAZY, targetEntity=Servico.class, cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = false)
	@JoinColumn(name="servico")
	public Servico getServico() {
		return servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}

	//--------------
	@Transient
	public String getTipoLancamentoFormatado(){
		if ( getTipoLancamento().equals(TipoLancamentoFinanceiro.RECEITA) ){
			return "C";
		}
		return "D";
	}
	
	
	@Transient
	public BigDecimal getValorTotal(){
		return getValor().add(getJuros()).add(getMulta());
	}
	
	@Transient
	public String getValorTotalFormatado(){
		return "R$ " + NumberFormatUtil.decimalFormat( getValor().add(getJuros()).add(getMulta()));
	}
	
}