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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.milksys.model.Animal;
import br.com.milksys.model.EncerramentoLactacao;
import br.com.milksys.model.Indicador;
import br.com.milksys.model.MorteAnimal;
import br.com.milksys.model.Parametro;
import br.com.milksys.model.Parto;
import br.com.milksys.model.Sexo;
import br.com.milksys.model.SituacaoCobertura;
import br.com.milksys.model.VendaAnimal;
import br.com.milksys.util.DateUtil;

@Repository
public class IndicadorDao extends AbstractGenericDao<Integer, Indicador> {

	@Autowired ParametroDao parametroDao;
	
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
				
			case "TDC":
				indicador.setValorApurado(getValorApuradoTaxaDeteccaoCio());
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
	/*
	 * Buscar todos os partos e fazer a média dos dias em lactação
	 */
	@SuppressWarnings("unchecked")
	private BigDecimal getValorApuradoDiasEmLactacao(){
		
		BigDecimal diasEmLactacao = BigDecimal.ZERO;
		int        totalPartos    = 0;
		
		//busca todos os partos
		Query query = entityManager.createQuery("SELECT p FROM Parto p order by p.data asc");
		
		List<Parto> partos = query.getResultList();
		
		for ( Parto parto : partos ){
			
			BigDecimal diasLactacaoParto = contaDiasLactacaoParto(parto);
			
			if ( diasLactacaoParto.compareTo(BigDecimal.ZERO) <= 0 ){
				//se retornou zero é porque o ultimo parto não teve encerramento da lactação
				//e o animal não foi vendido nem está morto.
				//Nesse caso utilizar a data corrente para cálculo dos dias em lactação
				diasLactacaoParto = BigDecimal.valueOf(ChronoUnit.DAYS.between(DateUtil.asLocalDate(parto.getData()), LocalDate.now()));
			}
			
			diasEmLactacao = diasEmLactacao.add(diasLactacaoParto);
			totalPartos++;
			
		}
		
		if ( diasEmLactacao.compareTo(BigDecimal.ZERO) > 0 && totalPartos > 0 ){
			diasEmLactacao = diasEmLactacao.divide(new BigDecimal(totalPartos), 2, RoundingMode.HALF_UP);
		}
		
		return diasEmLactacao;
		
	}
	
