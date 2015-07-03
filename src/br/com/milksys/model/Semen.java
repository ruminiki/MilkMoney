package br.com.milksys.model;

import java.beans.Transient;
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

import org.hibernate.annotations.Formula;

import br.com.milksys.util.DateUtil;
import br.com.milksys.util.NumberFormatUtil;


/**
 * The persistent class for the semen database table.
 * 
 */
@Entity
@Table(name="semen")
@NamedQuery(name="Semen.findAll", query="SELECT a FROM Semen a")
public class Semen extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private StringProperty descricao = new SimpleStringProperty();
	private StringProperty touro = new SimpleStringProperty();
	private ObjectProperty<LocalDate> dataCompra = new SimpleObjectProperty<LocalDate>(LocalDate.now());  
	private StringProperty valorUnitario = new SimpleStringProperty();
	private StringProperty quantidade = new SimpleStringProperty();
	private StringProperty lote = new SimpleStringProperty();
	private ObjectProperty<Fornecedor> fornecedor = new SimpleObjectProperty<Fornecedor>();
	
	@Formula("(quantidade - (select sum(c.quantidadeDosesUtilizadas) from Cobertura c where c.semen = id))")
	private BigDecimal quantidadeDisponivel;
	
	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	public Date getDataCompra() {
		return DateUtil.asDate(this.dataCompra.get());
	}

	public void setDataCompra(Date dataNascimento) {
		this.dataCompra.set(DateUtil.asLocalDate(dataNascimento));
	}
	
	public ObjectProperty<LocalDate> dataCompraProperty(){
		return dataCompra;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Access(AccessType.PROPERTY)
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
	public String getTouro() {
		return this.touro.get();
	}

	public void setTouro(String touro) {
		this.touro.set(touro);
	}

	public StringProperty touroProperty(){
		return touro;
	}
	
	@Access(AccessType.PROPERTY)
	public BigDecimal getValorUnitario() {
		return NumberFormatUtil.fromString(this.valorUnitario.get());
	}

	public void setValorUnitario(BigDecimal numero) {
		this.valorUnitario.set(NumberFormatUtil.decimalFormat(numero));
	}
	
	public StringProperty valorUnitarioProperty(){
		return valorUnitario;
	}

	@Transient
	public BigDecimal getValorTotal() {
		return getValorUnitario().multiply(new BigDecimal(getQuantidade()));
	}
	
	@Access(AccessType.PROPERTY)
	public int getQuantidade() {
		return NumberFormatUtil.fromString(this.quantidade.get()).intValue();
	}

	public void setQuantidade(int quantidade) {
		this.quantidade.set(String.valueOf(quantidade));
	}
	
	public StringProperty quantidadeProperty(){
		return quantidade;
	}

	@Access(AccessType.PROPERTY)
	public String getLote() {
		return this.lote.get();
	}

	public void setLote(String lote) {
		this.lote.set(lote);
	}

	public StringProperty loteProperty(){
		return lote;
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
	
	
	@Transient
	public BigDecimal getQuantidadeDisponivel() {
		return quantidadeDisponivel;
	}

	public void setQuantidadeDisponivel(BigDecimal quantidadeDisponivel) {
		this.quantidadeDisponivel = quantidadeDisponivel;
	}

	@Override
	public String toString() {
		return this.getTouro();
	}
	/*@Override
	public boolean isValid() {
		
		if ( !super.isValid() )
			return false;
		
		if ( getQuantidade() <= 0 ){
			CustomAlert.mensagemAlerta("Por favor informe uma quantidade maior que zero para continuar.");
		}
		
		return true;
	}*/
	
}