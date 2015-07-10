package br.com.milksys.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Entity
@IdClass(SituacaoAnimalPK.class)
/*@NamedNativeQuery(name = "findSituacaoAnimal", query = "SELECT 'VENDIDO' as situacao, v.animal FROM animalVendido v "+
	"UNION ALL  "+
	"SELECT 'MORTO' as situacao, m.animal FROM morteAnimal m "+
	"UNION ALL  "+
	"SELECT 'SECO' as situacao, e.animal FROM encerramentoLactacao e  "+
	"WHERE NOT EXISTS( SELECT 1 FROM parto p INNER JOIN cobertura c ON (c.id = p.cobertura)  "+
	"WHERE p.data >= e.data and c.femea = e.animal) LIMIT 1  "+
	"UNION ALL  "+
	"SELECT 'EM LACTAÇÃO' as situacao, a.id FROM animal a  "+
	"INNER JOIN cobertura c on (c.femea = a.id) INNER JOIN parto p on (p.cobertura = c.id) "+
	"WHERE NOT EXISTS( SELECT 1 FROM encerramentoLactacao e WHERE e.data > p.data and e.animal = a.id)  "+
	"LIMIT 1", resultClass = SituacaoAnimal.class)*/
@Immutable
@Subselect(
	"SELECT 'VENDIDO' as situacao, v.animal FROM animalVendido v "+
	"UNION ALL  "+
	"SELECT 'MORTO' as situacao, m.animal FROM morteAnimal m "+
	"UNION ALL  "+
	"SELECT 'SECO' as situacao, e.animal FROM encerramentoLactacao e  "+
	"WHERE NOT EXISTS( SELECT 1 FROM parto p INNER JOIN cobertura c ON (c.id = p.cobertura)  "+
	"WHERE p.data >= e.data and c.femea = e.animal) LIMIT 1  "+
	"UNION ALL  "+
	"SELECT 'EM LACTAÇÃO' as situacao, a.id FROM animal a  "+
	"INNER JOIN cobertura c on (c.femea = a.id) INNER JOIN parto p on (p.cobertura = c.id) "+
	"WHERE NOT EXISTS( SELECT 1 FROM encerramentoLactacao e WHERE e.data > p.data and e.animal = a.id)  "+
	"LIMIT 1")
@Synchronize( {"animal"} ) //tables impacted
public class SituacaoAnimal{
	
	public static final String EM_LACTACAO = "EM LACTAÇÃO";
	public static final String SECO = "SECO";
	public static final String VENDIDO = "VENDIDO";
	public static final String MORTO = "MORTO";
	public static final String NAO_DEFINIDA = "NÃO DEFINIDA";
	
	/*@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;*/
	@Id
	private String situacao;
	@Id
	private int animal;
	
	public SituacaoAnimal() {
		// TODO Auto-generated constructor stub
	}
	
	public SituacaoAnimal(String situacao) {
		this.situacao = situacao;
	}

	/*public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}*/

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public int getAnimal() {
		return animal;
	}

	public void setAnimal(int animal) {
		this.animal = animal;
	}

	public static ObservableList<String> getItems(){
		return FXCollections.observableArrayList(EM_LACTACAO, SECO, VENDIDO, MORTO, NAO_DEFINIDA);
	}
	
}

