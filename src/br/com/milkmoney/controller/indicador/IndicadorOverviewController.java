package br.com.milkmoney.controller.indicador;

import java.util.function.Function;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.BoxIndicador;
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
	
	@FXML private GridPane grid;
	
	private ObservableList<Indicador> data;

	@FXML
	public void initialize() {

		indicadorColumn.setCellValueFactory(new PropertyValueFactory<Indicador,String>("descricao"));
		siglaColumn.setCellValueFactory(new PropertyValueFactory<Indicador,String>("sigla"));
		valorReferenciaColumn.setCellValueFactory(new PropertyDecimalValueFactory<Indicador,String>("valorReferencia"));
		valorApuradoColumn.setCellValueFactory(new PropertyDecimalValueFactory<Indicador,String>("valorApurado"));
		resultadoColumn.setCellFactory(new TableCellValueFactoryResultadoIndicador<Indicador,String>("resultado"));
		super.initialize((IndicadorFormController)MainApp.getBean(IndicadorFormController.class));
		
		data = service.findAllAsObservableList();
		
		int row = 0;
		int col = 0;
		
		for ( Indicador indicador : data ){
			
			BoxIndicador box = new BoxIndicador(indicador, editIndicador);
			GridPane.setConstraints(box, row, col);
			
			col++;
			
			if ( col == 5 ){
				col = 0;
				row++;
			}
			
			grid.getChildren().add(box);
			
		}
		
			
	}
	
	Function<Indicador, Boolean> editIndicador = indicador -> {
		super.setObject(indicador);
		super.handleEdit();
		return true;
	};
	
	public String getFormName(){
		return "view/indicador/IndicadorOverview.fxml";
	}

	@Override
	public String getFormTitle() {
		return "Indicador";
	}
	
	@Override
	@Resource(name = "indicadorService")
	protected void setService(IService<Integer, Indicador> service) {
		super.setService(service);
	}


}
