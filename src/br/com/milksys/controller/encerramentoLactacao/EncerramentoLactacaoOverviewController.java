package br.com.milksys.controller.encerramentoLactacao;

import java.util.Date;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milksys.MainApp;
import br.com.milksys.components.TableCellDateFactory;
import br.com.milksys.controller.AbstractOverviewController;
import br.com.milksys.model.Animal;
import br.com.milksys.model.EncerramentoLactacao;
import br.com.milksys.service.IService;

@Controller
public class EncerramentoLactacaoOverviewController extends AbstractOverviewController<Integer, EncerramentoLactacao> {

	@FXML private TableColumn<EncerramentoLactacao, Date> dataEncerramentoColumn;
	@FXML private TableColumn<EncerramentoLactacao, Date> dataPrevisaoPartoColumn;
	@FXML private TableColumn<EncerramentoLactacao, String> diasParaPartoColumn;
	@FXML private TableColumn<Animal, String> animalColumn;
	@FXML private TableColumn<EncerramentoLactacao, String> motivoEncerramentoColumn;
	
	@FXML
	public void initialize() {
		
		dataEncerramentoColumn.setCellFactory(new TableCellDateFactory<EncerramentoLactacao,Date>("data"));
		dataPrevisaoPartoColumn.setCellFactory(new TableCellDateFactory<EncerramentoLactacao,Date>("dataPrevisaoParto"));
		diasParaPartoColumn.setCellValueFactory(new PropertyValueFactory<EncerramentoLactacao,String>("diasParaParto"));
		animalColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("animal"));
		motivoEncerramentoColumn.setCellValueFactory(new PropertyValueFactory<EncerramentoLactacao,String>("motivoEncerramentoLactacao"));
		
		super.initialize((EncerramentoLactacaoFormController)MainApp.getBean(EncerramentoLactacaoFormController.class));
		
	}

	@Override
	protected String getFormTitle() {
		return "Encerramento Lactação";
	}
	
	@Override
	protected String getFormName() {
		return "view/encerramentoLactacao/EncerramentoLactacaoOverview.fxml";
	}
	
	@Override
	@Resource(name = "encerramentoLactacaoService")
	protected void setService(IService<Integer, EncerramentoLactacao> service) {
		super.setService(service);
	}
	
}
