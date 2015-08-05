package br.com.milkmoney.controller.procedimento;

import java.util.Date;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.TableCellDateFactory;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.model.Procedimento;
import br.com.milkmoney.service.IService;

@Controller
public class ProcedimentoOverviewController extends AbstractOverviewController<Integer, Procedimento>  {
	
	@FXML private TableColumn<Procedimento, String> descricaoColumn;
	@FXML private TableColumn<Procedimento, Date>   dataColumn;
	@FXML private TableColumn<Procedimento, String> tipoProcedimentoColumn;
	@FXML private TableColumn<Procedimento, String> responsavelColumn;
	
	@FXML
	public void initialize() {
		
		dataColumn.setCellFactory(new TableCellDateFactory<Procedimento,Date>("dataRealizacao"));
		descricaoColumn.setCellValueFactory(new PropertyValueFactory<Procedimento,String>("descricao"));
		tipoProcedimentoColumn.setCellValueFactory(new PropertyValueFactory<Procedimento,String>("tipoProcedimento"));
		responsavelColumn.setCellValueFactory(new PropertyValueFactory<Procedimento,String>("responsavel"));
		super.initialize( (ProcedimentoFormController) MainApp.getBean(ProcedimentoFormController.class) );
		
	}
	
	@Override
	protected String getFormName() {
		return "view/procedimento/ProcedimentoOverview.fxml";
	}
	
	@Override
	protected String getFormTitle() {
		return "Procedimento";
	}
	
	@Override
	@Resource(name = "procedimentoService")
	protected void setService(IService<Integer, Procedimento> service) {
		super.setService(service);
	}

	
}
