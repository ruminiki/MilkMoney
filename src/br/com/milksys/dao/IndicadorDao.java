package br.com.milksys.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.milksys.model.Animal;
import br.com.milksys.model.EncerramentoLactacao;
import br.com.milksys.model.Indicador;
import br.com.milksys.model.MorteAnimal;
import br.com.milksys.model.Parto;
import br.com.milksys.model.Sexo;
import br.com.milksys.model.VendaAnimal;
import br.com.milksys.util.DateUtil;

@Repository
public class IndicadorDao extends AbstractGenericDao<Integer, Indicador> {

	@Override
	public List<Indicador> findAll(Class<Indicador> clazz) {
		
		List<Indicador> indicadores = super.findAll(Indicador.class);

		for ( Indicador indicador : indicadores ){
			
			switch (indicador.getSigla()) {
			case "DEL":
				indicador.setValorApurado(getValorApuradoDiasEmLactacao());
				break;
				
			case "DEA":
				indicador.setValorApurado(getValorApuradoDiasEmAberto());
				break;

			case "IEP":
				indicador.setValorApurado(getValorApuradoIntervaloEntrePartos());
				break;
				
			case "TR":
				indicador.setValorApurado(getValorApuradoTamanhoRebanho());
				break;	
				
			case "FE":
				indicador.setValorApurado(getValorApuradoTotalFemeas());
				break;
				
			case "VC":
				indicador.setValorApurado(getValorApuradoTotalVacas());
				break;
				
			case "NV":
				indicador.setValorApurado(getValorApuradoTotalNovilhas());
				break;
				
			case "NV1":
				indicador.setValorApurado(getValorApuradoTotalNovilhasAteUmAno());
				break;
				
			case "NV2":
				indicador.setValorApurado(getValorApuradoTotalNovilhasMaisUmAno());
				break;
				
			case "VCL":
				indicador.setValorApurado(getValorApuradoTotalVacasLactacao());
				break;
				
			case "VCS":
				indicador.setValorApurado(getValorApuradoTotalVacasSecas());
				break;
				
			case "TP":
				indicador.setValorApurado(getValorApuradoTaxaPrenhez());
				break;
			default:
				break;
			}
			
		}
		
		return indicadores;
		/*for ( Indicador indicador : indicadores ){
			
			if ( indicador.getQuery() != null && !indicador.getQuery().isEmpty()){
				
				Query query = entityManager.createNativeQuery(indicador.getQuery());
				Object value = query.getSingleResult();
				
				if ( value != null ){
					indicador.setValorApurado( value.toString() );
				}else{
					indicador.setValorApurado("--");
				}
				
			}else{
				indicador.setValorApurado("fórmula não cadastrada");
			}
			
			
			return indicadores;
			
			
		}*/
		
		
		
	}
	
	//============DIAS LACTAÇÃO===========
	@SuppressWarnings("unchecked")
	private BigDecimal getValorApuradoDiasEmLactacao(){
		
		Date       dataInicio     = DateUtil.asDate(LocalDate.now().minusYears(1));
		Date       dataFim        = DateUtil.asDate(LocalDate.now());
		BigDecimal diasEmLactacao = BigDecimal.ZERO;
		int        totalPartos    = 0;
		
		//busca todas as fêmeas que já tiveram parto
		Query query = entityManager.createQuery("SELECT a FROM Animal a WHERE a.sexo = '" + Sexo.FEMEA + "' and exists  " +
				"(select 1 from Cobertura c inner join c.parto p where c.femea = a.id)");
		
		List<Animal> femeas = query.getResultList();
		
		for ( Animal femea : femeas ){
			
			//localiza o parto anterior a data de inicio, para verificar se ele se sobrepõe ao período atual
			query = entityManager.createQuery("SELECT p FROM Parto p where p.data <= :dataInicio and p.cobertura.femea = :femea order by p.data desc");
			query.setParameter("dataInicio", dataInicio);
			query.setParameter("femea", femea);
			query.setMaxResults(1);
			
			Parto ultimoPartoAnteriorPeriodo = (Parto) query.getSingleResult();
			
			//localiza o parto anterior a data de inicio, para verificar se ele se sobrepõe ao período atual
			query = entityManager.createQuery("SELECT p FROM Parto p where p.data between :dataInicio and :dataFim and p.cobertura.femea = :femea order by p.data asc");
			query.setParameter("dataInicio", ultimoPartoAnteriorPeriodo != null ? ultimoPartoAnteriorPeriodo.getData() : dataInicio);
			query.setParameter("dataFim", dataFim);
			query.setParameter("femea", femea);
			
			List<Parto> partos = query.getResultList();
			
			for ( Parto  parto : partos ){
				BigDecimal diasLactacaoParto = contaDiasLactacaoParto(
						parto.getData().before(dataInicio) ? dataInicio : parto.getData(), dataFim, parto, femea);
				
				if ( diasLactacaoParto.compareTo(BigDecimal.ZERO) <= 0 ){
					//se retornou zero é porque o ultimo parto não teve encerramento da lactação
					//e o animal não foi vendido nem está morto.
					//Nesse caso utilizar o ultimo dia do ano (para anos passados) ou a data corrente (para o ano atual)
					//para cálculo dos dias em lactação
					if ( new Date().before(dataFim) ){
						diasLactacaoParto = BigDecimal.valueOf(ChronoUnit.DAYS.between(DateUtil.asLocalDate(parto.getData()), LocalDate.now()));
					}else{
						diasLactacaoParto = BigDecimal.valueOf(ChronoUnit.DAYS.between(DateUtil.asLocalDate(parto.getData()), DateUtil.asLocalDate(dataFim)));
					}
				}
				
				diasEmLactacao = diasEmLactacao.add(diasLactacaoParto);
				totalPartos++;
			}
			
		}
		
		if ( diasEmLactacao.compareTo(BigDecimal.ZERO) > 0 && totalPartos > 0 ){
			diasEmLactacao = diasEmLactacao.divide(new BigDecimal(totalPartos), 2, RoundingMode.HALF_UP);
		}
		
		return diasEmLactacao;
		
	}
	
