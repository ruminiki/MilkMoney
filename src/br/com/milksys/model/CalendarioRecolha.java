package br.com.milksys.model;

import java.io.Serializable;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the calendariorecolha database table.
 * 
 */
@Entity
@Table(name="calendariorecolha")
@NamedQuery(name="CalendarioRecolha.findAll", query="SELECT c FROM CalendarioRecolha c")
public class CalendarioRecolha implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private BooleanProperty calendarioAtual;

	private IntegerProperty dataFim;

	private IntegerProperty dataInicio;

	private StringProperty descricao;

	public CalendarioRecolha() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean getCalendarioAtual() {
		return this.calendarioAtual.get();
	}

	public void setCalendarioAtual(boolean calendarioAtual) {
		this.calendarioAtual.set(calendarioAtual);
	}
	
	public BooleanProperty calendarioAtualProperty(){
		return this.calendarioAtual;
	}

	public int getDataFim() {
		return this.dataFim.get();
	}

	public void setDataFim(int dataFim) {
		this.dataFim.set(dataFim);
	}
	
	public IntegerProperty dataFimProperty(){
		return this.dataFim;
	}

	public int getDataInicio() {
		return this.dataInicio.get();
	}

	public void setDataInicio(int dataInicio) {
		this.dataInicio.set(dataInicio);
	}

	public IntegerProperty dataInicioProperty(){
		return this.dataInicio;
	}

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