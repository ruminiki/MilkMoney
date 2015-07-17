package br.com.milksys.controller.painel;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.model.Indicador;
import br.com.milksys.service.indicadores.IndicadorService;

@Controller
public class IndicadorOverviewController {

	@FXML private TableView<Indicador> table;
	@FXML private TableColumn<Indicador, String> indicadorColumn;
	@FXML private TableColumn<Indicador, String> siglaColumn;
	@FXML private TableColumn<Indicador, String> valorReferenciaColumn;
	@FXML private TableColumn<Indicador, String> valorApuradoColumn;
	
	@Autowired private IndicadorService service;

	@FXML
	public void initialize() {

		indicadorColumn.setCellValueFactory(new PropertyValueFactory<Indicador,String>("descricao"));
		siglaColumn.setCellValueFactory(new PropertyValueFactory<Indicador,String>("sigla"));
		valorReferenciaColumn.setCellValueFactory(new PropertyValueFactory<Indicador,String>("valorReferencia"));
		valorApuradoColumn.setCellValueFactory(new PropertyValueFactory<Indicador,String>("valorApurado"));
		
		table.setItems(service.findAllAsObservableList());
		
	}
	
	public String getFormName(){
		return "view/painel/IndicadorOverview.fxml";
	}

}