	private BigDecimal contaDiasLactacaoParto(Date dataInicio, Date dataFim, Parto parto, Animal femea){
		
		BigDecimal diasEmLactacao = BigDecimal.ZERO;
		
		//verifica se o parto teve o encerramento da lactação
		EncerramentoLactacao encerramento = parto.getEncerramentoLactacao();
		if ( encerramento != null && encerramento.getData().compareTo(dataInicio) >= 0 && encerramento.getData().compareTo(dataFim) <= 0 ){
			diasEmLactacao = diasEmLactacao.add(BigDecimal.valueOf(ChronoUnit.DAYS.between(DateUtil.asLocalDate(dataInicio), DateUtil.asLocalDate(encerramento.getData()))));
		}else{
			
			//Procura registro venda animal após o último parto
			VendaAnimal vendaAnimal = findVendaAnimal(parto.getData(), femea);
			
			if ( vendaAnimal != null ){
				long diasEntreVendaEInicioPeriodo = ChronoUnit.DAYS.between(DateUtil.asLocalDate(dataInicio), DateUtil.asLocalDate(vendaAnimal.getDataVenda()));
				if ( diasEntreVendaEInicioPeriodo > 0 ){//a lactação avançou pelo período
					diasEmLactacao = diasEmLactacao.add(BigDecimal.valueOf(diasEntreVendaEInicioPeriodo));
				}
			}
			
			//Procura registro morte animal após o último parto
			MorteAnimal morteAnimal = findMorteAnimal(parto.getData(), femea);
			if ( morteAnimal != null ){
				long diasEntreMorteEInicioPeriodo = ChronoUnit.DAYS.between(DateUtil.asLocalDate(dataInicio), DateUtil.asLocalDate(morteAnimal.getDataMorte()));
				if ( diasEntreMorteEInicioPeriodo > 0 ){//a lactação avançou pelo período
					diasEmLactacao = diasEmLactacao.add(BigDecimal.valueOf(diasEntreMorteEInicioPeriodo));
				}
			}
			
		}
		
		return diasEmLactacao;
		
	}
	
	
	private VendaAnimal findVendaAnimal(Date dataInicio, Animal animal){
		
		Query query = entityManager.createQuery("SELECT va FROM VendaAnimal va inner join va.animaisVendidos av where av.animal = :animal and va.dataVenda > :dataInicio order by va.dataVenda desc");
		query.setParameter("dataInicio", dataInicio);
		query.setParameter("animal", animal);
		query.setMaxResults(1);
		
		VendaAnimal vendaAnimal = null;
		try{
			vendaAnimal = (VendaAnimal) query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
		
		return vendaAnimal;
		
	}
	
	private MorteAnimal findMorteAnimal(Date dataInicio, Animal animal){
		Query query = entityManager.createQuery("SELECT m FROM MorteAnimal m where m.animal = :animal and m.dataMorte > :dataInicio order by m.dataMorte desc");
		query.setParameter("dataInicio", dataInicio);
		query.setParameter("animal", animal);
		query.setMaxResults(1);
		
		MorteAnimal morteAnimal = null;
		try{
			morteAnimal = (MorteAnimal) query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
		return morteAnimal;
	}
	
	//============FIM DIAS LACTAÇÃO===========
	
	private BigDecimal getValorApuradoDiasEmAberto(){
		return BigDecimal.ZERO;
	}
	
	private BigDecimal getValorApuradoIntervaloEntrePartos(){
		return BigDecimal.ZERO;
	}
	
	private BigDecimal getValorApuradoTamanhoRebanho(){
		
		Object result = entityManager.createNativeQuery("select count(*) from viewAnimaisAtivos a").getSingleResult();
		return (result == null ? BigDecimal.ZERO : new BigDecimal(result.toString()));
		
	}
	
	private BigDecimal getValorApuradoTotalFemeas(){
		
		Object result = entityManager.createNativeQuery("select count(*) from viewAnimaisAtivos where sexo = '" + Sexo.FEMEA + "'").getSingleResult();
		return (result == null ? BigDecimal.ZERO : new BigDecimal(result.toString()));
		
	}
	
	private BigDecimal getValorApuradoTotalVacas(){
		
		Object result = entityManager.createNativeQuery(
				"select count(*) from viewAnimaisAtivos a where a.sexo = '" + Sexo.FEMEA + "' and exists  " +
				"(select 1 from cobertura c inner join parto p on (p.cobertura = c.id) where c.femea = a.id)").getSingleResult();
		return (result == null ? BigDecimal.ZERO : new BigDecimal(result.toString()));

	}
	
	private BigDecimal getValorApuradoTotalNovilhas(){
		
		Object result = entityManager.createNativeQuery(
				"select count(*) from viewAnimaisAtivos a where a.sexo = '" + Sexo.FEMEA + "' and not exists  " +
				"(select 1 from cobertura c inner join parto p on (p.cobertura = c.id) where c.femea = a.id)").getSingleResult();
		return (result == null ? BigDecimal.ZERO : new BigDecimal(result.toString()));

	}
	
	private BigDecimal getValorApuradoTotalNovilhasAteUmAno(){
		
		Object result = entityManager.createNativeQuery(
				"select count(*) from viewAnimaisAtivos a " +
				"where DATEDIFF(a.dataNascimento, current_date()) <= 360  " +
				"and a.sexo = '" + Sexo.FEMEA + "' and not exists "
						+ "(select 1 from cobertura c inner join parto p on (p.cobertura = c.id) where c.femea = a.id)").getSingleResult();
		return (result == null ? BigDecimal.ZERO : new BigDecimal(result.toString()));

	}
	
	private BigDecimal getValorApuradoTotalNovilhasMaisUmAno(){
		
		Object result = entityManager.createNativeQuery(
				"select count(*) from viewAnimaisAtivos a " +
				"where DATEDIFF(a.dataNascimento, current_date()) > 360  " +
				"and a.sexo = '" + Sexo.FEMEA + "' and not exists "
						+ "(select 1 from cobertura c inner join parto p on (p.cobertura = c.id) where c.femea = a.id)").getSingleResult();
		return (result == null ? BigDecimal.ZERO : new BigDecimal(result.toString()));
	
	}
	
	private BigDecimal getValorApuradoTotalVacasLactacao(){
		
		Object result = entityManager.createNativeQuery(" select count(*) from viewAnimaisAtivos a "
				+ "inner join cobertura c inner join parto p on (p.cobertura = c.id) "
				+ "where c.femea = a.id and not exists "
				+ "(select 1 from encerramentoLactacao e where e.data > p.data and e.animal = a.id)").getSingleResult();
		
		return (result == null ? BigDecimal.ZERO : new BigDecimal(result.toString()));
	}
	
	private BigDecimal getValorApuradoTotalVacasSecas(){
		
		Object result = entityManager.createNativeQuery("select count(*) from viewAnimaisAtivos a "
				+ "inner join encerramentoLactacao e on (e.animal = a.id) where not exists "
				+ "(select 1 from parto p inner join cobertura c on (c.id = p.cobertura) where p.data > e.data and c.femea = a.id)").getSingleResult();
		return (result == null ? BigDecimal.ZERO : new BigDecimal(result.toString()));
		
	}
	/*
	 * TDC - dividindo o número de vacas inseminadas no período de 21 dias pelo número de vacas 
	 * disponíveis para serem inseminadas no mesmo período.
	 * 
	 * http://www.milkpoint.com.br/radar-tecnico/reproducao/manejo-reprodutivo-do-rebanho-leiteiro-26245n.aspx
	 * 
	 * http://www.milkpoint.com.br/radar-tecnico/reproducao/estrategias-de-manejo-para-aumentar-a-eficiencia-reprodutiva-de-vacas-de-leite-28283n.aspx
	 * 
	 * PVE - Periodo voluntário de espera (dias após o parto em que a vaca não deve ser enseminada)
	 */
	private BigDecimal getValorApuradoTaxaDeteccaoCio(){
		
		//vacas enseminadas ultimos 21 dias
		BigInteger vacasEnseminadas = (BigInteger) entityManager.createNativeQuery("select count(*) from cobertura c where  DATEDIFF(c.data, current_date()) <= 21 ").getSingleResult();
		
		//vacas disponíveis para serem cobertas
		//não vendidas, não mortas, não cobertas
		
		//return (result == null ? BigDecimal.ZERO : new BigDecimal(result.toString()));
		
		return BigDecimal.ZERO;
	}
	
	private BigDecimal getValorApuradoTaxaPrenhez(){
		return BigDecimal.ZERO;
	}
	
	
	
}