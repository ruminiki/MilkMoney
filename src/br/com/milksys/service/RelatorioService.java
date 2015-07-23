package br.com.milksys.service;

import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.controller.reports.GenericPentahoReport;
import br.com.milksys.model.Propriedade;

@Service
public class RelatorioService {

	@Autowired FichaAnimalService fichaAnimalService;
	@Autowired PropriedadeService propriedadeService;
	
	public static final String FICHA_ANIMAL                        = "report/fichaAnimal.prpt"; 
	public static final String FORMULARIO_CAMPO_REGISTRO_COBERTURA = "report/formularioCobertura.prpt"; 
	public static final String FORMULARIO_CAMPO_REGISTRO_PARTO     = "report/formularioParto.prpt"; 
	
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
