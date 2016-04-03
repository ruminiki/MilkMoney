package br.com.milkmoney.controller.motivoEncerramentoLactacao;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.controller.AbstractReducedOverviewController;
import br.com.milkmoney.model.MotivoEncerramentoLactacao;
import br.com.milkmoney.service.IService;

@Controller
public class MotivoEncerramentoLactacaoReducedOverviewController extends AbstractReducedOverviewController<Integer, MotivoEncerramentoLactacao> {

	@FXML private TableColumn<MotivoEncerramentoLactacao, String> idColumn;
	@FXML private TableColumn<MotivoEncerramentoLactacao, String> descricaoColumn; 

	@FXML
	public void initialize() {

		idColumn.setCellValueFactory(new PropertyValueFactory<MotivoEncerramentoLactacao,String>("id"));
		descricaoColumn.setCellValueFactory(new PropertyValueFactory<MotivoEncerramentoLactacao,String>("descricao"));
		super.initialize((MotivoEncerramentoLactacaoFormController) MainApp.getBean(MotivoEncerramentoLactacaoFormController.class));
		
	}

	@Override
	public String getFormName() {
		return "view/motivoEncerramentoLactacao/MotivoEncerramentoLactacaoReducedOverview.fxml";
	}
	
	@Override
	public String getFormTitle() {
		return "Motivo Encerramento Lactação";
	}
	
	@Override
	@Resource(name = "motivoEncerramentoLactacaoService")
	protected void setService(IService<Integer, MotivoEncerramentoLactacao> service) {
		super.setService(service);
	}
	
}