	private BigDecimal contaDiasLactacaoParto(Parto parto){
		
		BigDecimal diasEmLactacao = BigDecimal.ZERO;
		
		//verifica se o parto teve o encerramento da lactação
		EncerramentoLactacao encerramento = parto.getEncerramentoLactacao();
		
		if ( encerramento != null ){
			diasEmLactacao = diasEmLactacao.add(BigDecimal.valueOf(ChronoUnit.DAYS.between(DateUtil.asLocalDate(parto.getData()), DateUtil.asLocalDate(encerramento.getData()))));
		}else{
			
			//Procura registro venda animal após o parto
			VendaAnimal vendaAnimal = findVendaAnimal(parto.getData(), parto.getCobertura().getFemea());
			
			if ( vendaAnimal != null ){
				long diasEntreVendaEInicioPeriodo = ChronoUnit.DAYS.between(DateUtil.asLocalDate(parto.getData()), DateUtil.asLocalDate(vendaAnimal.getDataVenda()));
				if ( diasEntreVendaEInicioPeriodo > 0 ){//a lactação avançou pelo período
					diasEmLactacao = diasEmLactacao.add(BigDecimal.valueOf(diasEntreVendaEInicioPeriodo));
				}
			}
			
			//Procura registro morte animal após o último parto
			MorteAnimal morteAnimal = findMorteAnimal(parto.getData(), parto.getCobertura().getFemea());
			if ( morteAnimal != null ){
				long diasEntreMorteEInicioPeriodo = ChronoUnit.DAYS.between(DateUtil.asLocalDate(parto.getData()), DateUtil.asLocalDate(morteAnimal.getDataMorte()));
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
		
		try{
			return (VendaAnimal) query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
		
	}
	
	private MorteAnimal findMorteAnimal(Date dataInicio, Animal animal){
		Query query = entityManager.createQuery("SELECT m FROM MorteAnimal m where m.animal = :animal and m.dataMorte > :dataInicio order by m.dataMorte desc");
		query.setParameter("dataInicio", dataInicio);
		query.setParameter("animal", animal);
		query.setMaxResults(1);
		
		try{
			return (MorteAnimal) query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}
	
	//============FIM DIAS LACTAÇÃO===========
	/*
	 * Para o cálculo do dias em aberto devemos considerar o número de dias do último parto até:
	   A data da concepção das vacas gestantes
	   A data da última cobertura das vacas ainda não confirmadas gestantes
	   Ou a data em que o cálculo foi realizado.
       As vacas já definidas como descarte, mas que ainda estão em lactação, não precisam ser incluídas nos cálculos.
	   O cálculo da média dos dias em aberto é feito pela soma dos dias em aberto de cada vaca divido pelo número de vacas do rebanho.
	 */
	private BigDecimal getValorApuradoDiasEmAberto(){
		return BigDecimal.ZERO;
	}
	/*
	 * O intervalo entre partos atual é o cálculo do número de meses entre o parto mais recente e o 
	 * parto anterior das vacas com mais de um parto. Neste dado não entram as vacas de primeira lactação.
	 * http://www.milkpoint.com.br/radar-tecnico/reproducao/interpretacao-dos-indices-da-eficiencia-reprodutiva-41269n.aspx
	 * 
	 */
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
				"where DATEDIFF(current_date(), a.dataNascimento) <= 360  " +
				"and a.sexo = '" + Sexo.FEMEA + "' and not exists "
						+ "(select 1 from cobertura c inner join parto p on (p.cobertura = c.id) where c.femea = a.id)").getSingleResult();
		return (result == null ? BigDecimal.ZERO : new BigDecimal(result.toString()));

	}
	
	private BigDecimal getValorApuradoTotalNovilhasMaisUmAno(){
		
		Object result = entityManager.createNativeQuery(
				"select count(*) from viewAnimaisAtivos a " +
				"where DATEDIFF(current_date(), a.dataNascimento) > 360  " +
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
		
		int diasIdadeMinimaParaCobertura = 0;
		try{
			//o parametro estara em meses, multiplicar por 30 para obter os dias
			diasIdadeMinimaParaCobertura = Integer.parseInt(parametroDao.findBySigla(Parametro.IDMC)) * 30;
		}catch(Exception e){
			diasIdadeMinimaParaCobertura = 24 * 30;
		}
		
		int periodoVoluntarioEspera = 0;
		try{
			periodoVoluntarioEspera = Integer.parseInt(parametroDao.findBySigla(Parametro.PVE));
		}catch(Exception e){
			periodoVoluntarioEspera = 40;//default 40 dias
		}
		
		//vacas enseminadas ultimos 21 dias
		BigInteger vacasEnseminadas = (BigInteger) entityManager.createNativeQuery(
				"select count(*) from cobertura c where  DATEDIFF(current_date(), c.data) between 0 and 21 ").getSingleResult();
		
		//vacas disponíveis para serem cobertas:
		//(1) não vendidas, (2) não mortas, (3) que não estejam cobertas(prenhas) no período, (3) não são recém paridas, (4) tem idade suficiente para cobertura
		BigInteger vacasDisponiveis = (BigInteger) entityManager.createNativeQuery(
				"select count(*) from animal a where DATEDIFF(current_date(), a.dataNascimento) between 0 and " + diasIdadeMinimaParaCobertura + " and "
				+ "not exists (select 1 from animalVendido av where av.animal = a.id) and "
				+ "not exists (select 1 from morteAnimal ma where ma.animal = a.id) and "
				+ "not exists (select 1 from cobertura c where c.femea = a.id and DATEDIFF(current_date(), c.data) < 21 and c.situacaoCobertura in ('" + SituacaoCobertura.PRENHA + "','" + SituacaoCobertura.INDEFINIDA + "')) and "
				+ "not exists (select 1 from parto p inner join cobertura c on (c.id = p.cobertura) where c.femea = a.id and DATEDIFF(current_date(), p.data) between 0 and " + periodoVoluntarioEspera + ")").getSingleResult();
		
		if ( vacasEnseminadas.compareTo(BigInteger.ZERO) <= 0 ||
				vacasDisponiveis.compareTo(BigInteger.ZERO) <= 0 ){
			return BigDecimal.ZERO;
		}
		
		return BigDecimal.valueOf(vacasEnseminadas.divide(vacasDisponiveis).multiply(BigInteger.valueOf(100)).longValue());
		
	}
	
	
	private BigDecimal getValorApuradoTaxaPrenhez(){
		return BigDecimal.ZERO;
	}
	
	
	/*
	 * 
		Dias pós-parto no primeiro serviço (primeira cobertura)
		O número de dias pós-parto no primeiro serviço é influenciado pelo período voluntário de espera (PVE), 
		como o próprio nome diz, por uma decisão de manejo, por isso esse índice varia muito entre os rebanhos. 
		Cada fazenda deve, a partir do final do PVE, definir o objetivo a ser alcançado para a média de dias pós-parto no primeiro serviço.
		Algumas vacas podem ser cobertas com 40 dias pós-parto, porém na maioria dos rebanhos de alta produção, 
		o máximo de fertilidade é alcançado por volta dos 60 dias pós-parto.
		http://www.milkpoint.com.br/radar-tecnico/reproducao/interpretacao-dos-indices-da-eficiencia-reprodutiva-41269n.aspx
	 */
	
	
}