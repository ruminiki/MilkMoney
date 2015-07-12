package br.com.milksys.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Component;

import br.com.milksys.model.Animal;
import br.com.milksys.model.FinalidadeAnimal;
import br.com.milksys.model.Sexo;
import br.com.milksys.model.SituacaoCobertura;

@Component
public class AnimalDao extends AbstractGenericDao<Integer, Animal> {
	
	@SuppressWarnings("unchecked")
	public List<Animal> findAllByNumeroNome(String param) {
		Query query = entityManager.createQuery("SELECT a FROM Animal a WHERE a.nome like :param or a.numero like :param");
		query.setParameter("param", '%' + param + '%');
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasAtivas() {
		
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a WHERE a.sexo = 'FÊMEA' "
						+ "and not exists (SELECT 1 FROM VendaAnimal v inner join v.animaisVendidos av WHERE av.animal.id = a.id) "
						+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");
		
		return query.getResultList();

	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllReprodutoresAtivos() {
		
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a WHERE a.sexo = 'MACHO' and a.finalidadeAnimal = '" + FinalidadeAnimal.REPRODUCAO + "' "
						+ "and not exists (SELECT 1 FROM VendaAnimal v inner join v.animaisVendidos av WHERE av.animal.id = a.id) "
						+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");
		
		return query.getResultList();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Animal> findAllMachosAtivos() {
		
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a WHERE a.sexo = 'MACHO' "
						+ "and not exists (SELECT 1 FROM VendaAnimal v inner join v.animaisVendidos av WHERE av.animal.id = a.id) "
						+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");
		
		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasCobertas() {
		
		Query query = entityManager.createQuery(
				"SELECT a FROM Cobertura c inner join c.femea a "
				+ "WHERE c.situacaoCobertura in ('" + SituacaoCobertura.INDEFINIDA + "','" + SituacaoCobertura.PRENHA + "') "
				+ "and not exists (SELECT 1 FROM VendaAnimal v inner join v.animaisVendidos av WHERE av.animal.id = a.id) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");
		
		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasNaoCobertas() {
		
		Query query = entityManager.createQuery(
				"SELECT a FROM Cobertura c inner join c.femea a "
				+ "WHERE c.situacaoCobertura not in ('" + SituacaoCobertura.INDEFINIDA + "','" + SituacaoCobertura.PRENHA + "') "
				+ "and not exists (SELECT 1 FROM VendaAnimal v inner join v.animaisVendidos av WHERE av.animal.id = a.id) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");
		List<Animal> animaisNaoCobertos = query.getResultList();
		
		query = entityManager.createQuery(
				"SELECT a FROM Animal a WHERE a not in (select c.femea from Cobertura c) and a.sexo = '" + Sexo.FEMEA + "' "
				+ "and not exists (SELECT 1 FROM VendaAnimal v inner join v.animaisVendidos av WHERE av.animal.id = a.id) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");

		animaisNaoCobertos.addAll(query.getResultList());
		
		return animaisNaoCobertos;
		
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasLactacaoXDias(double dias) {
		
		Query query = entityManager.createQuery("SELECT a FROM Parto p inner join p.cobertura.femea a "
						+ "WHERE DATEDIFF(p.data, current_date()) between 0 and :dias "
						+ "and not exists (SELECT 1 FROM VendaAnimal v inner join v.animaisVendidos av WHERE av.animal.id = a.id) "
						+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");
		
		query.setParameter("dias", dias);
		
		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasASecar() {
		
		Query query = entityManager.createQuery("SELECT a FROM Cobertura c inner join c.femea a "
				+ "WHERE DATEDIFF(c.previsaoParto, current_date()) <= 70 "
				+ "and not exists (SELECT 1 FROM EncerramentoLactacao e WHERE e.animal = a.id and e.data > c.data) "
				+ "and c.situacaoCobertura in ('" + SituacaoCobertura.INDEFINIDA + "','" + SituacaoCobertura.PRENHA + "') "
				+ "and not exists (SELECT 1 FROM VendaAnimal v inner join v.animaisVendidos av WHERE av.animal.id = a.id) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");

		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasLactacaoMaisXDias(double dias) {
		Query query = entityManager.createQuery("SELECT a FROM Parto p inner join p.cobertura.femea a "
				+ "WHERE DATEDIFF(p.parto, current_date()) >= :dias "
				+ "and not exists (SELECT 1 FROM VendaAnimal v inner join v.animaisVendidos av WHERE av.animal.id = a.id) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id) order by p.data desc limit 1");

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

		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasSecas() {
		
		Query query = entityManager.createQuery("SELECT a FROM EncerramentoLactacao e inner join e.animal a "
				+ "WHERE not exists (SELECT 1 FROM Parto p inner join p.cobertura c WHERE p.data > e.data and c.femea = a.id) "
				+ "and not exists (SELECT 1 FROM VendaAnimal v inner join v.animaisVendidos av WHERE av.animal.id = a.id) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");

		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllAnimaisMortos() {
		Query query = entityManager.createQuery("SELECT a FROM MorteAnimal m inner join m.animal a ");

		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Animal> findAllAnimaisVendidos() {
		Query query = entityManager.createQuery("SELECT a FROM AnimalVendido av inner join av.animal a");

		return query.getResultList();
	}
	
}