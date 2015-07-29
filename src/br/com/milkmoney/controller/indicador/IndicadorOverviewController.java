package br.com.milkmoney.controller.indicador;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.PropertyDecimalValueFactory;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.controller.painel.renderer.TableCellValueFactoryResultadoIndicador;
import br.com.milkmoney.model.Indicador;
import br.com.milkmoney.service.IService;

@Controller
public class IndicadorOverviewController extends AbstractOverviewController<Integer, Indicador>{

	@FXML private TableView<Indicador> table;
	@FXML private TableColumn<Indicador, String> indicadorColumn;
	@FXML private TableColumn<Indicador, String> siglaColumn;
	@FXML private TableColumn<Indicador, String> valorReferenciaColumn;
	@FXML private TableColumn<Indicador, String> valorApuradoColumn;
	@FXML private TableColumn<Indicador, String> resultadoColumn;
	@FXML private Label lblIndicador, lblDefinicaoIndicador;
	
	//private Indicador selectedIndicador;

	@FXML
	public void initialize() {

		indicadorColumn.setCellValueFactory(new PropertyValueFactory<Indicador,String>("descricao"));
		siglaColumn.setCellValueFactory(new PropertyValueFactory<Indicador,String>("sigla"));
		valorReferenciaColumn.setCellValueFactory(new PropertyDecimalValueFactory<Indicador,String>("valorReferencia"));
		valorApuradoColumn.setCellValueFactory(new PropertyDecimalValueFactory<Indicador,String>("valorApurado"));
		resultadoColumn.setCellFactory(new TableCellValueFactoryResultadoIndicador<Indicador,String>("resultado"));
		
		table.setItems(service.findAllAsObservableList());
		
		super.initialize((IndicadorFormController)MainApp.getBean(IndicadorFormController.class));
		
		/*if ( table.getItems() != null && table.getItems().size() > 0 ){
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
		});*/
		
	}
	/*
	protected void selectRowTableHandler(Indicador indicador) {
		if ( indicador != null ){
			selectedIndicador = indicador;
			lblIndicador.setText(indicador.getDescricao());
			lblDefinicaoIndicador.setText(indicador.getDefinicao());
			inputValorReferencia.setText(NumberFormatUtil.decimalFormat(indicador.getValorReferencia()));
		}
	}
	
	@FXML
	private void handleSalvar(){
		if ( selectedIndicador != null ){
			selectedIndicador.setValorReferencia(NumberFormatUtil.fromString(inputValorReferencia.getText()));
			service.save(selectedIndicador);
			
			for ( int index = 0; index < table.getItems().size(); index++ ){
				Indicador indicador = table.getItems().get(index);
				if ( selectedIndicador.getId() == indicador.getId() ){
					table.getItems().set(index, selectedIndicador);
				}
			}
			
		}
	}*/

	public String getFormName(){
		return "view/indicador/IndicadorOverview.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Indicador";
	}
	
	@Override
	@Resource(name = "indicadorService")
	protected void setService(IService<Integer, Indicador> service) {
		super.setService(service);
	}


}
