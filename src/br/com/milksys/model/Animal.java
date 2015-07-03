package br.com.milksys.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
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
@Table(name="animal")
@NamedQuery(name="Animal.findAll", query="SELECT a FROM Animal a")
public class Animal extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private ObjectProperty<LocalDate> dataNascimento = new SimpleObjectProperty<LocalDate>(LocalDate.now());  
	private StringProperty nome = new SimpleStringProperty();
	private StringProperty numero = new SimpleStringProperty();
	private StringProperty sexo = new SimpleStringProperty();
	private ObjectProperty<Raca> raca = new SimpleObjectProperty<Raca>();
	private StringProperty finalidadeAnimal = new SimpleStringProperty();
	private StringProperty situacaoAnimal = new SimpleStringProperty();
	
	public Animal() {
	}

	public Animal(String sexo) {
		this.sexo.set(sexo);
	}

	public Animal(String sexo, String finalidadeAnimal) {
		this.sexo.set(sexo);
		this.finalidadeAnimal.set(finalidadeAnimal);
	}

	@Temporal(TemporalType.DATE)
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="data de nascimento")
	public Date getDataNascimento() {
		return DateUtil.asDate(this.dataNascimento.get());
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento.set(DateUtil.asLocalDate(dataNascimento));
	}
	
	public ObjectProperty<LocalDate> dataNascimentoProperty(){
		return dataNascimento;
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
	@FieldRequired(message="número")
	public String getNumero() {
		return this.numero.get();
	}

	public void setNumero(String numero) {
		this.numero.set(numero);
	}
	
	public StringProperty numeroProperty(){
		return numero;
	}
	
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="finalidade do animal")
	public String getFinalidadeAnimal() {
		return finalidadeAnimal.get();
	}
	
	public void setFinalidadeAnimal(String finalidadeAnimal) {
		this.finalidadeAnimal.set(finalidadeAnimal);
	}
	
	public StringProperty finalidadeAnimalProperty(){
		return finalidadeAnimal;
	}
	
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="sexo")
	public String getSexo() {
		return this.sexo.get();
	}

	public void setSexo(String sexo) {
		this.sexo.set(sexo);
	}
	
	public StringProperty sexoProperty(){
		return sexo;
	}
	
	@Access(AccessType.PROPERTY)
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="raca")
	@FieldRequired(message="raça")
	public Raca getRaca() {
		return raca.get();
	}
	
	public void setRaca(Raca raca) {
		this.raca.set(raca);
	}
	
	public ObjectProperty<Raca> racaProperty(){
		return raca;
	}
	
	@Access(AccessType.PROPERTY)
	public String getSituacaoAnimal() {
		return situacaoAnimal.get();
	}
	
	public void setSituacaoAnimal(String situacaoAnimal) {
		this.situacaoAnimal.set(situacaoAnimal);
	}
	
	public StringProperty situacaoAnimalProperty(){
		return situacaoAnimal;
	}
	
	//==========================
	public String getNumeroNome(){
		return this.numero.get() + "-" + this.nome.get();
	}
	
	public StringProperty numeroNomeProperty(){
		return new SimpleStringProperty(numero.get() + "-" + this.nome.get());
	}
	
	public long getIdade() {
		
		if ( this.dataNascimento != null && this.dataNascimento.get() != null )
			return ChronoUnit.MONTHS.between(this.dataNascimento.get(), LocalDate.now());
		return 0;
		
	}
	
	@Override
	public String toString() {
		return getNumeroNome();
	}

}