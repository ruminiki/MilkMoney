package br.com.milkmoney.service;

import java.util.Date;
import java.util.List;

import javafx.concurrent.Task;
import javafx.scene.Cursor;

import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.controller.reports.GenericPentahoReport;
import br.com.milkmoney.controller.root.RootLayoutController;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Propriedade;

@Service
public class RelatorioService {

	@Autowired FichaAnimalService fichaAnimalService;
	@Autowired PropriedadeService propriedadeService;
	
	@Autowired RootLayoutController rootLayoutController;
	
	public static final String FICHA_ANIMAL                        = "report/fichaAnimal.prpt"; 
	public static final String FORMULARIO_CAMPO_REGISTRO_COBERTURA = "report/formularioCobertura.prpt"; 
	public static final String FORMULARIO_CAMPO_REGISTRO_PARTO     = "report/formularioParto.prpt"; 
	public static final String FORMULARIO_CAMPO_REGISTRO_PRODUCAO  = "report/formularioRegistroProducao.prpt"; 
	public static final String RELATORIO_COBERTURA                 = "report/relatorioCobertura.prpt";
	public static final String RELATORIO_PARTOS_PREVISTOS          = "report/relatorioPartosPrevistos.prpt";
	public static final String RELATORIO_INDICADORES               = "report/relatorioIndicadores.prpt";
	public static final String RELATORIO_PARTOS 				   = "report/relatorioPartos.prpt";
	public static final String RESUMO_FINANCEIRO                   = "report/resumoFinanceiro.prpt";
	public static final String RELATORIO_LANCAMENTOS_FINANCEIROS   = "report/relatorioFinanceiro.prpt";
	public static final String IMPRIMIR_COBERTURAS_ANIMAL          = "report/coberturasAnimal.prpt";
	public static final String FICHA_COMPLETA_ANIMAL               = "report/fichaCompletaAnimal.prpt";
	public static final String RELATORIO_PROCEDIMENTOS             = "report/relatorioProcedimentos.prpt";
	public static final String RELATORIO_PRODUCAO                  = "report/relatorioProducao.prpt";
	public static final String RELATORIO_RANKING_ANIMAIS           = "report/rankingAnimais.prpt";
	public static final String RELATORIO_ABORTOS                   = "report/relatorioAbortos.prpt";
	public static final String RELATORIO_ENCERRAMENTO_LACTACAO     = "report/relatorioEncerramentoLactacao.prpt";
	public static final String RELATORIO_RESUMO_ATIVIDADE          = "report/resumoAtividade.prpt";
	
