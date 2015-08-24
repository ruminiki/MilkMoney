package br.com.milkmoney.model;

import java.io.Serializable;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.milkmoney.components.FieldRequired;


@Entity
@Table(name="categoriaDespesa")
@NamedQuery(name="CategoriaDespesa.findAll", query="SELECT r FROM CategoriaDespesa r")
public class CategoriaDespesa extends AbstractEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private StringProperty                         descricao          = new SimpleStringProperty();
	private SimpleObjectProperty<CategoriaDespesa> categoriaSuperiora = new SimpleObjectProperty<CategoriaDespesa>();
	
	public CategoriaDespesa() {
		// TODO Auto-generated constructor stub
	}
	
	public CategoriaDespesa(String descricao) {
		setDescricao(descricao);
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="descri��o")
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
	@OneToOne(targetEntity=CategoriaDespesa.class, cascade=CascadeType.REFRESH)
	@JoinColumn(name="categoriaSuperiora")
	public CategoriaDespesa getCategoriaSuperiora() {
		return categoriaSuperiora.get();
	}
	
	public void setCategoriaSuperiora(CategoriaDespesa categoriaSuperiora) {
		this.categoriaSuperiora.set(categoriaSuperiora);
	}
	
	public ObjectProperty<CategoriaDespesa> categoriaSuperioraProperty(){
		return categoriaSuperiora;
	}
	
	@Override
	public String toString() {
		return getDescricao();
	}
	
}