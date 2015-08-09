package br.com.milkmoney.controller.tipoProcedimento;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.controller.AbstractReducedOverviewController;
import br.com.milkmoney.model.TipoProcedimento;
import br.com.milkmoney.service.IService;

@Controller
public class TipoProcedimentoReducedOverviewController extends AbstractReducedOverviewController<Integer, TipoProcedimento> {

	@FXML private TableColumn<TipoProcedimento, String> idColumn;
	@FXML private TableColumn<TipoProcedimento, String> descricaoColumn;

	@FXML
	public void initialize() {

		idColumn.setCellValueFactory(new PropertyValueFactory<TipoProcedimento,String>("id"));
		descricaoColumn.setCellValueFactory(new PropertyValueFactory<TipoProcedimento,String>("descricao"));
		super.initialize((TipoProcedimentoFormController) MainApp.getBean(TipoProcedimentoFormController.class));
		
	}

	@Override
	public String getFormName() {
		return "view/tipoProcedimento/TipoProcedimentoReducedOverview.fxml";
	}
	
	@Override
	public String getFormTitle() {
		return "Tipo Procedimento";
	}
	
	@Override
	public TipoProcedimento getObject() {
		return (TipoProcedimento) super.object;
	}

	@Override
	@Resource(name = "tipoProcedimentoService")
	protected void setService(IService<Integer, TipoProcedimento> service) {
		super.setService(service);
	}
	
}
