package br.com.milkmoney.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.Access;
import javax.persistence.AccessType;
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

@Entity
@Table(name="confirmacaoPrenhez")
@NamedQuery(name="ConfirmacaoPrenhez.findAll", query="SELECT c FROM ConfirmacaoPrenhez c")
public class ConfirmacaoPrenhez extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private ObjectProperty<LocalDate> data              = new SimpleObjectProperty<LocalDate>(LocalDate.now());  
	private StringProperty            situacaoCobertura = new SimpleStringProperty();
	private StringProperty            metodoConfirmacao = new SimpleStringProperty();
	private StringProperty            observacao        = new SimpleStringProperty();
	private ObjectProperty<Cobertura> cobertura         = new SimpleObjectProperty<Cobertura>();
	
	public ConfirmacaoPrenhez() {}
	
	public ConfirmacaoPrenhez(Cobertura cobertura) {
		this.setCobertura(cobertura);
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
	@FieldRequired(message="método confirmação")
	public String getMetodoConfirmacao() {
		return this.metodoConfirmacao.get();
	}

	public void setMetodoConfirmacao(String metodoConfirmacao) {
		this.metodoConfirmacao.set(metodoConfirmacao);
	}

	public StringProperty metodoConfirmacaoProperty(){
		return metodoConfirmacao;
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
	@ManyToOne(targetEntity=Cobertura.class)
	@JoinColumn(name="cobertura")
	@FieldRequired(message="cobertura")
	public Cobertura getCobertura() {
		return cobertura.get();
	}
	
	public void setCobertura(Cobertura cobertura) {
		this.cobertura.set(cobertura);
	}
	
	public ObjectProperty<Cobertura> coberturaProperty(){
		return cobertura;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return DateUtil.format(getData()) + " - " + getSituacaoCobertura();
	}

}