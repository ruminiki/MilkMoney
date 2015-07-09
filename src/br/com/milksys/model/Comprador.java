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

import br.com.milksys.components.FieldRequired;

@Entity
@NamedQuery(name="Comprador.findAll", query="SELECT f FROM Comprador f")
public class Comprador extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private StringProperty nome = new SimpleStringProperty();
	private StringProperty cpfCnpj = new SimpleStringProperty();
	private StringProperty telefonePrincipal = new SimpleStringProperty();
	private StringProperty telefoneSecundario = new SimpleStringProperty();
	private StringProperty email = new SimpleStringProperty();
	private StringProperty endereco = new SimpleStringProperty();
	private StringProperty site = new SimpleStringProperty();
	private StringProperty observacao = new SimpleStringProperty();

	public Comprador() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="nome")
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
		this.telefoneSecundario.set(telefoneSecundario);
	}

	public StringProperty telefoneSecundarioProperty(){
		return telefoneSecundario;
	}
	
	@Access(AccessType.PROPERTY)
	public String getCpfCnpj() {
		return this.cpfCnpj.get();
	}

	public void setCpfCnpj(String cpfCnpf) {
		this.cpfCnpj.set(cpfCnpf);
	}

	public StringProperty cpfCnpfProperty(){
		return cpfCnpj;
	}
	
	@Access(AccessType.PROPERTY)
	public String getSite() {
		return this.site.get();
	}

	public void setSite(String site) {
		this.site.set(site);
	}

	public StringProperty siteProperty(){
		return site;
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
	
	@Override
	public String toString() {
		return getNome();
	}
	
}