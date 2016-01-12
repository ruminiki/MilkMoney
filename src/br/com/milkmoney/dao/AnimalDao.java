package br.com.milkmoney.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.FinalidadeAnimal;
import br.com.milkmoney.model.Sexo;
import br.com.milkmoney.model.SituacaoAnimal;
import br.com.milkmoney.model.SituacaoCobertura;
import br.com.milkmoney.service.AnimalService;

@Repository
public class AnimalDao extends AbstractGenericDao<Integer, Animal> {
	
	@SuppressWarnings("unchecked")
	public List<Animal> defultSearch(String param) {
		Query query = entityManager.createQuery("SELECT a FROM Animal a "
				+ "WHERE "
				+ "a.nome like :param or a.numero like :param or "
				+ "a.raca.descricao like :param or "
				+ "a.sexo like :param or "
				+ "a.finalidadeAnimal like :param or "
				+ "a.situacaoAnimal like :param");
		query.setParameter("param", '%' + param + '%');
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Animal> superSearch(HashMap<String, String> params) {
		
		StringBuilder SQL = new StringBuilder("SELECT a FROM Animal a WHERE ");
		
		if ( params.get(AnimalService.FILTER_FINALIDADE_ANIMAL) != null ){
			SQL.append("a.finalidadeAnimal = '" + params.get(AnimalService.FILTER_FINALIDADE_ANIMAL) + "' and ");
		}
		
		if ( params.get(AnimalService.FILTER_SITUACAO_ANIMAL) != null ){
			SQL.append("a.situacaoAnimal = '" + params.get(AnimalService.FILTER_SITUACAO_ANIMAL) + "' and ");
		}
		
		if ( params.get(AnimalService.FILTER_SITUACAO_COBERTURA) != null ){
			SQL.append("exists (SELECT 1 FROM Cobertura c where c.situacaoCobertura = '" + params.get(AnimalService.FILTER_SITUACAO_COBERTURA) + "' and c.femea = a ) and ");
		}
		
		if ( params.get(AnimalService.FILTER_LOTE) != null ){
			SQL.append("exists (SELECT 1 FROM Lote l inner join l.animais la where l.ativo = 'SIM' and la.id = a.id and l.id = " + params.get(AnimalService.FILTER_LOTE) + ") and ");
		}

		if ( params.get(AnimalService.FILTER_SEXO) != null ){
			SQL.append("a.sexo = '" + params.get(AnimalService.FILTER_SEXO) + "' and ");
		}
		
		if ( params.get(AnimalService.FILTER_RACA) != null ){
			SQL.append("a.raca.id = " + params.get(AnimalService.FILTER_RACA) + " and ");
		}
		
		if ( params.get(AnimalService.FILTER_IDADE_DE) != null && params.get(AnimalService.FILTER_IDADE_ATE) != null ){
			SQL.append("(DATEDIFF(current_date(), a.dataNascimento)/30) between " + params.get(AnimalService.FILTER_IDADE_DE) + " and " + params.get(AnimalService.FILTER_IDADE_ATE) + " and ");
		}
		
		if ( params.get(AnimalService.FILTER_DIAS_POS_PARTO) != null ){
			if ( !params.get(AnimalService.FILTER_DIAS_POS_PARTO).matches(">=.*|<=.*|<.*|>.*|=.*") ){
				params.put(AnimalService.FILTER_DIAS_POS_PARTO, "= " + params.get(AnimalService.FILTER_DIAS_POS_PARTO).replaceAll("[^0-9]", ""));
			}
			SQL.append("exists (select 1 from Parto p where p.cobertura.femea = a) and ");
			SQL.append("DATEDIFF(current_date(), (select max(p1.data) from Parto p1 where p1.cobertura.femea = a)) " + params.get(AnimalService.FILTER_DIAS_POS_PARTO) + " and ");
		}
		
		if ( params.get(AnimalService.FILTER_DIAS_POS_COBERTURA) != null ){
			if ( !params.get(AnimalService.FILTER_DIAS_POS_COBERTURA).matches(">=.*|<=.*|<.*|>.*|=.*") ){
				params.put(AnimalService.FILTER_DIAS_POS_COBERTURA, "= " + params.get(AnimalService.FILTER_DIAS_POS_COBERTURA).replaceAll("[^0-9]", ""));
			}
			SQL.append("exists (select 1 from Cobertura c where c.femea = a) and ");
			SQL.append("DATEDIFF(current_date(), (select max(c1.data) from Cobertura c1 where c1.femea = a)) " + params.get(AnimalService.FILTER_DIAS_POS_COBERTURA) + " and ");
		}
		
		if ( params.get(AnimalService.FILTER_NUMERO_PARTOS) != null ){
			if ( !params.get(AnimalService.FILTER_NUMERO_PARTOS).matches(">=.*|<=.*|<.*|>.*|=.*") ){
				params.put(AnimalService.FILTER_NUMERO_PARTOS, "= " + params.get(AnimalService.FILTER_NUMERO_PARTOS).replaceAll("[^0-9]", ""));
			}
			SQL.append("(select count(*) from Parto p where p.cobertura.femea = a) " + params.get(AnimalService.FILTER_NUMERO_PARTOS) + " and ");
		}
		
		if ( params.get(AnimalService.FILTER_NAO_COBERTAS_X_DIAS_APOS_PARTO) != null ){
			if ( !params.get(AnimalService.FILTER_NAO_COBERTAS_X_DIAS_APOS_PARTO).matches(">=.*|<=.*|<.*|>.*|=.*") ){
				params.put(AnimalService.FILTER_NAO_COBERTAS_X_DIAS_APOS_PARTO, "= " + params.get(AnimalService.FILTER_NAO_COBERTAS_X_DIAS_APOS_PARTO).replaceAll("[^0-9]", ""));
			}
			SQL.append("not exists (select 1 from Cobertura c where c.femea = a and DATEDIFF(current_date(), c.data) " + params.get(AnimalService.FILTER_NAO_COBERTAS_X_DIAS_APOS_PARTO) + " and c.situacaoCobertura in ('" + SituacaoCobertura.PRENHA + "','" + SituacaoCobertura.NAO_CONFIRMADA + "')) and ");
		}
		
		if ( params.get(AnimalService.FILTER_SECAR_EM_X_DIAS) != null ){
			if ( !params.get(AnimalService.FILTER_SECAR_EM_X_DIAS).matches(">=.*|<=.*|<.*|>.*|=.*") ){
				params.put(AnimalService.FILTER_SECAR_EM_X_DIAS, "= " + params.get(AnimalService.FILTER_SECAR_EM_X_DIAS).replaceAll("[^0-9]", ""));
			}
			SQL.append("exists (select 1 from Lactacao l where l.animal = a) and ");
			SQL.append("exists (SELECT 1 FROM Lactacao lc WHERE lc.animal = a and (DATEDIFF(current_date(), lc.dataInicio) + " + params.get(AnimalService.FILTER_SECAR_EM_X_DIAS) + ") >= 305 and lc.dataFim is null) and ");
		}
		
		if ( params.get(AnimalService.FILTER_SITUACAO_ANIMAL) != null && !params.get(AnimalService.FILTER_SITUACAO_ANIMAL).equals(SituacaoAnimal.MORTO) ){
			SQL.append("not exists (SELECT 1 FROM MorteAnimal m WHERE m.animal = a) and ");
		}
			
		if ( params.get(AnimalService.FILTER_SITUACAO_ANIMAL) != null && !params.get(AnimalService.FILTER_SITUACAO_ANIMAL).equals(SituacaoAnimal.VENDIDO) ){
			SQL.append("not exists (SELECT 1 FROM VendaAnimal v WHERE v.animal = a) ");
		}
		
		if ( SQL.toString().toLowerCase().endsWith("and ") ){
			SQL.setLength(SQL.length() - 4);
		}
		
		if ( SQL.toString().toLowerCase().endsWith("where ") ){
			SQL.setLength(SQL.length() - 6);
		} 
		
		Query query = entityManager.createQuery(SQL.toString());
		return query.getResultList();
	}
	
	
	
	public Animal findByNumero(String numero) {
		Query query = entityManager.createQuery("SELECT a FROM Animal a WHERE a.numero = :numero ");
		query.setParameter("numero", numero);
		
		try{
			return (Animal)query.getSingleResult();
		}catch ( NoResultException e ){
			return null;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Animal> findAllByNumeroNome(String param) {
		Query query = entityManager.createQuery("SELECT a FROM Animal a WHERE a.nome like :param or a.numero like :param");
		query.setParameter("param", '%' + param + '%');
		query.setMaxResults(15);
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Animal> findFemeasByNumeroNome(String param) {
		Query query = entityManager.createQuery("SELECT a FROM Animal a WHERE a.nome like :param or a.numero like :param and a.sexo = '" + Sexo.FEMEA + "'");
		query.setParameter("param", '%' + param + '%');
		query.setMaxResults(15);
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasAtivas() {
		
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a WHERE a.sexo = 'FÊMEA' "
						+ "and not exists (SELECT 1 FROM VendaAnimal v WHERE v.animal.id = a.id) "
						+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");
		return query.getResultList();

	}
	
	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeas() {
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a WHERE a.sexo = '" + Sexo.FEMEA + "' ");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllReprodutoresAtivos() {
		
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a WHERE a.sexo = '" + Sexo.MACHO + "' and a.finalidadeAnimal = '" + FinalidadeAnimal.REPRODUCAO + "' "
						+ "and not exists (SELECT 1 FROM VendaAnimal v WHERE v.animal.id = a.id) "
						+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");
		return query.getResultList();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Animal> findAllMachos() {
		
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a WHERE a.sexo = '" + Sexo.MACHO + "' ");
		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasCobertas() {
		
		Query query = entityManager.createQuery(
				"SELECT distinct a FROM Cobertura c inner join c.femea a "
				+ "WHERE c.situacaoCobertura in ('" + SituacaoCobertura.NAO_CONFIRMADA + "','" + SituacaoCobertura.PRENHA + "') "
				+ "and not exists (SELECT 1 FROM VendaAnimal v WHERE v.animal.id = a.id) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");
		return query.getResultList();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasCobertasNaoConfirmadas() {
		
		Query query = entityManager.createQuery(
				"SELECT distinct a FROM Cobertura c inner join c.femea a "
				+ "WHERE c.situacaoCobertura in ('" + SituacaoCobertura.NAO_CONFIRMADA + "') "
				+ "and not exists (SELECT 1 FROM VendaAnimal v WHERE v.animal.id = a.id) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");
		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasNaoCobertas() {
		
		Query query = entityManager.createQuery(
				"SELECT distinct a FROM Animal a WHERE a.sexo = '" + Sexo.FEMEA + "' and "
				+ "not exists (SELECT 1 FROM Cobertura c where c.situacaoCobertura in ('" + SituacaoCobertura.NAO_CONFIRMADA + "','" + SituacaoCobertura.PRENHA + "') and c.femea = a.id ) "
				+ "and not exists (SELECT 1 FROM VendaAnimal v WHERE v.animal.id = a.id) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");
		
		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasLactacaoXDias(Integer dias) {
		
		Query query = entityManager.createQuery("SELECT a FROM Lactacao lc inner join lc.animal a "
						+ "WHERE lc.dataFim is null and DATEDIFF(current_date(), lc.dataInicio) between 0 and :dias "
						+ "and not exists (SELECT 1 FROM VendaAnimal v WHERE v.animal.id = a.id) "
						+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");
		query.setParameter("dias", dias);
		
		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasASecar() {
		
		Query query = entityManager.createQuery("SELECT a FROM Animal a "
				+ "WHERE exists (SELECT 1 FROM Lactacao lc WHERE lc.animal = a and DATEDIFF(current_date(), lc.dataInicio) >= 305 and lc.dataFim is null) "
				+ "and not exists (SELECT 1 FROM VendaAnimal v WHERE v.animal.id = a.id) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");
		return query.getResultList();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasSecas() {
		
		Query query = entityManager.createQuery("SELECT a FROM Animal a "
				+ "WHERE not exists (SELECT 1 FROM Lactacao lc where lc.animal = a and lc.dataFim is null) "
				+ "and exists (SELECT 1 FROM Parto p where p.cobertura.femea = a) "
				+ "and not exists (SELECT 1 FROM VendaAnimal v WHERE v.animal.id = a.id) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");
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
		query.setParameter("dias", dias);
		
		return query.getResultList();
	}

	/*
	 *Recupera todos os animais em lactação em uma determinada data.
	 *Se não for passado parâmetro, considera a data atual.
	 */
	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasEmLactacao(Object ...params) {
		
		Date   data   = (params != null && params.length > 0) ? (Date)   params[0] : new Date();

		Query query = entityManager.createQuery(
				"SELECT a FROM Lactacao lc inner join lc.animal a WHERE :data between lc.dataInicio and coalesce(lc.dataFim, current_date()) "
				+ "and not exists (SELECT 1 FROM VendaAnimal v WHERE v.animal.id = a.id and v.dataVenda <= :data) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id and m.dataMorte <= :data) ");
		query.setParameter("data", data);
		return query.getResultList();
		
	}
	
	/*
	 *Identifica se o animal está em lactação em uma determinada data.
	 */
	public Boolean isInLactacao(Date data, Animal animal) {
		
		Query query = entityManager.createQuery(
				"SELECT a FROM Lactacao lc inner join lc.animal a WHERE lc.animal = :animal and :data between lc.dataInicio and coalesce(lc.dataFim, current_date()) "
				+ "and not exists (SELECT 1 FROM VendaAnimal v WHERE v.animal.id = a.id and v.dataVenda <= :data) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id and m.dataMorte <= :data) ");
		query.setParameter("data", data);
		query.setParameter("animal", animal);
		
		try{
			return query.getSingleResult() != null;
		}catch ( NoResultException e ){
			return false;
		}
		
	}
	
	/*
	 * Conta quantos animais estão em lactação em uma determinada data.
	 * Utilizado para popular o campo animais ordenhados, na tela de produção diária.
	 * 
	 */
	public Long contaAnimaisEmLactacao(Date data) {
		
		Query query = entityManager.createQuery(
				"SELECT count(a) FROM Lactacao lc inner join lc.animal a WHERE :data between lc.dataInicio and coalesce(lc.dataFim, current_date()) "
				+ "and not exists (SELECT 1 FROM VendaAnimal v WHERE v.animal.id = a.id and v.dataVenda <= :data) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id and m.dataMorte <= :data) ");
		query.setParameter("data", data);
		
		try{
			return (Long) query.getSingleResult();
		}catch ( NoResultException e ){
			return 0L;
		}
		
	}

	public Long contaAnimaisSecos(Date data) {
		
		Query query = entityManager.createQuery(
				"SELECT count(a) FROM Animal a "
				//deve existir uma lactação encerrando antes da data
				+ "WHERE exists (select 1 from Lactacao l1 where l1.animal = a and l1.dataFim != null and l1.dataFim < :data) "
				//e não deve existir nenhuma lactação na data
				+ "and not exists (select 1 from Lactacao l2 where l2.animal = a and :data between l2.dataInicio and coalesce(l2.dataFim, current_date())) "
				+ "and not exists (SELECT 1 FROM VendaAnimal v WHERE v.animal.id = a.id and v.dataVenda <= :data) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id and m.dataMorte <= :data) ");
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
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Animal> findAllAnimaisVendidos() {
		Query query = entityManager.createQuery("SELECT a FROM VendaAnimal v inner join v.animal a");
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
	
	public BigInteger countAllNovilhasIdadeAcimaXMeses(int meses, Date data) {
		int dias = meses * 30;
		Query query = entityManager.createNativeQuery(
				"select count(*) from viewAnimaisAtivos a " +
				"where DATEDIFF(:data, a.dataNascimento) > :dias  " +
				"and a.sexo = '" + Sexo.FEMEA + "' and not exists "
				+ "(select 1 from lactacao lc where lc.animal = a.id)");
		query.setParameter("dias", dias);
		query.setParameter("data", data);
		return (BigInteger) query.getSingleResult();
	}
	
	public BigInteger countAllNovilhasIdadeAteXMeses(int meses, Date data) {
		
		int dias = meses * 30;
		Query query = entityManager.createNativeQuery(
				"select count(*) from viewAnimaisAtivos a " +
				"where DATEDIFF(:data, a.dataNascimento) <= :dias  " +
				"and a.sexo = '" + Sexo.FEMEA + "' and not exists "
				+ "(select 1 from cobertura c inner join parto p on (p.cobertura = c.id) where c.femea = a.id)");
		query.setParameter("dias", dias);
		query.setParameter("data", data);
		
		return (BigInteger) query.getSingleResult();
		
	}
	
	public BigInteger countAllNovilhasIdadeEntreMeses(int mesesIdadeMinima, int mesesIdadeMaxima, Date data) {
		int diasIdadeMinima = mesesIdadeMinima * 30;
		int diasIdadeMaxima = mesesIdadeMaxima * 30;
		Query query = entityManager.createNativeQuery(
				"select count(*) from viewAnimaisAtivos a " +
				"where DATEDIFF(:data, a.dataNascimento) between :min and :max " +
				"and a.sexo = '" + Sexo.FEMEA + "' and not exists "
				+ "(select 1 from cobertura c inner join parto p on (p.cobertura = c.id) where c.femea = a.id)");
		query.setParameter("min", diasIdadeMinima);
		query.setParameter("max", diasIdadeMaxima);
		query.setParameter("data", data);
		
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
		//vacas disponíveis para serem cobertas:
		//(1) não vendidas, (2) não mortas, (3) que não estejam cobertas(prenhas) no período, (3) não são recém paridas, (4) tem idade suficiente para cobertura
		return (BigInteger) entityManager.createNativeQuery(
				"select count(*) from viewAnimaisAtivos a where DATEDIFF(current_date(), a.dataNascimento) between 0 and " + diasIdadeMinimaParaCobertura + " and "
				+ "not exists (select 1 from cobertura c where c.femea = a.id and DATEDIFF(current_date(), c.data) between 0 and 21 and c.situacaoCobertura in ('" + SituacaoCobertura.PRENHA + "','" + SituacaoCobertura.NAO_CONFIRMADA + "')) and "
				+ "not exists (select 1 from parto p inner join cobertura c on (c.id = p.cobertura) where c.femea = a.id and DATEDIFF(current_date(), p.data) between 0 and " + periodoVoluntarioEspera + ")").getSingleResult();
	}

	public BigInteger countAllAnimaisSemLactacao() {
		Query query = entityManager.createNativeQuery(
				"select count(*) from viewAnimaisAtivos a " +
				"where a.sexo = '" + Sexo.FEMEA + "' and not exists "
				+ "(select 1 from lactacao lc where lc.animal = a.id)");
		return (BigInteger) query.getSingleResult();
	}
	
	/*
	 * Vacas disponíveis para serem cobertas:
	 * (1) não vendidas, 
	 * (2) não mortas, 
	 * (3) que não estejam cobertas(prenhas) no período de 21 dias, 
	 * (3) não são recém paridas,  
	 * (4) tem idade suficiente para cobertura
	 */
	@SuppressWarnings("unchecked")
	public List<Animal> findAnimaisAtivosComIdadeParaServicoNoPeriodo(int diasIdadeMinimaParaCobertura, Date dataInicio, Date dataFim) {
				
		//busca animais que tinha idade suficiente e não estavam mortos nem vendidos
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a where DATEDIFF(:dataFim, a.dataNascimento) > " + diasIdadeMinimaParaCobertura + " and "
				+ "not exists (select 1 from VendaAnimal v where v.animal.id = a.id and v.dataVenda <= :dataInicio) and "
				+ "not exists (select 1 from MorteAnimal ma where ma.animal.id = a.id and ma.dataMorte <= :dataInicio) and "
				+ "a.sexo = '" + Sexo.FEMEA + "'");
		query.setParameter("dataInicio", dataInicio);
		query.setParameter("dataFim", dataFim);
		
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
	 * Esse método é utilizado para filtra os animais não cobertos 
	 * dentro do período dos três primeiros ciclos após o parto.
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
	 * Esse método é utilizado para filtra os animais não cobertos 
	 * após já passados os três primeiros ciclos.
	 */
	@SuppressWarnings("unchecked")
	public List<Animal> findFemeasNaoPrenhasAposXDiasAposParto(int dias) {
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a Where "
				+ "not exists (select 1 from VendaAnimal v where v.animal.id = a.id) and "
				+ "not exists (select 1 from MorteAnimal ma where ma.animal.id = a.id) and "
				+ "exists (select 1 from Parto p where p.cobertura.femea = a and DATEDIFF(current_date(), p.data) >= " + dias + ") and "
				+ "not exists (select 1 from Cobertura c where c.femea = a and c.situacaoCobertura in ('" + SituacaoCobertura.PRENHA + "', '" + SituacaoCobertura.NAO_CONFIRMADA + "'))");
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


	@SuppressWarnings("unchecked")
	public List<Animal> findFemeasCobertasSemParto() {
		Query query = entityManager.createQuery("SELECT a FROM Animal a where "
				+ "not exists (select 1 from VendaAnimal v where v.animal.id = a.id) and "
				+ "not exists (select 1 from MorteAnimal ma where ma.animal.id = a.id) and "
				+ "exists (select 1 from Cobertura c where c.femea.id = a.id ) and "
				+ "not exists (select 1 from Parto p where p.cobertura.femea.id = a.id )");
		
		return query.getResultList();
	}
	/**
	 * Seleciona os animais que compõem o cálculo da eficiência reprodutiva
	 * Fêmeas com partos ou coberturas no período
	 * Fêmeas que já tinham partos ou coberturas e foram eliminadas no período
	 * 
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Animal> findAnimaisParaCalculoEficiencia(Date dataInicio, Date dataFim) {
		
		Query query = entityManager.createQuery("SELECT a FROM Animal a where "
				+ "("
				+ "exists (select 1 from Cobertura c where c.femea.id = a.id and c.data between :dataInicio and :dataFim ) or "
				+ "exists (select 1 from Parto p where p.cobertura.femea.id = a.id and p.data between :dataInicio and :dataFim )"
				+ ") "
				+ "or"
				+ "(exists (select 1 from Cobertura c where c.femea.id = a.id and c.data < :dataInicio ) and "
				+ "(exists (select 1 from VendaAnimal v where v.animal.id = a.id and v.dataVenda between :dataInicio and :dataFim ) or "
				+ "exists (select 1 from MorteAnimal m where m.animal.id = a.id and m.dataMorte between :dataInicio and :dataFim )))");
		query.setParameter("dataInicio", dataInicio);
		query.setParameter("dataFim", dataFim);
		return query.getResultList();

	}

	public Animal findMae(Animal animal) {
		
		Query query = entityManager.createQuery("select a.mae from Animal a where a.id = :id");
		query.setParameter("id", animal.getId());
		
		try{
			return (Animal) query.getSingleResult();
		}catch ( NoResultException e ){
			return null;
		}
		
	}
	
	public Object findPai(Animal animal) {
		
		Object pai  = null;
		Query query = null;
		
		try{
			query = entityManager.createQuery("select a.paiEnseminacaoArtificial from Animal a where a.id = :id");
			query.setParameter("id", animal.getId());
			
			pai = query.getSingleResult();
			return pai;
		}catch ( NoResultException e ){
			
			query = entityManager.createQuery("select a.paiMontaNatural from Animal a where a.id = :id");
			query.setParameter("id", animal.getId());
			
			try{
				pai = query.getSingleResult();
				return pai;
			}catch ( NoResultException e1 ){
				return null;
			}
		}
		
	}

}