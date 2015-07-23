package br.com.milkmoney.controller.painel;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.MaskFieldUtil;
import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.painel.renderer.TableCellValueFactoryResultadoIndicador;
import br.com.milkmoney.model.Indicador;
import br.com.milkmoney.service.indicadores.IndicadorService;

@Controller
public class IndicadorOverviewController {

	@FXML private TableView<Indicador> table;
	@FXML private TableColumn<Indicador, String> indicadorColumn;
	@FXML private TableColumn<Indicador, String> siglaColumn;
	@FXML private TableColumn<Indicador, String> valorReferenciaColumn;
	@FXML private TableColumn<Indicador, String> valorApuradoColumn;
	@FXML private TableColumn<Indicador, String> resultadoColumn;
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
		resultadoColumn.setCellFactory(new TableCellValueFactoryResultadoIndicador<Indicador,String>("resultado"));
		
		table.setItems(service.findAllAsObservableList());
		if ( table.getItems() != null && table.getItems().size() > 0 ){
			table.getSelectionModel().clearAndSelect(0);
			selectRowTableHandler(table.getItems().get(0));
		}
		
		table.getSelectionModel().selectedItemProperty()
		.addListener((observable, oldValue, newValue) -> selectRowTableHandler(newValue));
		
		// captura o evento de ENTER no text field
		inputValorReferencia.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)) {
					handleSalvar();
				}
			}
		});
		
		MaskFieldUtil.decimal(inputValorReferencia);
		
	}
	
	private void selectRowTableHandler(Indicador indicador) {
		if ( indicador != null ){
			selectedIndicador = indicador;
			lblIndicador.setText(indicador.getDescricao());
			lblDefinicaoIndicador.setText(indicador.getDefinicao());
			inputValorReferencia.setText(indicador.getValorReferencia());
		}
	}
	
	@FXML
	private void handleSalvar(){
		if ( selectedIndicador != null ){
			selectedIndicador.setValorReferencia(inputValorReferencia.getText());
			service.save(selectedIndicador);
			
			for ( int index = 0; index < table.getItems().size(); index++ ){
				Indicador indicador = table.getItems().get(index);
				if ( selectedIndicador.getId() == indicador.getId() ){
					table.getItems().set(index, selectedIndicador);
				}
			}
			
		}
	}

	public String getFormName(){
		return "view/painel/IndicadorOverview.fxml";
	}

}
