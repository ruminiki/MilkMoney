package br.com.milkmoney.controller.servico;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.MaskFieldUtil;
import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.controller.prestadorServico.PrestadorServicoReducedOverviewController;
import br.com.milkmoney.model.PrestadorServico;
import br.com.milkmoney.model.Servico;
import br.com.milkmoney.service.IService;

@Controller
public class ServicoFormController extends AbstractFormController<Integer, Servico> {

	@FXML private UCTextField inputDescricao, inputPrestadorServico, inputValor;
	@FXML private DatePicker inputData;

	@Autowired private PrestadorServicoReducedOverviewController prestadorServicoReducedOverviewController;
	
	@FXML
	public void initialize() {
		
		inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
		inputData.valueProperty().bindBidirectional(getObject().dataProperty());
		
		if ( getObject().getPrestadorServico() != null ){
			inputPrestadorServico.setText(getObject().getPrestadorServico().toString());
		}
		
		inputValor.textProperty().bindBidirectional(getObject().valorProperty());
		MaskFieldUtil.decimal(inputValor);
		
	}
	
	@Override
	public String getFormName() {
		return "view/servico/ServicoForm.fxml";
	}

	@Override
	public String getFormTitle() {
		return "Serviço";
	}
	
	@FXML
	protected void handleSelecionarPrestadorServico() {
		prestadorServicoReducedOverviewController.setObject(new PrestadorServico());
		prestadorServicoReducedOverviewController.showForm();
		if ( prestadorServicoReducedOverviewController.getObject() != null && prestadorServicoReducedOverviewController.getObject().getId() > 0 ){
			getObject().setPrestadorServico(prestadorServicoReducedOverviewController.getObject());
		}
		
		if ( getObject().getPrestadorServico() != null ) {
			inputPrestadorServico.setText(getObject().getPrestadorServico().toString());
		}else{
			inputPrestadorServico.setText(null);
		}
		
	}
	
	@Override
	@Resource(name = "servicoService")
	protected void setService(IService<Integer, Servico> service) {
		super.setService(service);
	}
	
}
