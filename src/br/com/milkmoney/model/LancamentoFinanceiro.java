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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
	private ObjectProperty<LocalDate>                     dataPagamento         = new SimpleObjectProperty<LocalDate>(LocalDate.now());
	private StringProperty                                tipoLancamento        = new SimpleStringProperty(TipoLancamentoFinanceiro.DESPESA);
	private ObjectProperty<CentroCusto>                   centroCusto           = new SimpleObjectProperty<CentroCusto>();
	private ObjectProperty<CategoriaLancamentoFinanceiro> categoria             = new SimpleObjectProperty<CategoriaLancamentoFinanceiro>();
	private StringProperty                                valor                 = new SimpleStringProperty();
	private StringProperty                                juros                 = new SimpleStringProperty();
	private StringProperty                                multa                 = new SimpleStringProperty();
	private StringProperty                                observacao            = new SimpleStringProperty(Sexo.FEMEA);
	
	public LancamentoFinanceiro() {
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="data emissão")
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
	@FieldRequired(message="data pagamento")
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
	public String getObservacao() {
		return this.observacao.get();
	}

	public void setObservacao(String descricao) {
		this.observacao.set(descricao);
	}

	public StringProperty observacaoProperty(){
		return observacao;
	}

	@Access(AccessType.PROPERTY)
	@ManyToOne(cascade=CascadeType.REFRESH)
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
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="categoria")
	@FieldRequired(message="categoria do lançamento")
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
	
	
}