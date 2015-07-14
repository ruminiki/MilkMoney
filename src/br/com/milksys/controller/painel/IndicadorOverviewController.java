package br.com.milksys.controller.painel;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.model.Comprador;
import br.com.milksys.model.Indicador;
import br.com.milksys.service.IndicadorService;

@Controller
public class IndicadorOverviewController {

	@FXML private TableView<Indicador> table;
	@FXML private TableColumn<Comprador, String> indicadorColumn;
	@FXML private TableColumn<Comprador, String> siglaColumn;
	@FXML private TableColumn<Comprador, String> valorReferenciaColumn;
	@FXML private TableColumn<Comprador, String> valorApuradoColumn;
	
	@Autowired private IndicadorService service;

	@FXML
	public void initialize() {

		indicadorColumn.setCellValueFactory(new PropertyValueFactory<Comprador,String>("descricao"));
		siglaColumn.setCellValueFactory(new PropertyValueFactory<Comprador,String>("sigla"));
		valorReferenciaColumn.setCellValueFactory(new PropertyValueFactory<Comprador,String>("valorReferencia"));
		valorApuradoColumn.setCellValueFactory(new PropertyValueFactory<Comprador,String>("valorApurado"));
		
		table.setItems(service.findAllAsObservableList());
		
	}
	
	public String getFormName(){
		return "view/painel/IndicadorOverview.fxml";
	}

}
