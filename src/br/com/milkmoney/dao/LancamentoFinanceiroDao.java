package br.com.milkmoney.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milkmoney.model.LancamentoFinanceiro;
import br.com.milkmoney.model.SaldoCategoriaDespesa;
import br.com.milkmoney.model.TipoLancamentoFinanceiro;

@Repository
public class LancamentoFinanceiroDao extends AbstractGenericDao<Integer, LancamentoFinanceiro> {

	@SuppressWarnings("unchecked")
	public List<LancamentoFinanceiro> defaultSearch(String param) {
		
		Query query = entityManager.createQuery("SELECT r FROM LancamentoFinanceiro r WHERE r.descricao like :param or "
				+ "r.tipoLancamento like :param or "
				+ "r.categoria.descricao like :param or "
				+ "r.centroCusto.descricao like :param");
		query.setParameter("param", '%' + param + '%');
		
		return query.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<LancamentoFinanceiro> findByMes(Date dataInicio, Date dataFim) {
		Query query = entityManager.createQuery("SELECT c FROM LancamentoFinanceiro c WHERE c.dataVencimento between :inicio and :fim");
		query.setParameter("inicio", dataInicio);
		query.setParameter("fim", dataFim);
		return query.getResultList();
	}

	public List<SaldoCategoriaDespesa> getSaldoByCategoriaDespesa(Date dataInicio, Date dataFim) {
		
		List<SaldoCategoriaDespesa> list = new ArrayList<SaldoCategoriaDespesa>();
		
		Query query = entityManager.createQuery(
				"SELECT categoria.descricao, sum(juros + multa + valor) as total "
				+ "FROM LancamentoFinanceiro l "
				+ "WHERE dataVencimento between :inicio and :fim "
				+ "and l.tipoLancamento = '" + TipoLancamentoFinanceiro.DESPESA + "' group by 1 order by 2");
		
		query.setParameter("inicio", dataInicio);
		query.setParameter("fim", dataFim);
		
		@SuppressWarnings("unchecked")
		List<Object> result = query.getResultList();
		
		BigDecimal totalDespesa = BigDecimal.ZERO;
		
		for ( int index = 0; index < result.size(); index++ ){
			if ( result.get(index) != null ){
				Object[] row = (Object[]) result.get(index);
				totalDespesa = totalDespesa.add(BigDecimal.valueOf(Double.valueOf(row[1].toString())));
			}
		}
		
		for ( int index = 0; index < result.size(); index++ ){
			if ( result.get(index) != null ){
				Object[] row = (Object[]) result.get(index);
				
				BigDecimal saldo = BigDecimal.valueOf(Double.valueOf(row[1].toString()));
				BigDecimal percentual = saldo.divide(totalDespesa, RoundingMode.HALF_EVEN)
											 .multiply(BigDecimal.valueOf(100));
				
				list.add(new SaldoCategoriaDespesa(String.valueOf(row[0]), saldo, percentual));
				
			}
		}
		
		return list;
		
	}

	@SuppressWarnings("unchecked")
	public List<LancamentoFinanceiro> findByMes(String param, Date dataInicio, Date dataFim) {
		Query query = entityManager.createQuery("SELECT r FROM LancamentoFinanceiro r WHERE r.dataVencimento between :inicio and :fim and "
				+ "(r.descricao like :param or "
				+ "r.tipoLancamento like :param or "
				+ "r.categoria.descricao like :param or "
				+ "r.centroCusto.descricao like :param)");
		query.setParameter("param", '%' + param + '%');
		query.setParameter("inicio", dataInicio);
		query.setParameter("fim", dataFim);
		
		return query.getResultList();
	}

}