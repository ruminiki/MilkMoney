package br.com.milkmoney.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.com.milkmoney.components.FieldRequired;
import br.com.milkmoney.util.DateUtil;
import br.com.milkmoney.util.NumberFormatUtil;


/**
 * The persistent class for the ANIMAL database table.
 * 
 */
@Entity
@Table(name="compra")
@NamedQuery(name="Compra.findAll", query="SELECT a FROM Compra a")
public class Compra extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private ObjectProperty<LocalDate>  data           = new SimpleObjectProperty<LocalDate>(LocalDate.now());
	private ObjectProperty<LocalDate>  dataVencimento = new SimpleObjectProperty<LocalDate>(LocalDate.now());
	private StringProperty             observacao     = new SimpleStringProperty();
	private ObjectProperty<Fornecedor> fornecedor     = new SimpleObjectProperty<Fornecedor>();
	private StringProperty             notaFiscal     = new SimpleStringProperty();
	private StringProperty             formaPagamento = new SimpleStringProperty();
	private List<ItemCompra>           itensCompra    = new ArrayList<ItemCompra>();

	public Compra() {
		// TODO Auto-generated constructor stub
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
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="fornecedor")
	public Fornecedor getFornecedor() {
		return fornecedor.get();
	}
	
	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor.set(fornecedor);
	}
	
	public ObjectProperty<Fornecedor> fornecedorProperty(){
		return fornecedor;
	}
	
	@Access(AccessType.PROPERTY)
	public String getNotaFiscal() {
		return this.notaFiscal.get();
	}

	public void setNotaFiscal(String notaFiscal) {
		this.notaFiscal.set(notaFiscal);
	}
	
	public StringProperty notaFiscalProperty(){
		return notaFiscal;
	}
	
	@Access(AccessType.PROPERTY)
	public String getFormaPagamento() {
		return this.formaPagamento.get();
	}

	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento.set(formaPagamento);
	}
	
	public StringProperty formaPagamentoProperty(){
		return formaPagamento;
	}
	
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	@Access(AccessType.PROPERTY)
	@OneToMany(orphanRemoval=true, targetEntity=ItemCompra.class, cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	@JoinColumn(name="compra")
	public List<ItemCompra> getItensCompra() {
		if ( itensCompra == null )
			return new ArrayList<ItemCompra>();
		return itensCompra;
	}
	
	public void setItensCompra(List<ItemCompra> itensCompra) {
		this.itensCompra = itensCompra;
	}
	
	@Transient
	public BigDecimal getValorTotal(){
		BigDecimal valorTotal = BigDecimal.ZERO;
		for ( ItemCompra ic : itensCompra ){
			valorTotal = valorTotal.add(ic.getValor());
		}
		return valorTotal;
	}
	
	@Override
	public String toString() {
		return getFornecedor().toString() + " - " + DateUtil.format(getData()) + "-" + NumberFormatUtil.decimalFormat(getValorTotal());
	}
	
}