	public void executeRelatorio(String format, String report, Object ...param){
		@SuppressWarnings("rawtypes")
		javafx.concurrent.Service service = new javafx.concurrent.Service() {
	        @Override
	        protected Task createTask() {
	            return new Task<Void>() {
	                @SuppressWarnings("unchecked")
					@Override protected Void call() throws Exception {
	                	//deve ter cadastrado a propriedade
	        			Propriedade propriedade = propriedadeService.findAll().get(0);
	        			MasterReport masterReport;
        		    	switch (report) {
        				case FICHA_ANIMAL:
        					fichaAnimalService.generateFichaAnimal((List<Animal>)param[0], null);
        					masterReport = GenericPentahoReport.getReportDefinition(report);
        					masterReport.getParameterValues().put("animais", param[1]);
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
        					masterReport.getParameterValues().put("lote", Integer.parseInt(param[3].toString()));
        					masterReport.getParameterValues().put("idadeDe", Integer.parseInt(param[4].toString()));
        					masterReport.getParameterValues().put("idadeAte", Integer.parseInt(param[5].toString()));
        					masterReport.getParameterValues().put("tipoCobertura", String.valueOf(param[6]));
        					masterReport.getParameterValues().put("touroInseminacaoArtificial", Integer.parseInt(param[7].toString()));
        					masterReport.getParameterValues().put("touroMontaNatural", Integer.parseInt(param[8].toString()));
        					GenericPentahoReport.runReport(format, masterReport);
        					break;
        				case RELATORIO_ABORTOS:
        					masterReport = GenericPentahoReport.getReportDefinition(report);
        					masterReport.getParameterValues().put("nomePropriedade", propriedade.getDescricao());
        					masterReport.getParameterValues().put("dataInicio", (Date) param[0]);
        					masterReport.getParameterValues().put("dataFim", (Date) param[1]);
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
        				case RELATORIO_RESUMO_ATIVIDADE:
        					masterReport = GenericPentahoReport.getReportDefinition(report);
        					masterReport.getParameterValues().put("nomePropriedade", propriedade.getDescricao());
        					masterReport.getParameterValues().put("ano", Integer.parseInt(param[0].toString()));
        					GenericPentahoReport.runReport(format, masterReport);
        					break;
        				case RESUMO_FINANCEIRO:
        					masterReport = GenericPentahoReport.getReportDefinition(report);
        					masterReport.getParameterValues().put("nomePropriedade", propriedade.getDescricao());
        					masterReport.getParameterValues().put("ano", Integer.parseInt(param[0].toString()));
        					masterReport.getParameterValues().put("centroCusto", Integer.parseInt(param[1].toString()));
        					masterReport.getParameterValues().put("nomeCentroCusto", param[2]);
        					GenericPentahoReport.runReport(format, masterReport);
        					break;
        				case RELATORIO_LANCAMENTOS_FINANCEIROS:
        					masterReport = GenericPentahoReport.getReportDefinition(report);
        					masterReport.getParameterValues().put("nomePropriedade", propriedade.getDescricao());
        					masterReport.getParameterValues().put("dataInicio", (Date) param[0]);
        					masterReport.getParameterValues().put("dataFim", (Date) param[1]);
        					masterReport.getParameterValues().put("descricao", param[2]);
        					masterReport.getParameterValues().put("categoria", Integer.parseInt(param[3].toString()));
        					masterReport.getParameterValues().put("centroCusto", Integer.parseInt(param[4].toString()));
        					GenericPentahoReport.runReport(format, masterReport);
        					break;
        				case IMPRIMIR_COBERTURAS_ANIMAL:
        					masterReport = GenericPentahoReport.getReportDefinition(report);
        					masterReport.getParameterValues().put("nomePropriedade", propriedade.getDescricao());
        					masterReport.getParameterValues().put("femea", Integer.parseInt(param[0].toString()));
        					GenericPentahoReport.runReport(format, masterReport);
        					break;
        				case FICHA_COMPLETA_ANIMAL:
        					masterReport = GenericPentahoReport.getReportDefinition(report);
        					masterReport.getParameterValues().put("nomePropriedade", propriedade.getDescricao());
        					masterReport.getParameterValues().put("femea", Integer.parseInt(param[0].toString()));
        					GenericPentahoReport.runReport(format, masterReport);
        					break;
        				case RELATORIO_PROCEDIMENTOS:
        					masterReport = GenericPentahoReport.getReportDefinition(report);
        					masterReport.getParameterValues().put("animais", param[0]);
        					masterReport.getParameterValues().put("nomePropriedade", propriedade.getDescricao());
        					masterReport.getParameterValues().put("dataInicio", (Date) param[1]);
        					masterReport.getParameterValues().put("dataFim", (Date) param[2]);
        					masterReport.getParameterValues().put("tipoProcedimento", String.valueOf(param[3]));
        					masterReport.getParameterValues().put("observacao", String.valueOf(param[4]));
        					GenericPentahoReport.runReport(format, masterReport);
        					break;
        				case RELATORIO_PRODUCAO:
        					masterReport = GenericPentahoReport.getReportDefinition(report);
        					masterReport.getParameterValues().put("nomePropriedade", propriedade.getDescricao());
        					masterReport.getParameterValues().put("ano", Integer.parseInt(param[0].toString()));
        					GenericPentahoReport.runReport(format, masterReport);
        					break;
        				case RELATORIO_RANKING_ANIMAIS:
        					fichaAnimalService.generateFichaForAll();
        					masterReport = GenericPentahoReport.getReportDefinition(report);
        					masterReport.getParameterValues().put("animais", param[0]);
        					masterReport.getParameterValues().put("nomePropriedade", propriedade.getDescricao());
        					GenericPentahoReport.runReport(format, masterReport);
        					break;
        				case RELATORIO_INDICADORES:
        					masterReport = GenericPentahoReport.getReportDefinition(report);
        					masterReport.getParameterValues().put("nomePropriedade", propriedade.getDescricao());
        					masterReport.getParameterValues().put("indicadores", param[0]);
        					GenericPentahoReport.runReport(format, masterReport);
        					break;
        				case RELATORIO_ENCERRAMENTO_LACTACAO:
        					masterReport = GenericPentahoReport.getReportDefinition(report);
        					masterReport.getParameterValues().put("nomePropriedade", propriedade.getDescricao());
        					masterReport.getParameterValues().put("dataInicio", (Date) param[0]);
        					masterReport.getParameterValues().put("dataFim", (Date) param[1]);
        					masterReport.getParameterValues().put("motivoEncerramentoLactacao", Integer.parseInt(param[2].toString()));
        					GenericPentahoReport.runReport(format, masterReport);
        					break;
        				default:
        					masterReport = GenericPentahoReport.getReportDefinition(report);
        					masterReport.getParameterValues().put("nomePropriedade", propriedade.getDescricao());
        					GenericPentahoReport.runReport(format, masterReport);
        					break;
        				}
        				MainApp.rootLayout.getCenter().setCursor(Cursor.DEFAULT);
        				MainApp.setCursor(Cursor.DEFAULT);
	                    return null;
	                }
	            };
	        }
	    };
		
		service.start();
		
		rootLayoutController.setMessage("O relat�rio est� sendo executado...");

		MainApp.rootLayout.getCenter().setCursor(Cursor.WAIT);
		MainApp.setCursor(Cursor.WAIT);
		
	}
	
	
	
}
