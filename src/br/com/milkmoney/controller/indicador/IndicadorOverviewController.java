package br.com.milkmoney.controller.indicador;

import java.util.function.Function;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.BoxIndicador;
import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.controller.reports.GenericPentahoReport;
import br.com.milkmoney.controller.root.RootLayoutController;
import br.com.milkmoney.model.Indicador;
import br.com.milkmoney.model.State;
import br.com.milkmoney.service.RelatorioService;
import br.com.milkmoney.service.indicadores.IndicadorService;

@Controller
public class IndicadorOverviewController {

	@FXML private GridPane grid;
	@Autowired private IndicadorService service;
	@Autowired private IndicadorFormController indicadorFormController;
	@Autowired private RelatorioService relatorioService;
	@Autowired private RootLayoutController rootLayoutController;
	
	private ObservableList<Indicador> data;
	
	
	@FXML
	public void initialize() {
		
		data = service.findAllAsObservableList();
		
		int row = 0;
		int col = 0;
		
		for ( Indicador indicador : data ){
			
			BoxIndicador box = new BoxIndicador(indicador, editIndicador);
			GridPane.setConstraints(box, col, row);
			
			col++;
			
			if ( col == 5 ){
				col = 0;
				row++;
			}
			
			grid.getChildren().add(box);
			
		}
		
	}
	
	Function<Indicador, Boolean> editIndicador = indicador -> {
		if ( indicador != null ){
			indicadorFormController.setState(State.UPDATE);
			indicadorFormController.setObject(indicador);
			indicadorFormController.showForm();
		} else {
			CustomAlert.nenhumRegistroSelecionado();
		}
		return true;
	};
	
	@FXML
	private void handleImprimirIndicadores(){
		
		relatorioService.executeRelatorio(GenericPentahoReport.PDF_OUTPUT_FORMAT, RelatorioService.RELATORIO_INDICADORES);
		rootLayoutController.setMessage("O relatório está sendo executado...");
		
	}
	
	public String getFormName(){
		return "view/indicador/IndicadorOverview.fxml";
	}

	public String getFormTitle() {
		return "Indicador";
	}
	
}
