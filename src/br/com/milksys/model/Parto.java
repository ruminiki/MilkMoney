package br.com.milksys.model;

import java.io.Serializable;
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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.milksys.components.FieldRequired;
import br.com.milksys.util.DateUtil;


/**
 * The persistent class for the ANIMAL database table.
 * 
 */
@Entity
@Table(name="parto")
@NamedQuery(name="Parto.findAll", query="SELECT a FROM Parto a")
public class Parto extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private ObjectProperty<LocalDate> data = new SimpleObjectProperty<LocalDate>(LocalDate.now());  
	private StringProperty observacao = new SimpleStringProperty();
	private StringProperty tipoParto = new SimpleStringProperty(TipoParto.PARTO_NORMAL);
	private StringProperty complicacaoParto = new SimpleStringProperty(ComplicacaoParto.NENHUMA);
	private ObjectProperty<Cobertura> cobertura = new SimpleObjectProperty<Cobertura>();
	private List<Cria> crias = new ArrayList<Cria>();
	
	public Parto() {
	}
	
	public Parto(Cobertura cobertura) {
		this.setCobertura(cobertura);
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="tipo do parto")
	public String getTipoParto() {
		return this.tipoParto.get();
	}

	public void setTipoParto(String tipoParto) {
		this.tipoParto.set(tipoParto);
	}

	public StringProperty tipoPartoProperty(){
		return tipoParto;
	}
	
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="complicação do parto")
	public String getComplicacaoParto() {
		return this.complicacaoParto.get();
	}

	public void setComplicacaoParto(String complicacaoParto) {
		this.complicacaoParto.set(complicacaoParto);
	}

	public StringProperty complicacaoPartoProperty(){
		return complicacaoParto;
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
	
	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	public Date getData() {
		return DateUtil.asDate(data.get());
	}

	public void setData(Date data) {
		this.data.set(DateUtil.asLocalDate(data));
	}
	
	public ObjectProperty<LocalDate> dataProperty(){
		return data;
	}
	
	@Access(AccessType.PROPERTY)
	@OneToOne(targetEntity=Cobertura.class, cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, optional=false)
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
	
	@Access(AccessType.PROPERTY)
	@OneToMany(orphanRemoval=true,  targetEntity=Cria.class, cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	@JoinColumn(name="parto")
	public List<Cria> getCrias() {
		if ( crias == null )
			return new ArrayList<Cria>();
		return crias;
	}
	
	public void setCrias(List<Cria> crias) {
		this.crias = crias;
	}
	
	@Override
	public String toString() {
		return "PARTO " + getCobertura().getFemea().getNumeroNome() + " - " +  DateUtil.format(getData());
	}

}