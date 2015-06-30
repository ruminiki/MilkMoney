package br.com.milksys.model;

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

import br.com.milksys.components.FieldRequired;
import br.com.milksys.util.DateUtil;
import br.com.milksys.util.NumberFormatUtil;


/**
 * The persistent class for the ANIMAL database table.
 * 
 */
@Entity
@Table(name="servico")
@NamedQuery(name="Servico.findAll", query="SELECT a FROM Servico a")
public class Servico extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private ObjectProperty<LocalDate> data = new SimpleObjectProperty<LocalDate>(LocalDate.now());  
	private StringProperty descricao = new SimpleStringProperty();
	private StringProperty historico = new SimpleStringProperty();
	private StringProperty valor = new SimpleStringProperty();
	private ObjectProperty<PrestadorServico> prestadorServico = new SimpleObjectProperty<PrestadorServico>();
	//private ObjectProperty<Despesa> despesa = new SimpleObjectProperty<Despesa>();

	public Servico() {
		// TODO Auto-generated constructor stub
	}
	
	public Servico(String descricao) {
		this.descricao.set(descricao);
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

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="descrição")
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
	public String getHistorico() {
		return this.historico.get();
	}

	public void setHistorico(String historico) {
		this.historico.set(historico);
	}

	public StringProperty historicooProperty(){
		return historico;
	}
	
	@Access(AccessType.PROPERTY)
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="prestadorServico")
	@FieldRequired(message="prestador de serviço")
	public PrestadorServico getPrestadorServico() {
		return prestadorServico.get();
	}
	
	public void setPrestadorServico(PrestadorServico prestadorServico) {
		this.prestadorServico.set(prestadorServico);
	}
	
	public ObjectProperty<PrestadorServico> prestadorServicoProperty(){
		return prestadorServico;
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
	
}