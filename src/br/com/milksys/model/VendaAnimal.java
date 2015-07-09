package br.com.milksys.model;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.com.milksys.components.FieldRequired;
import br.com.milksys.util.DateUtil;
import br.com.milksys.util.NumberFormatUtil;


/**
 * The persistent class for the funcionario database table.
 * 
 */
@Entity
@NamedQuery(name="VendaAnimal.findAll", query="SELECT f FROM VendaAnimal f")
public class VendaAnimal extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private ObjectProperty<LocalDate>         dataVenda               = new SimpleObjectProperty<LocalDate>(LocalDate.now());
	private ObjectProperty<LocalDate>         dataPrevisaoRecebimento = new SimpleObjectProperty<LocalDate>(LocalDate.now());  
	private StringProperty                    valorTotal              = new SimpleStringProperty();
	private ObjectProperty<Comprador>         comprador               = new SimpleObjectProperty<Comprador>();
	private StringProperty                    observacao              = new SimpleStringProperty();
	private List<AnimalVendido>               animaisVendidos         = new ArrayList<AnimalVendido>();

	public VendaAnimal() {
	}
	
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="data da venda")
	public Date getDataVenda() {
		return DateUtil.asDate(this.dataVenda.get());
	}

	public void setDataVenda(Date dataVenda) {
		this.dataVenda.set(DateUtil.asLocalDate(dataVenda));
	}
	
	public ObjectProperty<LocalDate> dataVendaProperty(){
		return dataVenda;
	}
	
	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="data recebimento previsto")
	public Date getDataPrevisaoRecebimento() {
		return DateUtil.asDate(this.dataPrevisaoRecebimento.get());
	}

	public void setDataPrevisaoRecebimento(Date dataPrevisaoRecebimento) {
		this.dataPrevisaoRecebimento.set(DateUtil.asLocalDate(dataPrevisaoRecebimento));
	}
	
	public ObjectProperty<LocalDate> dataRecebimentoProperty(){
		return dataPrevisaoRecebimento;
	}
	
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="valor total da venda")
	public BigDecimal getValorTotal() {
		return NumberFormatUtil.fromString(this.valorTotal.get());
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal.set(NumberFormatUtil.decimalFormat(valorTotal));
	}

	public StringProperty valorTotalProperty(){
		return valorTotal;
	}
	
	@Access(AccessType.PROPERTY)
	@ManyToOne(targetEntity=Comprador.class, cascade=CascadeType.REFRESH)
	@JoinColumn(name="comprador")
	@FieldRequired(message="comprador")
	public Comprador getComprador() {
		return comprador.get();
	}
	
	public void setComprador(Comprador comprador) {
		this.comprador.set(comprador);
	}
	
	public ObjectProperty<Comprador> compradorProperty(){
		return comprador;
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
	@OneToMany(orphanRemoval=true,  targetEntity=AnimalVendido.class, cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinColumn(name="vendaAnimal")
	public List<AnimalVendido> getAnimaisVendidos() {
		return animaisVendidos;
	}

	public void setAnimaisVendidos(List<AnimalVendido> animaisVendidos) {
		this.animaisVendidos = animaisVendidos;
	}
	
	@Transient
	public String getQuantidadeAnimaisVendidos(){
		
		if ( getAnimaisVendidos() != null ){
			return String.valueOf(getAnimaisVendidos().size());
		}
		return "0";
		
	}
	
}