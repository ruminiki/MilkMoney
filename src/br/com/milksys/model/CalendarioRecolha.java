package br.com.milksys.model;

import java.io.Serializable;

import javafx.beans.property.Property;
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

	public static final String MES_CORRENTE = "CORRENTE";
	public static final String MES_ANTERIOR = "ANTERIOR";
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private Property<String> calendarioVigente = new SimpleStringProperty();
	private Property<String> mesInicio = new SimpleStringProperty();
	private StringProperty diaFim = new SimpleStringProperty();
	private StringProperty diaInicio = new SimpleStringProperty();
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
	public String getCalendarioVigente() {
		return this.calendarioVigente.getValue();
	}

	public void setCalendarioVigente(String calendarioVigente) {
		this.calendarioVigente.setValue(calendarioVigente);
	}
	
	public Property<String> calendarioVigenteProperty(){
		return this.calendarioVigente;
	}
	@Access(AccessType.PROPERTY)
	public int getDiaFim() {
		return Integer.parseInt(this.diaFim.get());
	}

	public void setDiaFim(int diaFim) {
		this.diaFim.setValue(String.valueOf(diaFim));
	}
	
	public StringProperty diaFimProperty(){
		return this.diaFim;
	}
	@Access(AccessType.PROPERTY)
	public int getDiaInicio() {
		return Integer.parseInt(this.diaInicio.get());
	}

	public void setDiaInicio(int dataInicio) {
		this.diaInicio.setValue(String.valueOf(dataInicio));
	}

	public StringProperty diaInicioProperty(){
		return this.diaInicio;
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
	
	@Access(AccessType.PROPERTY)
	public String getMesInicio() {
		return this.mesInicio.getValue();
	}

	public void setMesInicio(String mesInicio) {
		this.mesInicio.setValue(mesInicio);
	}
	
	public Property<String> mesInicioProperty() {
		return mesInicio;
	}

}