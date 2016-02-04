package br.com.milkmoney.controller.raca;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.TableCellOptionSelectFactory;
import br.com.milkmoney.controller.AbstractReducedOverviewController;
import br.com.milkmoney.model.Raca;
import br.com.milkmoney.service.IService;

@Controller
public class RacaReducedOverviewController extends AbstractReducedOverviewController<Integer, Raca> {

	@FXML private TableColumn<Raca, String> idColumn;
	@FXML private TableColumn<Raca, String> descricaoColumn; 
	@FXML private TableColumn<Raca, String> duracaoGestacaoColumn;
	@FXML private TableColumn<Raca, String> opcoesColumn;

	@FXML
	public void initialize() {

		idColumn.setCellValueFactory(new PropertyValueFactory<Raca,String>("id"));
		descricaoColumn.setCellValueFactory(new PropertyValueFactory<Raca,String>("descricao"));
		duracaoGestacaoColumn.setCellValueFactory(new PropertyValueFactory<Raca,String>("duracaoGestacao"));
		opcoesColumn.setCellValueFactory(new PropertyValueFactory<Raca,String>("id"));
		opcoesColumn.setCellFactory(new TableCellOptionSelectFactory<Raca,String>(selecionar));
		super.initialize((RacaFormController) MainApp.getBean(RacaFormController.class));
		
	}

	@Override
	public String getFormName() {
		return "view/raca/RacaReducedOverview.fxml";
	}
	
	@Override
	public String getFormTitle() {
		return "Raça";
	}
	
	@Override
	public Raca getObject() {
		return (Raca) super.object;
	}

	@Override
	@Resource(name = "racaService")
	protected void setService(IService<Integer, Raca> service) {
		super.setService(service);
	}
	
}
