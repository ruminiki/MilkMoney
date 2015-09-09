package br.com.milkmoney.controller.indicador;

import java.util.function.Function;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.BoxIndicador;
import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.model.Indicador;
import br.com.milkmoney.model.State;
import br.com.milkmoney.service.indicadores.IndicadorService;

@Controller
public class IndicadorOverviewController {

	@FXML private GridPane grid;
	@Autowired IndicadorService service;
	@Autowired IndicadorFormController indicadorFormController;
	
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
	
	public String getFormName(){
		return "view/indicador/IndicadorOverview.fxml";
	}

	public String getFormTitle() {
		return "Indicador";
	}
	
}
