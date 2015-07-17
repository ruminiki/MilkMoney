package br.com.milksys.controller.painel;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.UCTextField;
import br.com.milksys.model.Indicador;
import br.com.milksys.service.indicadores.IndicadorService;

@Controller
public class IndicadorOverviewController {

	@FXML private TableView<Indicador> table;
	@FXML private TableColumn<Indicador, String> indicadorColumn;
	@FXML private TableColumn<Indicador, String> siglaColumn;
	@FXML private TableColumn<Indicador, String> valorReferenciaColumn;
	@FXML private TableColumn<Indicador, String> valorApuradoColumn;
	@FXML private Label lblIndicador, lblDefinicaoIndicador;
	@FXML private UCTextField inputValorReferencia;
	
	@Autowired private IndicadorService service;
	
	private Indicador selectedIndicador;

	@FXML
	public void initialize() {

		indicadorColumn.setCellValueFactory(new PropertyValueFactory<Indicador,String>("descricao"));
		siglaColumn.setCellValueFactory(new PropertyValueFactory<Indicador,String>("sigla"));
		valorReferenciaColumn.setCellValueFactory(new PropertyValueFactory<Indicador,String>("valorReferencia"));
		valorApuradoColumn.setCellValueFactory(new PropertyValueFactory<Indicador,String>("valorApurado"));
		
		table.setItems(service.findAllAsObservableList());
		
		table.getSelectionModel().selectedItemProperty()
		.addListener((observable, oldValue, newValue) -> selectRowTableHandler(newValue));
		
	}
	
	private void selectRowTableHandler(Indicador indicador) {
		
		selectedIndicador = indicador;
		lblIndicador.setText(indicador.getDescricao());
		lblDefinicaoIndicador.setText(indicador.getDefinicao());
		inputValorReferencia.setText(indicador.getValorReferencia());
		
	}
	
	@FXML
	private void handleSalvar(){
		service.save(selectedIndicador);
	}

	public String getFormName(){
		return "view/painel/IndicadorOverview.fxml";
	}

}
