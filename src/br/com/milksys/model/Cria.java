package br.com.milksys.model;

import java.io.Serializable;
import java.math.BigDecimal;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.milksys.components.FieldRequired;
import br.com.milksys.util.NumberFormatUtil;


/**
 * The persistent class for the CRIA database table.
 * 
 */
@Entity
@Table(name="cria")
@NamedQuery(name="Cria.findAll", query="SELECT a FROM Cria a")
public class Cria extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private StringProperty            sexo = new SimpleStringProperty(Sexo.FEMEA);
	private ObjectProperty<Parto>     parto = new SimpleObjectProperty<Parto>();
	private ObjectProperty<Animal>    animal = new SimpleObjectProperty<Animal>();
	private StringProperty            situacaoNascimento = new SimpleStringProperty(SituacaoNascimento.NASCIDO_VIVO);
	private StringProperty            peso = new SimpleStringProperty();
	private StringProperty            incorporadoAoRebanho = new SimpleStringProperty();
	
	public Cria() {}
	
	public Cria(Parto parto){
		this.parto.set(parto);
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
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
	@FieldRequired(message="situação nascimento")
	public String getSituacaoNascimento() {
		return this.situacaoNascimento.get();
	}

	public void setSituacaoNascimento(String situacaoNascimento) {
		this.situacaoNascimento.set(situacaoNascimento);
	}
	
	public StringProperty situacaoNascimentoProperty(){
		return situacaoNascimento;
	}
	
	@Access(AccessType.PROPERTY)
	@FieldRequired(message="incorporado ao rebanho")
	public String getIncorporadoAoRebanho() {
		return this.incorporadoAoRebanho.get();
	}

	public void setIncorporadoAoRebanho(String incorporadoAoRebanho) {
		this.incorporadoAoRebanho.set(incorporadoAoRebanho);
	}
	
	public StringProperty incorporadoAoRebanhoProperty(){
		return incorporadoAoRebanho;
	}
	
	@Access(AccessType.PROPERTY)
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinColumn(name="parto")
	public Parto getParto() {
		return parto.get();
	}
	
	public void setParto(Parto parto) {
		this.parto.set(parto);
	}
	
	public ObjectProperty<Parto> partoProperty(){
		return parto;
	}
	
	@Access(AccessType.PROPERTY)
	@OneToOne(orphanRemoval=true, targetEntity=Animal.class, cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinColumn(name="animal")
	public Animal getAnimal() {
		return animal.get();
	}
	
	public void setAnimal(Animal animal) {
		this.animal.set(animal);
	}
	
	public ObjectProperty<Animal> animalProperty(){
		return animal;
	}
	
	@Access(AccessType.PROPERTY)
	public BigDecimal getPeso() {
		return NumberFormatUtil.fromString(this.peso.get());
	}

	public void setPeso(BigDecimal peso) {
		this.peso.set(NumberFormatUtil.decimalFormat(peso));
	}

	public StringProperty pesoProperty(){
		return peso;
	}
	
	@Override
	public String toString() {
		return getSexo() + " - " + getSituacaoNascimento();
	}

}