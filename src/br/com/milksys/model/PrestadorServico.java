package br.com.milksys.model;

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


/**
 * The persistent class for the funcionario database table.
 * 
 */
@Entity
@NamedQuery(name="PrestadorServico.findAll", query="SELECT f FROM PrestadorServico f")
public class PrestadorServico extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private StringProperty nome = new SimpleStringProperty();
	private StringProperty email = new SimpleStringProperty();
	private StringProperty cpf = new SimpleStringProperty();
	private StringProperty telefonePrincipal = new SimpleStringProperty();
	private StringProperty telefoneSecundario = new SimpleStringProperty();
	private StringProperty endereco = new SimpleStringProperty();

	public PrestadorServico() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Access(AccessType.PROPERTY)
	public String getNome() {
		return this.nome.get();
	}

	public void setNome(String nome) {
		this.nome.set(nome);
	}

	public StringProperty nomeProperty(){
		return nome;
	}
	
	@Access(AccessType.PROPERTY)
	public String getEmail() {
		return this.email.get();
	}

	public void setEmail(String email) {
		this.email.set(email);
	}

	public StringProperty emailProperty(){
		return email;
	}
	
	@Access(AccessType.PROPERTY)
	public String getTelefonePrincipal() {
		return this.telefonePrincipal.get();
	}

	public void setTelefonePrincipal(String telefonePrincipal) {
		this.telefonePrincipal.set(telefonePrincipal);
	}

	public StringProperty telefonePrincipalProperty(){
		return telefonePrincipal;
	}
	
	@Access(AccessType.PROPERTY)
	public String getTelefoneSecundario() {
		return this.telefoneSecundario.get();
	}

	public void setTelefoneSecundario(String telefoneSecundario) {
		this.telefonePrincipal.set(telefoneSecundario);
	}

	public StringProperty telefoneSecundarioProperty(){
		return telefoneSecundario;
	}
	
	@Access(AccessType.PROPERTY)
	public String getCpf() {
		return this.cpf.get();
	}

	public void setCpf(String cpf) {
		this.cpf.set(cpf);
	}
	
	public StringProperty cpfProperty(){
		return cpf;
	}

	@Access(AccessType.PROPERTY)
	public String getEndereco() {
		return this.endereco.get();
	}

	public void setEndereco(String endereco) {
		this.endereco.set(endereco);
	}

	public StringProperty enderecoProperty(){
		return endereco;
	}
	
	@Override
	public String toString() {
		return getNome();
	}
	
}