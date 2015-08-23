package br.com.milkmoney.model;

import java.io.Serializable;
import java.math.BigDecimal;

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
import javax.persistence.Transient;

import br.com.milkmoney.components.FieldRequired;
import br.com.milkmoney.util.NumberFormatUtil;


/**
 * The persistent class for the ANIMAL database table.
 * 
 */
@Entity
@Table(name="itemCompra")
@NamedQuery(name="ItemCompra.findAll", query="SELECT a FROM ItemCompra a")
public class ItemCompra extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private ObjectProperty<Insumo>     insumo         = new SimpleObjectProperty<Insumo>();
	private ObjectProperty<Compra>     compra         = new SimpleObjectProperty<Compra>();
	private StringProperty             quantidade     = new SimpleStringProperty();
	private StringProperty             valor          = new SimpleStringProperty();
	private StringProperty             observacao     = new SimpleStringProperty();

	public ItemCompra() {
		// TODO Auto-generated constructor stub
	}
	
	public ItemCompra(Compra compra) {
		setCompra(compra);
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
	@JoinColumn(name="insumo")
	@FieldRequired(message="insumo")
	public Insumo getInsumo() {
		return insumo.get();
	}
	
	public void setInsumo(Insumo insumo) {
		this.insumo.set(insumo);
	}
	
	public ObjectProperty<Insumo> insumoProperty(){
		return insumo;
	}
	
	@Access(AccessType.PROPERTY)
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="compra")
	@FieldRequired(message="compra")
	public Compra getCompra() {
		return compra.get();
	}
	
	public void setCompra(Compra compra) {
		this.compra.set(compra);
	}
	
	public ObjectProperty<Compra> compraProperty(){
		return compra;
	}
	
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="valor")
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
	@FieldRequired(message="quantidade")
	public BigDecimal getQuantidade() {
		return NumberFormatUtil.fromString(this.quantidade.get());
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade.set(NumberFormatUtil.decimalFormat(quantidade));
	}
	
	public StringProperty quantidadeProperty(){
		return quantidade;
	}
	
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Transient
	public BigDecimal getValorItem(){
		return getQuantidade().multiply(getValor());
	}
	
	@Override
	public String toString() {
		return getInsumo().toString() + " - " + NumberFormatUtil.decimalFormat(getValor());
	}
	
}