package br.com.milksys.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milksys.model.Animal;
import br.com.milksys.model.FinalidadeAnimal;
import br.com.milksys.model.Sexo;
import br.com.milksys.model.SituacaoCobertura;

@Repository
public class AnimalDao extends AbstractGenericDao<Integer, Animal> {
	
	@SuppressWarnings("unchecked")
	public List<Animal> findAllByNumeroNome(String param) {
		Query query = entityManager.createQuery("SELECT a FROM Animal a WHERE a.nome like :param or a.numero like :param");
		query.setParameter("param", '%' + param + '%');
		query.setHint("org.hibernate.cacheable", "false");
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasAtivas() {
		
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a WHERE a.sexo = 'FÊMEA' "
						+ "and not exists (SELECT 1 FROM VendaAnimal v inner join v.animaisVendidos av WHERE av.animal.id = a.id) "
						+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");
		query.setHint("org.hibernate.cacheable", "false");
		return query.getResultList();

	}
	
	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeas() {
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a WHERE a.sexo = 'FÊMEA' ");
		query.setHint("org.hibernate.cacheable", "false");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllReprodutoresAtivos() {
		
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a WHERE a.sexo = 'MACHO' and a.finalidadeAnimal = '" + FinalidadeAnimal.REPRODUCAO + "' "
						+ "and not exists (SELECT 1 FROM VendaAnimal v inner join v.animaisVendidos av WHERE av.animal.id = a.id) "
						+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");
		query.setHint("org.hibernate.cacheable", "false");
		return query.getResultList();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Animal> findAllMachos() {
		
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a WHERE a.sexo = 'MACHO' ");
		query.setHint("org.hibernate.cacheable", "false");
		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasCobertas() {
		
		Query query = entityManager.createQuery(
				"SELECT distinct a FROM Cobertura c inner join c.femea a "
				+ "WHERE c.situacaoCobertura in ('" + SituacaoCobertura.INDEFINIDA + "','" + SituacaoCobertura.PRENHA + "') "
				+ "and not exists (SELECT 1 FROM VendaAnimal v inner join v.animaisVendidos av WHERE av.animal.id = a.id) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");
		query.setHint("org.hibernate.cacheable", "false");
		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasNaoCobertas() {
		
		Query query = entityManager.createQuery(
				"SELECT distinct a FROM Animal a WHERE a.sexo = '" + Sexo.FEMEA + "' and "
				+ "not exists (SELECT 1 FROM Cobertura c where c.situacaoCobertura in ('" + SituacaoCobertura.INDEFINIDA + "','" + SituacaoCobertura.PRENHA + "') and c.femea = a.id ) "
				+ "and not exists (SELECT 1 FROM VendaAnimal v inner join v.animaisVendidos av WHERE av.animal.id = a.id) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");
		query.setHint("org.hibernate.cacheable", "false");
		
		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasLactacaoXDias(Integer dias) {
		
		Query query = entityManager.createQuery("SELECT a FROM Parto p inner join p.cobertura.femea a "
						+ "WHERE DATEDIFF(current_date(), p.data) between 0 and :dias "
						+ "and not exists (SELECT 1 FROM VendaAnimal v inner join v.animaisVendidos av WHERE av.animal.id = a.id) "
						+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");
		query.setHint("org.hibernate.cacheable", "false");
		query.setParameter("dias", dias);
		
		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasASecar() {
		
		Query query = entityManager.createQuery("SELECT a FROM Cobertura c inner join c.femea a "
				+ "WHERE DATEDIFF(c.previsaoParto, current_date()) between 0 and 70 "
				+ "and not exists (SELECT 1 FROM EncerramentoLactacao e WHERE e.animal = a.id and e.data > c.data) "
				+ "and c.situacaoCobertura in ('" + SituacaoCobertura.INDEFINIDA + "','" + SituacaoCobertura.PRENHA + "') "
				+ "and not exists (SELECT 1 FROM VendaAnimal v inner join v.animaisVendidos av WHERE av.animal.id = a.id) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");
		query.setHint("org.hibernate.cacheable", "false");
		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasLactacaoMaisXDias(Integer dias) {
		Query query = entityManager.createQuery(
				"SELECT a FROM Parto p inner join p.cobertura.femea a "
				+ "WHERE DATEDIFF(current_date(), p.data) >= :dias "
				+ "and not exists (SELECT 1 FROM Parto p1 inner join p1.cobertura c1 WHERE c1.femea.id = a.id and p1.data > p.data) "
				+ "and not exists (SELECT 1 FROM VendaAnimal v inner join v.animaisVendidos av WHERE av.animal.id = a.id) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id) order by p.data desc");
		query.setMaxResults(1);
		query.setHint("org.hibernate.cacheable", "false");
		query.setParameter("dias", dias);
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasEmLactacao() {
		
		Query query = entityManager.createQuery("SELECT a FROM Cobertura c inner join c.femea a "
				+ "inner join c.parto p "
				+ "WHERE not exists (SELECT 1 FROM EncerramentoLactacao e WHERE e.data > p.data and e.animal.id = a.id) "
				+ "and not exists (SELECT 1 FROM VendaAnimal v inner join v.animaisVendidos av WHERE av.animal.id = a.id) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");
		query.setHint("org.hibernate.cacheable", "false");
		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasSecas() {
		
		Query query = entityManager.createQuery("SELECT distinct a FROM EncerramentoLactacao e inner join e.animal a "
				+ "WHERE not exists (SELECT 1 FROM Parto p inner join p.cobertura c WHERE p.data > e.data and c.femea = a.id) "
				+ "and not exists (SELECT 1 FROM VendaAnimal v inner join v.animaisVendidos av WHERE av.animal.id = a.id) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");
		query.setHint("org.hibernate.cacheable", "false");
		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllAnimaisMortos() {
		Query query = entityManager.createQuery("SELECT a FROM MorteAnimal m inner join m.animal a ");
		query.setHint("org.hibernate.cacheable", "false");
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Animal> findAllAnimaisVendidos() {
		Query query = entityManager.createQuery("SELECT a FROM AnimalVendido av inner join av.animal a");
		query.setHint("org.hibernate.cacheable", "false");
		return query.getResultList();
	}

}