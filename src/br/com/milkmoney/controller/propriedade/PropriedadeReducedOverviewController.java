package br.com.milkmoney.controller.propriedade;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.TableCellOptionSelectFactory;
import br.com.milkmoney.controller.AbstractReducedOverviewController;
import br.com.milkmoney.model.Propriedade;
import br.com.milkmoney.service.IService;

@Controller
public class PropriedadeReducedOverviewController extends AbstractReducedOverviewController<Integer, Propriedade> {

	@FXML private TableColumn<Propriedade, String> descricaoColumn;
	@FXML private TableColumn<Propriedade, String> enderecoColumn;
	@FXML private TableColumn<Propriedade, String> areaColumn;
	@FXML private TableColumn<Propriedade, String> opcoesColumn;

	@FXML
	public void initialize() {

		enderecoColumn.setCellValueFactory(new PropertyValueFactory<Propriedade,String>("endereco"));
		descricaoColumn.setCellValueFactory(new PropertyValueFactory<Propriedade,String>("descricao"));
		areaColumn.setCellValueFactory(new PropertyValueFactory<Propriedade,String>("area"));
		opcoesColumn.setCellValueFactory(new PropertyValueFactory<Propriedade,String>("id"));
		opcoesColumn.setCellFactory(new TableCellOptionSelectFactory<Propriedade,String>(selecionar));
		
		super.initialize((PropriedadeFormController) MainApp.getBean(PropriedadeFormController.class));
		
	}
	
	@Override
	protected void configureDoubleClickTable(){
		// captura o evento de double click da table
		table.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown()	&& event.getClickCount() == 2) {
					handleEdit();
				}
			}

		});
	}

	@Override
	public String getFormName() {
		return "view/propriedade/PropriedadeReducedOverview.fxml";
	}
	
	@Override
	public String getFormTitle() {
		return "Propriedade";
	}
	
	@Override
	@Resource(name = "propriedadeService")
	protected void setService(IService<Integer, Propriedade> service) {
		super.setService(service);
	}
	
}
