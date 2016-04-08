package br.com.milkmoney.model;

import java.io.Serializable;

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


@Entity
@Table(name="categoriaLancamentoFinanceiro")
@NamedQuery(name="CategoriaLancamentoFinanceiro.findAll", query="SELECT r FROM CategoriaLancamentoFinanceiro r")
public class CategoriaLancamentoFinanceiro extends AbstractEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private StringProperty                         descricao          = new SimpleStringProperty();
	
	public CategoriaLancamentoFinanceiro() {
	}
	
	public CategoriaLancamentoFinanceiro(String descricao) {
		setDescricao(descricao);
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
	
	/*@Access(AccessType.PROPERTY)
	@OneToOne(targetEntity=CategoriaLancamentoFinanceiro.class, cascade=CascadeType.REFRESH)
	@JoinColumn(name="categoriaSuperiora")
	public CategoriaLancamentoFinanceiro getCategoriaSuperiora() {
		return categoriaSuperiora.get();
	}
	
	public void setCategoriaSuperiora(CategoriaLancamentoFinanceiro categoriaSuperiora) {
		this.categoriaSuperiora.set(categoriaSuperiora);
	}
	
	public ObjectProperty<CategoriaLancamentoFinanceiro> categoriaSuperioraProperty(){
		return categoriaSuperiora;
	}*/
	
	@Override
	public String toString() {
		return getDescricao();
	}
	
}