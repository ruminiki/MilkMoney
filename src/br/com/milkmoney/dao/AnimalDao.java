package br.com.milkmoney.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.FinalidadeAnimal;
import br.com.milkmoney.model.Sexo;
import br.com.milkmoney.model.SituacaoCobertura;

@Repository
public class AnimalDao extends AbstractGenericDao<Integer, Animal> {
	
	@SuppressWarnings("unchecked")
	public List<Animal> defultSearch(String param) {
		Query query = entityManager.createQuery("SELECT a FROM Animal a WHERE "
				+ "a.nome like :param or a.numero like :param or "
				+ "a.raca.descricao like :param or "
				+ "a.sexo like :param or "
				+ "a.finalidadeAnimal like :param or "
				+ "a.situacaoAnimal like :param");
		query.setParameter("param", '%' + param + '%');
		query.setHint("org.hibernate.cacheable", "false");
		
		return query.getResultList();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Animal> findAllByNumeroNome(String param) {
		Query query = entityManager.createQuery("SELECT a FROM Animal a WHERE a.nome like :param or a.numero like :param");
		query.setParameter("param", '%' + param + '%');
		query.setMaxResults(15);
		query.setHint("org.hibernate.cacheable", "false");
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Animal> findFemeasByNumeroNome(String param) {
		Query query = entityManager.createQuery("SELECT a FROM Animal a WHERE a.nome like :param or a.numero like :param and a.sexo = '" + Sexo.FEMEA + "'");
		query.setParameter("param", '%' + param + '%');
		query.setMaxResults(15);
		query.setHint("org.hibernate.cacheable", "false");
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasAtivas() {
		
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a WHERE a.sexo = 'F�MEA' "
						+ "and not exists (SELECT 1 FROM VendaAnimal v WHERE v.animal.id = a.id) "
						+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");
		query.setHint("org.hibernate.cacheable", "false");
		return query.getResultList();

	}
	
	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeas() {
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a WHERE a.sexo = '" + Sexo.FEMEA + "' ");
		query.setHint("org.hibernate.cacheable", "false");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllReprodutoresAtivos() {
		
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a WHERE a.sexo = '" + Sexo.MACHO + "' and a.finalidadeAnimal = '" + FinalidadeAnimal.REPRODUCAO + "' "
						+ "and not exists (SELECT 1 FROM VendaAnimal v WHERE v.animal.id = a.id) "
						+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");
		query.setHint("org.hibernate.cacheable", "false");
		return query.getResultList();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Animal> findAllMachos() {
		
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a WHERE a.sexo = '" + Sexo.MACHO + "' ");
		query.setHint("org.hibernate.cacheable", "false");
		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasCobertas() {
		
		Query query = entityManager.createQuery(
				"SELECT distinct a FROM Cobertura c inner join c.femea a "
				+ "WHERE c.situacaoCobertura in ('" + SituacaoCobertura.NAO_CONFIRMADA + "','" + SituacaoCobertura.PRENHA + "') "
				+ "and not exists (SELECT 1 FROM VendaAnimal v WHERE v.animal.id = a.id) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");
		query.setHint("org.hibernate.cacheable", "false");
		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasNaoCobertas() {
		
		Query query = entityManager.createQuery(
				"SELECT distinct a FROM Animal a WHERE a.sexo = '" + Sexo.FEMEA + "' and "
				+ "not exists (SELECT 1 FROM Cobertura c where c.situacaoCobertura in ('" + SituacaoCobertura.NAO_CONFIRMADA + "','" + SituacaoCobertura.PRENHA + "') and c.femea = a.id ) "
				+ "and not exists (SELECT 1 FROM VendaAnimal v WHERE v.animal.id = a.id) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");
		query.setHint("org.hibernate.cacheable", "false");
		
		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasLactacaoXDias(Integer dias) {
		
		Query query = entityManager.createQuery("SELECT a FROM Lactacao lc inner join lc.animal a "
						+ "WHERE lc.dataFim is null and DATEDIFF(current_date(), lc.dataInicio) between 0 and :dias "
						+ "and not exists (SELECT 1 FROM VendaAnimal v WHERE v.animal.id = a.id) "
						+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");
		query.setHint("org.hibernate.cacheable", "false");
		query.setParameter("dias", dias);
		
		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasASecar() {
		
		Query query = entityManager.createQuery("SELECT a FROM Animal a "
				+ "WHERE exists (SELECT 1 FROM Lactacao lc WHERE lc.animal = a and DATEDIFF(current_date(), lc.dataInicio) >= 210 and lc.dataFim is null) "
				+ "and not exists (SELECT 1 FROM VendaAnimal v WHERE v.animal.id = a.id) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");
		query.setHint("org.hibernate.cacheable", "false");
		return query.getResultList();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasSecas() {
		
		Query query = entityManager.createQuery("SELECT a FROM Animal a "
				+ "WHERE not exists (SELECT 1 FROM Lactacao lc where lc.animal = a and lc.dataFim is null) "
				+ "and exists (SELECT 1 FROM Parto p where p.cobertura.femea = a) "
				+ "and not exists (SELECT 1 FROM VendaAnimal v WHERE v.animal.id = a.id) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");
		query.setHint("org.hibernate.cacheable", "false");
		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasLactacaoMaisXDias(Integer dias) {
		Query query = entityManager.createQuery(
				"SELECT a FROM Lactacao lc inner join lc.animal a "
				+ "WHERE DATEDIFF(current_date(), lc.dataInicio) >= :dias and lc.dataFim is null "
				+ "and not exists (SELECT 1 FROM VendaAnimal v WHERE v.animal.id = a.id) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id) order by lc.dataInicio");
		query.setMaxResults(1);
		query.setHint("org.hibernate.cacheable", "false");
		query.setParameter("dias", dias);
		
		return query.getResultList();
	}

	/*
	 *Recupera todos os animais em lacta��o em uma determinada data.
	 *Se n�o for passado par�metro, considera a data atual.
	 */
	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasEmLactacao(Object ...params) {
		
		Date   data   = (params != null && params.length > 0) ? (Date)   params[0] : new Date();

		Query query = entityManager.createQuery(
				"SELECT a FROM Lactacao lc inner join lc.animal a WHERE :data between lc.dataInicio and coalesce(lc.dataFim, current_date()) "
				+ "and not exists (SELECT 1 FROM VendaAnimal v WHERE v.animal.id = a.id and v.dataVenda <= :data) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id and m.dataMorte <= :data) ");
		query.setHint("org.hibernate.cacheable", "false");
		query.setParameter("data", data);
		return query.getResultList();
		
	}
	
	/*
	 *Identifica se o animal est� em lacta��o em uma determinada data.
	 */
	public Boolean isInLactacao(Date data, Animal animal) {
		
		Query query = entityManager.createQuery(
				"SELECT a FROM Lactacao lc inner join lc.animal a WHERE lc.animal = :animal and :data between lc.dataInicio and coalesce(lc.dataFim, current_date()) "
				+ "and not exists (SELECT 1 FROM VendaAnimal v WHERE v.animal.id = a.id and v.dataVenda <= :data) "
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
	public Long contaAnimaisEmLactacao(Date data) {
		
		Query query = entityManager.createQuery(
				"SELECT count(a) FROM Lactacao lc inner join lc.animal a WHERE :data between lc.dataInicio and coalesce(lc.dataFim, current_date()) "
				+ "and not exists (SELECT 1 FROM VendaAnimal v WHERE v.animal.id = a.id and v.dataVenda <= :data) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id and m.dataMorte <= :data) ");
		query.setHint("org.hibernate.cacheable", "false");
		query.setParameter("data", data);
		
		try{
			return (Long) query.getSingleResult();
		}catch ( NoResultException e ){
			return 0L;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Animal> findAllAnimaisMortos() {
		Query query = entityManager.createQuery("SELECT a FROM MorteAnimal m inner join m.animal a ");
		query.setHint("org.hibernate.cacheable", "false");
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Animal> findAllAnimaisVendidos() {
		Query query = entityManager.createQuery("SELECT a FROM VendaAnimal v inner join v.animal a");
		query.setHint("org.hibernate.cacheable", "false");
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Animal> findAnimaisComCobertura() {
		Query query = entityManager.createQuery("SELECT a FROM Animal a where "
				+ "not exists (select 1 from VendaAnimal v where v.animal.id = a.id) and "
				+ "not exists (select 1 from MorteAnimal ma where ma.animal.id = a.id) and "
				+ "exists (select 1 from Cobertura c where c.femea.id = a.id ) ");
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Animal> findAnimaisComParto() {
		Query query = entityManager.createQuery("SELECT a FROM Animal a where "
				+ "not exists (select 1 from VendaAnimal v where v.animal.id = a.id) and "
				+ "not exists (select 1 from MorteAnimal ma where ma.animal.id = a.id) and "
				+ "exists (select 1 from Parto p where p.cobertura.femea.id = a.id ) ");
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAnimaisComMaisDeUmParto() {
		
		Query query = entityManager.createQuery("SELECT a FROM Animal a where "
				+ "not exists (select 1 from VendaAnimal v where v.animal.id = a.id) and "
				+ "not exists (select 1 from MorteAnimal ma where ma.animal.id = a.id) "
				+ "group by a having (select count(p) from Parto p where p.cobertura.femea.id = a.id ) > 1 ");
		
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
				+ "(select 1 from lactacao lc where lc.animal = a.id)");
		query.setParameter("dias", dias);
		return (BigInteger) query.getSingleResult();
	}

	public BigInteger countAllFemeasEmLactacao() {
		Object result = entityManager.createNativeQuery(" select count(*) from viewAnimaisAtivos a "
				+ "inner join lactacao lc where lc.animal = a.id and lc.dataFim is null ").getSingleResult();
		
		return (BigInteger) result;
	}

	public BigInteger countAllFemeasSecas() {
		Object result = entityManager.createNativeQuery("select count(*) from viewAnimaisAtivos a "
				+ "WHERE (select lc.dataFim from lactacao lc where lc.animal = a.id order by lc.dataInicio desc limit 1) is not null").getSingleResult();
		return (BigInteger) result;
	}

	public BigInteger countVacasDisponiveisParaCoberturaUltimos21Dias(int diasIdadeMinimaParaCobertura, int periodoVoluntarioEspera) {
		//vacas dispon�veis para serem cobertas:
		//(1) n�o vendidas, (2) n�o mortas, (3) que n�o estejam cobertas(prenhas) no per�odo, (3) n�o s�o rec�m paridas, (4) tem idade suficiente para cobertura
		return (BigInteger) entityManager.createNativeQuery(
				"select count(*) from viewAnimaisAtivos a where DATEDIFF(current_date(), a.dataNascimento) between 0 and " + diasIdadeMinimaParaCobertura + " and "
				+ "not exists (select 1 from cobertura c where c.femea = a.id and DATEDIFF(current_date(), c.data) between 0 and 21 and c.situacaoCobertura in ('" + SituacaoCobertura.PRENHA + "','" + SituacaoCobertura.NAO_CONFIRMADA + "')) and "
				+ "not exists (select 1 from parto p inner join cobertura c on (c.id = p.cobertura) where c.femea = a.id and DATEDIFF(current_date(), p.data) between 0 and " + periodoVoluntarioEspera + ")").getSingleResult();
	}

	public BigInteger countAllNovilhasAtivas() {
		Query query = entityManager.createNativeQuery(
				"select count(*) from viewAnimaisAtivos a " +
				"where a.sexo = '" + Sexo.FEMEA + "' and not exists "
				+ "(select 1 from lactacao lc where lc.animal = a.id)");
		return (BigInteger) query.getSingleResult();
	}
	/*
	 * Vacas dispon�veis para serem cobertas:
	 * (1) n�o vendidas, 
	 * (2) n�o mortas, 
	 * (3) que n�o estejam cobertas(prenhas) no per�odo de 21 dias, 
	 * (3) n�o s�o rec�m paridas,  
	 * (4) tem idade suficiente para cobertura
	 */
	@SuppressWarnings("unchecked")
	public List<Animal> findAnimaisDisponiveisParaCobertura(int diasIdadeMinimaParaCobertura, int periodoVoluntarioEspera) {
		
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a where DATEDIFF(current_date(), a.dataNascimento) > " + diasIdadeMinimaParaCobertura + " and "
				+ "not exists (select 1 from VendaAnimal v where v.animal.id = a.id) and "
				+ "not exists (select 1 from MorteAnimal ma where ma.animal.id = a.id) and "
				+ "not exists (select 1 from Cobertura c where c.femea = a and c.situacaoCobertura in ('" + SituacaoCobertura.PRENHA + "','" + SituacaoCobertura.NAO_CONFIRMADA + "')) and "
				+ "not exists (select 1 from Parto p where p.cobertura.femea = a and DATEDIFF(current_date(), p.data) between 0 and " + periodoVoluntarioEspera + ") and "
				+ "a.sexo = '" + Sexo.FEMEA + "'");
		
		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasEmPeriodoVoluntarioEspera(int periodoVoluntarioEspera) {
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a Where "
				+ "not exists (select 1 from VendaAnimal v where v.animal.id = a.id) and "
				+ "not exists (select 1 from MorteAnimal ma where ma.animal.id = a.id) and "
				+ "exists (select 1 from Parto p where p.cobertura.femea = a and DATEDIFF(current_date(), p.data) between 0 and " + periodoVoluntarioEspera + ")");
		return query.getResultList();
	}

	/*
	 * Esse m�todo � utilizado para filtra os animais n�o cobertos 
	 * dentro do per�odo dos tr�s primeiros ciclos ap�s o parto.
	 */
	@SuppressWarnings("unchecked")
	public List<Animal> findFemeasNaoPrenhasXDiasAposParto(int dias) {
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a Where "
				+ "not exists (select 1 from VendaAnimal v where v.animal.id = a.id) and "
				+ "not exists (select 1 from MorteAnimal ma where ma.animal.id = a.id) and "
				+ "exists (select 1 from Parto p where p.cobertura.femea = a and DATEDIFF(current_date(), p.data) between 0 and " + dias + ") and "
				+ "not exists (select 1 from Cobertura c where c.femea = a and c.situacaoCobertura in ('" + SituacaoCobertura.PRENHA + "'))");
		return query.getResultList();
	}
	/*
	 * Esse m�todo � utilizado para filtra os animais n�o cobertos 
	 * ap�s j� passados os tr�s primeiros ciclos.
	 */
	@SuppressWarnings("unchecked")
	public List<Animal> findFemeasNaoPrenhasAposXDiasAposParto(int dias) {
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a Where "
				+ "not exists (select 1 from VendaAnimal v where v.animal.id = a.id) and "
				+ "not exists (select 1 from MorteAnimal ma where ma.animal.id = a.id) and "
				+ "exists (select 1 from Parto p where p.cobertura.femea = a and DATEDIFF(current_date(), p.data) >= " + dias + ") and "
				+ "not exists (select 1 from Cobertura c where c.femea = a and c.situacaoCobertura in ('" + SituacaoCobertura.PRENHA + "'))");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findFemeasPrenhas() {
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a Where "
				+ "not exists (select 1 from VendaAnimal v where v.animal.id = a.id) and "
				+ "not exists (select 1 from MorteAnimal ma where ma.animal.id = a.id) and "
				+ "exists (select 1 from Cobertura c where  c.femea = a and c.situacaoCobertura = '" + SituacaoCobertura.PRENHA + "')");
		return query.getResultList();
	}

	public BigInteger countDiasLactacao(Animal animal) {
		Query query = entityManager.createNativeQuery(
				"select DATEDIFF(current_date(), l.dataInicio) from Lactacao l where l.animal = :animal order by l.dataInicio desc limit 1");
		query.setParameter("animal", animal);
		return (BigInteger) query.getSingleResult();
	}

}