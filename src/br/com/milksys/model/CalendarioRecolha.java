package br.com.milksys.model;

import java.io.Serializable;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the calendariorecolha database table.
 * 
 */
@Entity
@Table(name="calendarioRecolha")
@NamedQuery(name="CalendarioRecolha.findAll", query="SELECT c FROM CalendarioRecolha c")
public class CalendarioRecolha implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private Property<String> calendarioAtual = new SimpleStringProperty();
	private Property<Number> dataFim = new SimpleIntegerProperty();
	private Property<Number> dataInicio = new SimpleIntegerProperty();
	private StringProperty descricao = new SimpleStringProperty();

	public CalendarioRecolha() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	@Access(AccessType.PROPERTY)
	public String getCalendarioAtual() {
		return this.calendarioAtual.getValue();
	}

	public void setCalendarioAtual(String calendarioAtual) {
		this.calendarioAtual.setValue(calendarioAtual);
	}
	
	public Property<String> calendarioAtualProperty(){
		return this.calendarioAtual;
	}
	@Access(AccessType.PROPERTY)
	public int getDataFim() {
		return (int) this.dataFim.getValue();
	}

	public void setDataFim(int dataFim) {
		this.dataFim.setValue(dataFim);
	}
	
	public Property<Number> dataFimProperty(){
		return this.dataFim;
	}
	@Access(AccessType.PROPERTY)
	public int getDataInicio() {
		return (int) this.dataInicio.getValue();
	}

	public void setDataInicio(int dataInicio) {
		this.dataInicio.setValue(dataInicio);
	}

	public Property<Number> dataInicioProperty(){
		return this.dataInicio;
	}
	@Access(AccessType.PROPERTY)
	public String getDescricao() {
		return this.descricao.get();
	}

	public void setDescricao(String descricao) {
		this.descricao.set(descricao);
	}
	
	public StringProperty descricaoProperty(){
		return this.descricao;
	}

}