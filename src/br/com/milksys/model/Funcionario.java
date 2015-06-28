package br.com.milksys.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.milksys.util.DateUtil;
import br.com.milksys.util.NumberFormatUtil;


/**
 * The persistent class for the funcionario database table.
 * 
 */
@Entity
@NamedQuery(name="Funcionario.findAll", query="SELECT f FROM Funcionario f")
public class Funcionario extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private ObjectProperty<LocalDate> dataContratacao = new SimpleObjectProperty<LocalDate>(LocalDate.now());  
	private StringProperty nome = new SimpleStringProperty();
	private StringProperty email = new SimpleStringProperty();
	private StringProperty telefone = new SimpleStringProperty();
	private StringProperty diaPagamento = new SimpleStringProperty();
	private StringProperty jornadaTrabalho = new SimpleStringProperty();
	private StringProperty salario = new SimpleStringProperty();

	public Funcionario() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	public Date getDataContratacao() {
		return DateUtil.asDate(this.dataContratacao.get());
	}

	public void setDataContratacao(Date dataContratacao) {
		this.dataContratacao.set(DateUtil.asLocalDate(dataContratacao));
	}

	public ObjectProperty<LocalDate> dataContratacaoProperty(){
		return dataContratacao;
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
	public String getTelefone() {
		return this.telefone.get();
	}

	public void setTelefone(String telefone) {
		this.telefone.set(telefone);
	}

	public StringProperty telefoneProperty(){
		return telefone;
	}
	
	@Access(AccessType.PROPERTY)
	public int getDiaPagamento() {
		return Integer.valueOf(this.diaPagamento.get());
	}
	
	public void setDiaPagamento(int diaPagamento) {
		this.diaPagamento.set(String.valueOf(diaPagamento));
	}
	
	public StringProperty diaPagamentoProperty(){
		return diaPagamento;
	}
	
	@Access(AccessType.PROPERTY)
	public String getJornadaTrabalho() {
		return this.jornadaTrabalho.get();
	}

	public void setJornadaTrabalho(String jornadaTrabalho) {
		this.jornadaTrabalho.set(jornadaTrabalho);
	}
	
	public StringProperty jornadaTrabalhoProperty(){
		return jornadaTrabalho;
	}
	
	@Access(AccessType.PROPERTY)
	public BigDecimal getSalario() {
		return NumberFormatUtil.fromString(this.salario.get());
	}

	public void setSalario(BigDecimal salario) {
		this.salario.set(NumberFormatUtil.decimalFormat(salario));
	}

	public StringProperty salarioProperty(){
		return salario;
	}
	
	@Override
	public String toString() {
		return getNome();
	}
	
}