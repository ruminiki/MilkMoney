package br.com.milkmoney.controller.servico;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.TableCellDateFactory;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.model.PrestadorServico;
import br.com.milkmoney.model.Servico;
import br.com.milkmoney.service.IService;

@Controller
public class ServicoOverviewController extends AbstractOverviewController<Integer, Servico> {

	@FXML private TableColumn<Servico, String> descricaoColumn;
	@FXML private TableColumn<Servico, String> dataColumn;
	@FXML private TableColumn<PrestadorServico, String> prestadorServicoColumn;
	@FXML private TableColumn<Servico, String> valorColumn;
	@FXML private TableColumn<Servico, String> historicoColumn;
	
	@FXML
	public void initialize() {
		
		descricaoColumn.setCellValueFactory(new PropertyValueFactory<Servico,String>("descricao"));
		dataColumn.setCellFactory(new TableCellDateFactory<Servico,String>("data"));
		valorColumn.setCellValueFactory(new PropertyValueFactory<Servico,String>("valor"));
		prestadorServicoColumn.setCellValueFactory(new PropertyValueFactory<PrestadorServico,String>("prestadorServico"));
		historicoColumn.setCellValueFactory(new PropertyValueFactory<Servico,String>("historico"));
		super.initialize((ServicoFormController) MainApp.getBean(ServicoFormController.class));
		
	}
	
	@Override
	public String getFormTitle() {
		return "Serviço";
	}
	
	@Override
	public String getFormName() {
		return "view/servico/ServicoOverview.fxml";
	}
	
	@Override
	@Resource(name = "servicoService")
	protected void setService(IService<Integer, Servico> service) {
		super.setService(service);
	}

}
