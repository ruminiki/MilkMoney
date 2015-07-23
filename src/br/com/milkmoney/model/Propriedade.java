package br.com.milkmoney.model;

import java.io.Serializable;
import java.math.BigDecimal;

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

import br.com.milkmoney.components.FieldRequired;
import br.com.milkmoney.util.NumberFormatUtil;


/**
 * The persistent class for the RACA database table.
 * 
 */
@Entity
@Table(name="propriedade")
@NamedQuery(name="Propriedade.findAll", query="SELECT r FROM Propriedade r")
public class Propriedade extends AbstractEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private StringProperty descricao = new SimpleStringProperty();
	private StringProperty endereco  = new SimpleStringProperty();
	private StringProperty area      = new SimpleStringProperty();
	
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
	@FieldRequired(message="endereço")
	public String getEndereco() {
		return this.endereco.get();
	}

	public void setEndereco(String endereco) {
		this.endereco.set(endereco);
	}
	
	public StringProperty enderecoProperty(){
		return endereco;
	}
	
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="área")
	public BigDecimal getArea() {
		return NumberFormatUtil.fromString(this.area.get());
	}

	public void setArea(BigDecimal area) {
		this.area.set(NumberFormatUtil.decimalFormat(area));
	}
	
	public StringProperty areaProperty(){
		return area;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return getDescricao();
	}
	
}