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
import br.com.milkmoney.model.Limit;
import br.com.milkmoney.model.Sexo;
import br.com.milkmoney.model.SimNao;
import br.com.milkmoney.model.SituacaoAnimal;
import br.com.milkmoney.model.SituacaoCobertura;
import br.com.milkmoney.service.AnimalService;

@Repository
public class AnimalDao extends AbstractGenericDao<Integer, Animal> {
	
	@SuppressWarnings("unchecked")
	public List<Animal> defaultSearch(String param, int limit) {
		Query query = entityManager.createQuery("SELECT a FROM Animal a "
				+ "WHERE "
				+ "a.nome like :param or a.numero like :param or "
				+ "a.raca.descricao like :param or "
				+ "a.sexo like :param or "
				+ "a.finalidadeAnimal like :param or "
				+ "a.situacaoAnimal like :param");
		query.setParameter("param", '%' + param + '%');
		if ( limit != Limit.UNLIMITED ) query.setMaxResults(limit);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Animal> superSearch(HashMap<String, String> params, int limit) {
		
		StringBuilder SQL = new StringBuilder("SELECT a FROM Animal a WHERE ");
		
		if ( params.get(AnimalService.FILTER_FINALIDADE_ANIMAL) != null ){
			SQL.append("a.finalidadeAnimal = '" + params.get(AnimalService.FILTER_FINALIDADE_ANIMAL) + "' and ");
		}
		
		if ( params.get(AnimalService.FILTER_SITUACAO_ANIMAL) != null ){
			SQL.append("a.situacaoAnimal = '" + params.get(AnimalService.FILTER_SITUACAO_ANIMAL) + "' and ");
		}
		
		if ( params.get(AnimalService.FILTER_SITUACAO_COBERTURA) != null ){
			//SQL.append("exists (SELECT 1 FROM Cobertura c where c.situacaoCobertura = '" + params.get(AnimalService.FILTER_SITUACAO_COBERTURA) + "' and c.femea = a order by c.data desc limit 1) and ");
			SQL.append("a.situacaoUltimaCobertura = '" + params.get(AnimalService.FILTER_SITUACAO_COBERTURA) + "' and ");
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
			SQL.append("TIMESTAMPDIFF(MONTH, a.dataNascimento, current_date()) between " + params.get(AnimalService.FILTER_IDADE_DE) + " and " + params.get(AnimalService.FILTER_IDADE_ATE) + " and ");
		}
		
		if ( params.get(AnimalService.FILTER_DIAS_POS_PARTO) != null ){
			params.put(AnimalService.FILTER_DIAS_POS_PARTO, params.get(AnimalService.FILTER_DIAS_POS_PARTO).replaceAll(" ", ""));
			if ( !params.get(AnimalService.FILTER_DIAS_POS_PARTO).matches("(?i)>=[0-9]+|<=[0-9]+|=[0-9]+|<[0-9]+|>[0-9]+") ){
				params.put(AnimalService.FILTER_DIAS_POS_PARTO, "= " + params.get(AnimalService.FILTER_DIAS_POS_PARTO).replaceAll("[^0-9]", ""));
			}
			SQL.append("exists (select 1 from Parto p where p.cobertura.femea = a) and ");
			SQL.append("DATEDIFF(current_date(), (select max(p1.data) from Parto p1 where p1.cobertura.femea = a)) " + params.get(AnimalService.FILTER_DIAS_POS_PARTO) + " and ");
		}
		
		if ( params.get(AnimalService.FILTER_DIAS_POS_COBERTURA) != null ){
			params.put(AnimalService.FILTER_DIAS_POS_COBERTURA, params.get(AnimalService.FILTER_DIAS_POS_COBERTURA).replaceAll(" ", ""));
			if ( !params.get(AnimalService.FILTER_DIAS_POS_COBERTURA).matches("(?i)>=[0-9]+|<=[0-9]+|=[0-9]+|<[0-9]+|>[0-9]+") ){
				params.put(AnimalService.FILTER_DIAS_POS_COBERTURA, "= " + params.get(AnimalService.FILTER_DIAS_POS_COBERTURA).replaceAll("[^0-9]", ""));
			}
			SQL.append("exists (select 1 from Cobertura c where c.femea = a) and ");
			SQL.append("DATEDIFF(current_date(), (select max(c1.data) from Cobertura c1 where c1.femea = a)) " + params.get(AnimalService.FILTER_DIAS_POS_COBERTURA) + " and ");
		}
		
		if ( params.get(AnimalService.FILTER_NUMERO_PARTOS) != null ){
			params.put(AnimalService.FILTER_NUMERO_PARTOS, params.get(AnimalService.FILTER_NUMERO_PARTOS).replaceAll(" ", ""));
			if ( !params.get(AnimalService.FILTER_NUMERO_PARTOS).matches("(?i)>=[0-9]+|<=[0-9]+|=[0-9]+|<[0-9]+|>[0-9]+") ){
				params.put(AnimalService.FILTER_NUMERO_PARTOS, "= " + params.get(AnimalService.FILTER_NUMERO_PARTOS).replaceAll("[^0-9]", ""));
			}
			SQL.append("(select count(*) from Parto p where p.cobertura.femea = a) " + params.get(AnimalService.FILTER_NUMERO_PARTOS) + " and ");
		}
		
		if ( params.get(AnimalService.FILTER_EFICIENCIA_REPRODUTIVA) != null ){
			params.put(AnimalService.FILTER_EFICIENCIA_REPRODUTIVA, params.get(AnimalService.FILTER_EFICIENCIA_REPRODUTIVA).replaceAll(" ", ""));
			if ( !params.get(AnimalService.FILTER_EFICIENCIA_REPRODUTIVA).matches("(?i)>=[0-9]+|<=[0-9]+|=[0-9]+|<[0-9]+|>[0-9]+") ){
				params.put(AnimalService.FILTER_EFICIENCIA_REPRODUTIVA, "= " + params.get(AnimalService.FILTER_EFICIENCIA_REPRODUTIVA).replaceAll("[^0-9]", ""));
			}
			SQL.append("exists (select 1 from FichaAnimal f where f.animal = a and ");
			SQL.append("f.eficienciaReprodutiva " + params.get(AnimalService.FILTER_EFICIENCIA_REPRODUTIVA) + ") and ");
		}
		
		if ( params.get(AnimalService.FILTER_COBERTAS) != null ){
			if ( params.get(AnimalService.FILTER_COBERTAS).equals(SimNao.NAO) ){
				SQL.append("not ");
			}
			SQL.append("exists (select 1 from Cobertura c where c.femea = a and c.situacaoCobertura in ('" + SituacaoCobertura.PRENHA + "','" + SituacaoCobertura.NAO_CONFIRMADA + "')) and ");
		}
		
		if ( params.get(AnimalService.FILTER_SECAR_EM_X_DIAS) != null ){
			params.put(AnimalService.FILTER_SECAR_EM_X_DIAS, params.get(AnimalService.FILTER_SECAR_EM_X_DIAS).replaceAll("[^0-9]", ""));
			//SQL.append("exists (select 1 from Lactacao l where l.animal = a and l.dataFim is null) and ");
			SQL.append("exists (SELECT 1 FROM Lactacao lc WHERE lc.animal = a and ADDDATE(lc.dataInicio, 305) <= ADDDATE(current_date(), " + params.get(AnimalService.FILTER_SECAR_EM_X_DIAS) + ") and lc.dataFim is null) and ");
		}
		
		if ( params.get(AnimalService.FILTER_SITUACAO_ANIMAL) != null && !params.get(AnimalService.FILTER_SITUACAO_ANIMAL).equals(SituacaoAnimal.MORTO) ){
			SQL.append("not exists (SELECT 1 FROM MorteAnimal m WHERE m.animal = a) and ");
		}
			
		if ( params.get(AnimalService.FILTER_SITUACAO_ANIMAL) != null && !params.get(AnimalService.FILTER_SITUACAO_ANIMAL).equals(SituacaoAnimal.VENDIDO) ){
			SQL.append("not exists (SELECT 1 FROM VendaAnimal v WHERE v.animal = a) and ");
		}
		
		if ( params.get(AnimalService.FILTER_NUMERO_SERVICOS) != null ){
			params.put(AnimalService.FILTER_NUMERO_SERVICOS, params.get(AnimalService.FILTER_NUMERO_SERVICOS).replaceAll(" ", ""));
			if ( !params.get(AnimalService.FILTER_NUMERO_SERVICOS).matches("(?i)>=[0-9]+|<=[0-9]+|=[0-9]+|<[0-9]+|>[0-9]+") ){
				params.put(AnimalService.FILTER_NUMERO_SERVICOS, "= " + params.get(AnimalService.FILTER_NUMERO_SERVICOS).replaceAll("[^0-9]", ""));
			}
			SQL.append("exists (select 1 from FichaAnimal f where f.animal = a and ");
			SQL.append("f.numeroServicosAtePrenhez >= " + params.get(AnimalService.FILTER_NUMERO_SERVICOS + ")"));
		}
		
		if ( SQL.toString().toLowerCase().endsWith("and ") ){
			SQL.setLength(SQL.length() - 4);
		}
		
		if ( SQL.toString().toLowerCase().endsWith("where ") ){
			SQL.setLength(SQL.length() - 6);
		} 
		
		Query query = entityManager.createQuery(SQL.toString());
		if ( limit != Limit.UNLIMITED ) query.setMaxResults(limit);
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
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Animal> findFemeasByNumeroNome(String param) {
		Query query = entityManager.createQuery("SELECT a FROM Animal a WHERE a.nome like :param or a.numero like :param and a.sexo = '" + Sexo.FEMEA + "'");
		query.setParameter("param", '%' + param + '%');
		return query.getResultList();
	}

	/**
	 * Todas as fêmeas, bezerras, vacas e novilhas.
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasAtivas(Date data, int limit) {
		
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a WHERE a.sexo = 'FÊMEA' "
						+ "and not exists (SELECT 1 FROM VendaAnimal v WHERE v.animal.id = a.id and v.dataVenda <= :data) "
						+ "and not exists (SELECT 1 FROM MorteAnimal m WHERE m.animal.id = a.id and m.dataMorte <= :data)");
		
		query.setParameter("data", data);
		if ( limit != Limit.UNLIMITED ) query.setMaxResults(limit);
		return query.getResultList();

	}
	
	/**
	 * Localiza fêmeas com partos.
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Animal> findAllVacasAtivas(Date data, int limit) {
		
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a WHERE a.sexo = 'FÊMEA' "
						+ "and not exists (SELECT 1 FROM VendaAnimal v WHERE v.animal.id = a.id and v.dataVenda <= :data) "
						+ "and not exists (SELECT 1 FROM MorteAnimal m WHERE m.animal.id = a.id and m.dataMorte <= :data)"
						+ "and exists (select 1 from Parto p where p.cobertura.femea.id = a.id and p.data <= :data)");
		
		query.setParameter("data", data);
		if ( limit != Limit.UNLIMITED ) query.setMaxResults(limit);
		return query.getResultList();

	}
	
	
	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeas(int limit) {
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a WHERE a.sexo = '" + Sexo.FEMEA + "' ");
		if ( limit != Limit.UNLIMITED ) query.setMaxResults(limit);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllReprodutoresAtivos(int limit) {
		
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a WHERE a.sexo = '" + Sexo.MACHO + "' and a.finalidadeAnimal = '" + FinalidadeAnimal.REPRODUCAO + "' "
						+ "and not exists (SELECT 1 FROM VendaAnimal v WHERE v.animal.id = a.id) "
						+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id)");
		
		if ( limit != Limit.UNLIMITED ) query.setMaxResults(limit);
		return query.getResultList();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Animal> findAllMachos(int limit) {
		
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a WHERE a.sexo = '" + Sexo.MACHO + "' ");
		
		if ( limit != Limit.UNLIMITED ) query.setMaxResults(limit);
		
		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasCobertas(Date data, int limit) {
		
		Query query = entityManager.createQuery(
				"SELECT distinct a FROM Cobertura c inner join c.femea a "
				+ "left join c.parto p on p.data > :data "
				+ "left join c.aborto ab on ab.data > :data "
				+ "WHERE c.data <= :data and "
				+ "not exists (SELECT 1 FROM VendaAnimal v WHERE v.animal.id = a.id and v.dataVenda <= :data) and "
				+ "not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id and m.dataMorte <= :data)");
		
		query.setParameter("data", data);
		if ( limit != Limit.UNLIMITED ) query.setMaxResults(limit);
		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAllFemeasEmLactacao(Date data, int limit) {
		
		Query query = entityManager.createQuery(
				"SELECT a FROM Lactacao lc inner join lc.animal a WHERE :data between lc.dataInicio and coalesce(lc.dataFim, current_date()) "
				+ "and not exists (SELECT 1 FROM VendaAnimal v WHERE v.animal.id = a.id and v.dataVenda <= :data) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id and m.dataMorte <= :data) ");
		query.setParameter("data", data);
		if ( limit != Limit.UNLIMITED ) query.setMaxResults(limit);
		
		return query.getResultList();
		
	}
	
	public Boolean isInLactacao(Date data, Animal animal, int limit) {
		
		Query query = entityManager.createQuery(
				"SELECT a FROM Lactacao lc inner join lc.animal a WHERE lc.animal = :animal and :data between lc.dataInicio and coalesce(lc.dataFim, current_date()) "
				+ "and not exists (SELECT 1 FROM VendaAnimal v WHERE v.animal.id = a.id and v.dataVenda <= :data) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id and m.dataMorte <= :data) ");
		query.setParameter("data", data);
		query.setParameter("animal", animal);
		if ( limit != Limit.UNLIMITED ) query.setMaxResults(limit);
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
	public BigInteger countAllFemeasEmLactacao(Date data) {
		
		Query query = entityManager.createQuery(
				"SELECT count(distinct lc.animal) FROM Lactacao lc "
				+ "inner join lc.animal a WHERE :data between lc.dataInicio and coalesce(lc.dataFim, current_date()) "
				+ "and not exists (SELECT 1 FROM VendaAnimal v WHERE v.animal.id = a.id and v.dataVenda <= :data) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m WHERE m.animal.id = a.id and m.dataMorte <= :data) ");
		query.setParameter("data", data);
		
		try{
			return BigInteger.valueOf((Long)query.getSingleResult());
		}catch ( NoResultException e ){
			return BigInteger.ZERO;
		}
		
	}

	public BigInteger countAllFemeasSecas(Date data) {
		
		Query query = entityManager.createQuery(
				"SELECT count(*) FROM Animal a "
				//deve existir uma lactação encerrando antes da data
				+ "WHERE exists (select 1 from Lactacao l1 where l1.animal = a and l1.dataFim != null and l1.dataFim < :data) "
				//e não deve existir nenhuma lactação na data
				+ "and not exists (select 1 from Lactacao l2 where l2.animal = a and :data between l2.dataInicio and coalesce(l2.dataFim, current_date())) "
				+ "and not exists (SELECT 1 FROM VendaAnimal v WHERE v.animal.id = a.id and v.dataVenda <= :data) "
				+ "and not exists (SELECT 1 FROM MorteAnimal m inner join m.animal am WHERE am.id = a.id and m.dataMorte <= :data) ");
		query.setParameter("data", data);
		
		try{
			return BigInteger.valueOf((Long)query.getSingleResult());
		}catch ( NoResultException e ){
			return BigInteger.ZERO;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Animal> findAnimaisComMaisDeUmParto(Date data, int limit) {
		
		Query query = entityManager.createQuery("SELECT a FROM Animal a where "
				+ "not exists (select 1 from VendaAnimal v where v.animal.id = a.id and v.dataVenda <= :data) and "
				+ "not exists (select 1 from MorteAnimal ma where ma.animal.id = a.id and ma.dataMorte <= :data) "
				+ "group by a having (select count(p) from Parto p where p.cobertura.femea.id = a.id and p.data <= :data ) > 1 ");
		query.setParameter("data", data);
		if ( limit != Limit.UNLIMITED ) query.setMaxResults(limit);
		return query.getResultList();
		
	}

	public BigInteger countAllAtivos(Date data) {
		Query query = entityManager.createQuery(
				"SELECT count(*) FROM Animal a where "
				+ "not exists (select 1 from VendaAnimal v where v.animal.id = a.id and v.dataVenda <= :data) and "
				+ "not exists (select 1 from MorteAnimal ma where ma.animal.id = a.id and ma.dataMorte <= :data)");
		query.setParameter("data", data);
		return BigInteger.valueOf((Long)query.getSingleResult());
	}

	public BigInteger countAllFemeasAtivas(Date data) {
		Query query = entityManager.createQuery(
				"SELECT count(*) FROM Animal a where a.sexo = '" + Sexo.FEMEA + "' and " 
				+ "not exists (select 1 from VendaAnimal v where v.animal.id = a.id and v.dataVenda <= :data) and "
				+ "not exists (select 1 from MorteAnimal ma where ma.animal.id = a.id and ma.dataMorte <= :data)");
		query.setParameter("data", data);
		return BigInteger.valueOf((Long)query.getSingleResult());
	}

	public BigInteger countAllVacasAtivas(Date data) {
		Query query = entityManager.createQuery(
				"SELECT count(*) FROM Animal a where a.sexo = '" + Sexo.FEMEA + "' and "
				+ "not exists (select 1 from VendaAnimal v where v.animal.id = a.id and v.dataVenda <= :data) and "
				+ "not exists (select 1 from MorteAnimal ma where ma.animal.id = a.id and ma.dataMorte <= :data) and "
				+ "exists (select 1 from Parto p where p.cobertura.femea = a and p.data <= :data)");
		query.setParameter("data", data);
		
		return BigInteger.valueOf((Long)query.getSingleResult());
	}

	public BigInteger countAllNovilhasIdadeAcimaXMeses(int meses, Date data) {
		int dias = meses * 30;
		
		Query query = entityManager.createQuery(
				"SELECT count(*) FROM Animal a where a.sexo = '" + Sexo.FEMEA + "' and DATEDIFF(:data, a.dataNascimento) > :dias and "
				+ "not exists (select 1 from VendaAnimal v where v.animal.id = a.id and v.dataVenda <= :data) and "
				+ "not exists (select 1 from MorteAnimal ma where ma.animal.id = a.id and ma.dataMorte <= :data) and "
				+ "not exists (select 1 from Parto p where p.cobertura.femea = a and p.data <= :data) ");
		
		query.setParameter("dias", dias);
		query.setParameter("data", data);

		return BigInteger.valueOf((Long)query.getSingleResult());
	}
	
	public BigInteger countAllNovilhasIdadeAteXMeses(int meses, Date data) {
		
		int dias = meses * 30;
		
		Query query = entityManager.createQuery(
				"SELECT count(*) FROM Animal a where a.sexo = '" + Sexo.FEMEA + "' and DATEDIFF(:data, a.dataNascimento) <= :dias and "
				+ "not exists (select 1 from VendaAnimal v where v.animal.id = a.id and v.dataVenda <= :data) and "
				+ "not exists (select 1 from MorteAnimal ma where ma.animal.id = a.id and ma.dataMorte <= :data) and "
				+ "not exists (select 1 from Parto p where p.cobertura.femea = a and p.data <= :data) ");
		
		query.setParameter("dias", dias);
		query.setParameter("data", data);

		return BigInteger.valueOf((Long)query.getSingleResult());
		
	}
	
	public BigInteger countAllNovilhasIdadeEntreMeses(int mesesIdadeMinima, int mesesIdadeMaxima, Date data) {
		int diasIdadeMinima = mesesIdadeMinima * 30;
		int diasIdadeMaxima = mesesIdadeMaxima * 30;
		
		Query query = entityManager.createQuery(
				"SELECT count(*) FROM Animal a where a.sexo = '" + Sexo.FEMEA + "' and "
				+ "DATEDIFF(:data, a.dataNascimento) between :min and :max and "
				+ "not exists (select 1 from VendaAnimal v where v.animal.id = a.id and v.dataVenda <= :data) and "
				+ "not exists (select 1 from MorteAnimal ma where ma.animal.id = a.id and ma.dataMorte <= :data) and "
				+ "not exists (select 1 from Parto p where p.cobertura.femea = a and p.data <= :data) ");
		
		query.setParameter("min", diasIdadeMinima);
		query.setParameter("max", diasIdadeMaxima);
		query.setParameter("data", data);

		return BigInteger.valueOf((Long)query.getSingleResult());
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
	public List<Animal> findAnimaisAtivosComIdadeParaServicoNoPeriodo(int idadeMinimaParaCobertura, Date dataInicio, Date dataFim, int limit) {
				
		//busca animais que tinha idade suficiente e não estavam mortos nem vendidos
		Query query = entityManager.createQuery(
				"SELECT a FROM Animal a where TIMESTAMPDIFF(MONTH, a.dataNascimento, :dataFim) >= " + idadeMinimaParaCobertura + " and "
				+ "not exists (select 1 from VendaAnimal v where v.animal.id = a.id and v.dataVenda <= :dataInicio) and "
				+ "not exists (select 1 from MorteAnimal ma where ma.animal.id = a.id and ma.dataMorte <= :dataInicio) and "
				+ "a.sexo = '" + Sexo.FEMEA + "'");
		query.setParameter("dataInicio", dataInicio);
		query.setParameter("dataFim", dataFim);
		if ( limit != Limit.UNLIMITED ) query.setMaxResults(limit);
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
	public List<Animal> findAnimaisParaCalculoEficiencia(Date dataInicio, Date dataFim, int limit) {
		
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
		if ( limit != Limit.UNLIMITED ) query.setMaxResults(limit);
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

	public String getImagePath(Animal animal) {
		Query query = entityManager.createNativeQuery("select imagem from animal a where a.id = " + animal.getId());
		try{
			return (String) query.getSingleResult();
		}catch ( NoResultException e1 ){
			return null;
		}
	}

}
