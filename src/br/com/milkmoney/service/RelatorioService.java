package br.com.milkmoney.service;

import java.util.Date;

import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.controller.reports.GenericPentahoReport;
import br.com.milkmoney.model.Propriedade;

@Service
public class RelatorioService {

	@Autowired FichaAnimalService fichaAnimalService;
	@Autowired PropriedadeService propriedadeService;
	
	public static final String FICHA_ANIMAL                        = "report/fichaAnimal.prpt"; 
	public static final String FORMULARIO_CAMPO_REGISTRO_COBERTURA = "report/formularioCobertura.prpt"; 
	public static final String FORMULARIO_CAMPO_REGISTRO_PARTO     = "report/formularioParto.prpt"; 
	public static final String FORMULARIO_CAMPO_REGISTRO_PRODUCAO  = "report/formularioRegistroProducao.prpt"; 
	public static final String RELATORIO_COBERTURA                 = "report/relatorioCobertura.prpt";
	public static final String RELATORIO_PARTOS_PREVISTOS          = "report/relatorioPartosPrevistos.prpt";
	public static final String RELATORIO_INDICADORES               = "report/relatorioIndicadores.prpt";
	public static final String RELATORIO_PARTOS 				   = "report/relatorioPartos.prpt";
	public static final String RESUMO_FINANCEIRO                   = "report/resumoFinanceiro.prpt";
	
	public void executeRelatorio(String format, String report, Object ...param){
		Thread t = new Thread(new Runnable() {
			
			//deve ter cadastrado a propriedade
			Propriedade propriedade = propriedadeService.findAll().get(0);
			MasterReport masterReport;
		    @Override
		    public void run(){
		    	switch (report) {
				case FICHA_ANIMAL:
					fichaAnimalService.generateFichaForAll();
					masterReport = GenericPentahoReport.getReportDefinition(report);
					masterReport.getParameterValues().put("animais", param[0]);
					masterReport.getParameterValues().put("nomePropriedade", propriedade.getDescricao());
					GenericPentahoReport.runReport(format, masterReport);
					break;
				case FORMULARIO_CAMPO_REGISTRO_COBERTURA:
					masterReport = GenericPentahoReport.getReportDefinition(report);
					masterReport.getParameterValues().put("animais", param[0]);
					masterReport.getParameterValues().put("nomePropriedade", propriedade.getDescricao());
					GenericPentahoReport.runReport(format, masterReport);
					break;
				case FORMULARIO_CAMPO_REGISTRO_PARTO:
					masterReport = GenericPentahoReport.getReportDefinition(report);
					masterReport.getParameterValues().put("animais", param[0]);
					masterReport.getParameterValues().put("nomePropriedade", propriedade.getDescricao());
					GenericPentahoReport.runReport(format, masterReport);
					break;
				case FORMULARIO_CAMPO_REGISTRO_PRODUCAO:
					masterReport = GenericPentahoReport.getReportDefinition(report);
					masterReport.getParameterValues().put("nomePropriedade", propriedade.getDescricao());
					masterReport.getParameterValues().put("mes", String.valueOf(param[0]));
					GenericPentahoReport.runReport(format, masterReport);
					break;
				case RELATORIO_COBERTURA:
					masterReport = GenericPentahoReport.getReportDefinition(report);
					masterReport.getParameterValues().put("nomePropriedade", propriedade.getDescricao());
					masterReport.getParameterValues().put("dataInicio", (Date) param[0]);
					masterReport.getParameterValues().put("dataFim", (Date) param[1]);
					masterReport.getParameterValues().put("situacaoCobertura", String.valueOf(param[2]));
					masterReport.getParameterValues().put("tipoCobertura", String.valueOf(param[3]));
					masterReport.getParameterValues().put("touroInseminacaoArtificial", Integer.parseInt(param[4].toString()));
					masterReport.getParameterValues().put("touroMontaNatural", Integer.parseInt(param[5].toString()));
					GenericPentahoReport.runReport(format, masterReport);
					break;
				case RELATORIO_PARTOS:
					masterReport = GenericPentahoReport.getReportDefinition(report);
					masterReport.getParameterValues().put("nomePropriedade", propriedade.getDescricao());
					masterReport.getParameterValues().put("dataInicio", (Date) param[0]);
					masterReport.getParameterValues().put("dataFim", (Date) param[1]);
					masterReport.getParameterValues().put("tipoParto", String.valueOf(param[2]));
					masterReport.getParameterValues().put("complicacaoParto", Integer.parseInt(param[3].toString()));
					GenericPentahoReport.runReport(format, masterReport);
					break;
				case RELATORIO_PARTOS_PREVISTOS:
					masterReport = GenericPentahoReport.getReportDefinition(report);
					masterReport.getParameterValues().put("nomePropriedade", propriedade.getDescricao());
					masterReport.getParameterValues().put("quantidadeDias", Integer.parseInt(param[0].toString()));
					GenericPentahoReport.runReport(format, masterReport);
					break;
				case RESUMO_FINANCEIRO:
					masterReport = GenericPentahoReport.getReportDefinition(report);
					masterReport.getParameterValues().put("nomePropriedade", propriedade.getDescricao());
					masterReport.getParameterValues().put("ano", Integer.parseInt(param[0].toString()));
					GenericPentahoReport.runReport(format, masterReport);
					break;
				default:
					masterReport = GenericPentahoReport.getReportDefinition(report);
					masterReport.getParameterValues().put("nomePropriedade", propriedade.getDescricao());
					GenericPentahoReport.runReport(format, masterReport);
					break;
				}
		    }
		});
		t.start();
	}
	
	
	
}
