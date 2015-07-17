package br.com.milksys.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milksys.model.Animal;
import br.com.milksys.model.FinalidadeAnimal;
import br.com.milksys.model.Lactacao;
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
				"SELECT a FROM Animal a WHERE a.sexo = 'F�MEA' "
						+ "and not exists (SELECT 1 FROM VendaAnimal v inner join v.animaisVendidos av WHERE av.animal.id = a.id) "
						+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");
		query.setHint("org.hibernate.cacheable", "false");
		return query.getResultList();

	}
	
	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeas() {
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a WHERE a.sexo = 'F�MEA' ");
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

	/*
	 *Recupera todos os animais em lacta��o em um determinado per�odo.
	 *Se n�o for passado par�metro, considera a data atual.
	 */
	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasEmLactacao(Object ...params) {
		
		Date   data   = (params != null && params.length > 0) ? (Date)   params[0] : new Date();

		Query query = entityManager.createQuery(
				"SELECT a FROM Cobertura c inner join c.femea a inner join c.parto p WHERE p.data < :data "
				+ "and not exists (SELECT 1 FROM EncerramentoLactacao e WHERE e.parto.id = p.id and e.data <= :data) "
				+ "and not exists (SELECT 1 FROM VendaAnimal v inner join v.animaisVendidos av WHERE av.animal.id = a.id and v.dataVenda <= :data) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id and m.dataMorte <= :data) ");
		query.setHint("org.hibernate.cacheable", "false");
		query.setParameter("data", data);
		return query.getResultList();
		
	}
	
	/*
	 *Identifica se o animal est� em lacta��o em uma determinada data.
	 */
	public Boolean animalEstaEmLactacao(Date data, Animal animal) {
		
		Query query = entityManager.createQuery(
				"SELECT a FROM Cobertura c inner join c.femea a inner join c.parto p WHERE p.data < :data and a = :animal "
				+ "and not exists (SELECT 1 FROM EncerramentoLactacao e WHERE e.parto.id = p.id and e.data <= :data) "
				+ "and not exists (SELECT 1 FROM VendaAnimal v inner join v.animaisVendidos av WHERE av.animal.id = a.id and v.dataVenda <= :data) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id and m.dataMorte <= :data) ");
		query.setHint("org.hibernate.cacheable", "false");
		query.setParameter("data", data);
		query.setParameter("animal", animal);
		
		try{
			return query.getSingleResult() != null;
		}catch ( NoResultException e ){
			return false;
		}
		
	}
	/*
	 * Conta quantos animais est�o em lacta��o em uma determinada data.
	 * Utilizado para popular o campo animais ordenhados, na tela de produ��o di�ria.
	 * 
	 */
	public Long countAnimaisEmLactacao(Date data) {
		
		Query query = entityManager.createQuery(
				"SELECT count(a) FROM Cobertura c inner join c.femea a inner join c.parto p WHERE p.data < :data "
				+ "and not exists (SELECT 1 FROM EncerramentoLactacao e WHERE e.parto.id = p.id and e.data <= :data) "
				+ "and not exists (SELECT 1 FROM VendaAnimal v inner join v.animaisVendidos av WHERE av.animal.id = a.id and v.dataVenda <= :data) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id and m.dataMorte <= :data) ");
		query.setHint("org.hibernate.cacheable", "false");
		query.setParameter("data", data);
		
		try{
			return (Long) query.getSingleResult();
		}catch ( NoResultException e ){
			return 0L;
		}
		
	}
	
	/*
	 * Identifica os periodos de lacta��o do animal.
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Lactacao> findLactacoesAnimal(Animal animal) {
		
		List<Lactacao> lactacoes = new ArrayList<Lactacao>();
		
		Query query = entityManager.createNativeQuery(
				"select p.data as dataInicio, coalesce(e.data,va.dataVenda,ma.dataMorte,current_date()) as dataFim from animal a "
				+ "inner join cobertura c on (c.femea = a.id) "
				+ "inner join parto p on (p.id = c.parto) "
				+ "left join encerramentoLactacao e on (e.parto = p.id) "
				+ "left join animalVendido av on (av.animal = a.id ) "
				+ "left join vendaAnimal va on (va.id = av.vendaAnimal) "
				+ "left join morteAnimal ma on (ma.animal = a.id)");
				
		List<Object[]> result = (List<Object[]>) query.getResultList();
		
		for ( Object[] row : result ){
			Lactacao lactacao = new Lactacao();
			lactacao.setDataInicio( (Date) row[0] );
			lactacao.setDataFim( (Date) row[1] );
			lactacoes.add(lactacao);
		}
		
		return lactacoes;
		
	}
	
	public Long countNumeroPartos(Animal animal) {
		
		Query query = entityManager.createQuery("SELECT count(p) FROM Parto p where p.cobertura.femea = :animal");
		query.setHint("org.hibernate.cacheable", "false");
		query.setParameter("animal", animal);
		
		try{
			return (Long) query.getSingleResult();
		}catch ( NoResultException e ){
			return 0L;
		}
		
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

	@SuppressWarnings("unchecked")
	public List<Animal> findAnimaisComMaisDeUmParto() {
		
		Query query = entityManager.createQuery("SELECT a FROM Animal a where "
				+ "not exists (select 1 from AnimalVendido av where av.animal.id = a.id) and "
				+ "not exists (select 1 from MorteAnimal ma where ma.animal.id = a.id) "
				+ "group by a having (select count(p) from Parto p where p.cobertura.femea.id = a.id ) >= 2 ");
		
		return query.getResultList();
		
	}

	public BigInteger countAllAtivos() {
		Object result = entityManager.createNativeQuery("select count(*) from viewAnimaisAtivos a").getSingleResult();
		return (BigInteger) result;
	}

	public BigInteger countAllFemeasAtivas() {
		Object result = entityManager.createNativeQuery("select count(*) from viewAnimaisAtivos where sexo = '" + Sexo.FEMEA + "'").getSingleResult();
		return (BigInteger) result;
	}

	public BigInteger countAllVacasAtivas() {
		Object result = entityManager.createNativeQuery(
				"select count(*) from viewAnimaisAtivos a where a.sexo = '" + Sexo.FEMEA + "' and exists  " +
				"(select 1 from cobertura c inner join parto p on (p.cobertura = c.id) where c.femea = a.id)").getSingleResult();
		return (BigInteger) result;
	}

	public BigInteger countAllNovilhasIdadeAteXMeses(int meses) {
		int dias = meses * 30;
		Query query = entityManager.createNativeQuery(
				"select count(*) from viewAnimaisAtivos a " +
				"where DATEDIFF(current_date(), a.dataNascimento) <= :dias  " +
				"and a.sexo = '" + Sexo.FEMEA + "' and not exists "
				+ "(select 1 from cobertura c inner join parto p on (p.cobertura = c.id) where c.femea = a.id)");
		query.setParameter("dias", dias);
		return (BigInteger) query.getSingleResult();
	}

	public BigInteger countAllNovilhasIdadeEntreMeses(int mesesIdadeMinima, int mesesIdadeMaxima) {
		int diasIdadeMinima = mesesIdadeMinima * 30;
		int diasIdadeMaxima = mesesIdadeMaxima * 30;
		Query query = entityManager.createNativeQuery(
				"select count(*) from viewAnimaisAtivos a " +
				"where DATEDIFF(current_date(), a.dataNascimento) between :min and :max " +
				"and a.sexo = '" + Sexo.FEMEA + "' and not exists "
				+ "(select 1 from cobertura c inner join parto p on (p.cobertura = c.id) where c.femea = a.id)");
		query.setParameter("min", diasIdadeMinima);
		query.setParameter("max", diasIdadeMaxima);
		return (BigInteger) query.getSingleResult();
	}

	public BigInteger countAllNovilhasIdadeAcimaXMeses(int meses) {
		int dias = meses * 30;
		Query query = entityManager.createNativeQuery(
				"select count(*) from viewAnimaisAtivos a " +
				"where DATEDIFF(current_date(), a.dataNascimento) > :dias  " +
				"and a.sexo = '" + Sexo.FEMEA + "' and not exists "
				+ "(select 1 from cobertura c inner join parto p on (p.cobertura = c.id) where c.femea = a.id)");
		query.setParameter("dias", dias);
		return (BigInteger) query.getSingleResult();
	}

	public BigInteger countAllFemeasEmLactacao() {
		Object result = entityManager.createNativeQuery(" select count(*) from viewAnimaisAtivos a "
				+ "inner join cobertura c inner join parto p on (p.cobertura = c.id) "
				+ "where c.femea = a.id and not exists "
				+ "(select 1 from encerramentoLactacao e where e.data > p.data and e.animal = a.id)").getSingleResult();
		
		return (BigInteger) result;
	}

	public BigInteger countAllFemeasSecas() {
		Object result = entityManager.createNativeQuery("select count(*) from viewAnimaisAtivos a "
				+ "inner join encerramentoLactacao e on (e.animal = a.id) where not exists "
				+ "(select 1 from parto p inner join cobertura c on (c.id = p.cobertura) where p.data > e.data and c.femea = a.id)").getSingleResult();
		return (BigInteger) result;
	}

	public BigInteger countVacasDisponiveisParaCoberturaUltimos21Dias(int diasIdadeMinimaParaCobertura, int periodoVoluntarioEspera) {
		//vacas dispon�veis para serem cobertas:
		//(1) n�o vendidas, (2) n�o mortas, (3) que n�o estejam cobertas(prenhas) no per�odo, (3) n�o s�o rec�m paridas, (4) tem idade suficiente para cobertura
		return (BigInteger) entityManager.createNativeQuery(
				"select count(*) from animal a where DATEDIFF(current_date(), a.dataNascimento) between 0 and " + diasIdadeMinimaParaCobertura + " and "
				+ "not exists (select 1 from animalVendido av where av.animal = a.id) and "
				+ "not exists (select 1 from morteAnimal ma where ma.animal = a.id) and "
				+ "not exists (select 1 from cobertura c where c.femea = a.id and DATEDIFF(current_date(), c.data) < 21 and c.situacaoCobertura in ('" + SituacaoCobertura.PRENHA + "','" + SituacaoCobertura.INDEFINIDA + "')) and "
				+ "not exists (select 1 from parto p inner join cobertura c on (c.id = p.cobertura) where c.femea = a.id and DATEDIFF(current_date(), p.data) between 0 and " + periodoVoluntarioEspera + ")").getSingleResult();
	}

	public BigInteger countAllNovilhasAtivas() {
		Query query = entityManager.createNativeQuery(
				"select count(*) from viewAnimaisAtivos a " +
				"where a.sexo = '" + Sexo.FEMEA + "' and not exists "
				+ "(select 1 from cobertura c inner join parto p on (p.cobertura = c.id) where c.femea = a.id)");
		return (BigInteger) query.getSingleResult();
	}

}