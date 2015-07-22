package br.com.milksys.service;

import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milksys.reports.GenericPentahoReport;

@Service
public class RelatorioService {

	@Autowired FichaAnimalService fichaAnimalService;
	
	public static final String FICHA_ANIMAL_FILE_REPORT = "report/fichaAnimal.prpt"; 
	
	public void executeRelatorio(String format, String report, Object ...param){
		Thread t = new Thread(new Runnable() {
		    @Override
		    public void run(){
		    	switch (report) {
				case FICHA_ANIMAL_FILE_REPORT:
					fichaAnimalService.generateFichaForAll();
					MasterReport masterReport = GenericPentahoReport.getReportDefinition(report);
					masterReport.getParameterValues().put("animais", param[0]);
					GenericPentahoReport.runReport(format, masterReport);
					break;
				default:
					break;
				}
		    }
		});
		t.start();
	}
	
	
	
}
