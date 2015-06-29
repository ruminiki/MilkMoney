package br.com.milksys.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.MaskFieldUtil;
import br.com.milksys.components.TableCellDateFactory;
import br.com.milksys.components.UCTextField;
import br.com.milksys.model.PrestadorServico;
import br.com.milksys.model.Servico;
import br.com.milksys.model.State;
import br.com.milksys.service.IService;
import br.com.milksys.service.PrestadorServicoService;

@Controller
public class ServicoController extends AbstractController<Integer, Servico> {

	@FXML private TableColumn<Servico, String> descricaoColumn;
	@FXML private TableColumn<Servico, String> dataColumn;
	@FXML private TableColumn<PrestadorServico, String> prestadorServicoColumn;
	@FXML private TableColumn<Servico, String> valorColumn;
	@FXML private TableColumn<Servico, String> historicoColumn;
	
	@FXML private UCTextField inputDescricao;
	@FXML private DatePicker inputData;
	@FXML private ComboBox<PrestadorServico> inputPrestadorServico;
	@FXML private UCTextField inputValor;

	@Autowired private PrestadorServicoService prestadorServicoService;
	@Autowired private PrestadorServicoController prestadorServicoController;
	
	@FXML
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			descricaoColumn.setCellValueFactory(new PropertyValueFactory<Servico,String>("descricao"));
			dataColumn.setCellFactory(new TableCellDateFactory<Servico,String>("data"));
			valorColumn.setCellValueFactory(new PropertyValueFactory<Servico,String>("valor"));
			prestadorServicoColumn.setCellValueFactory(new PropertyValueFactory<PrestadorServico,String>("prestadorServico"));
			historicoColumn.setCellValueFactory(new PropertyValueFactory<Servico,String>("historico"));
			super.initialize();
		}
		
		if ( state.equals(State.INSERT) || state.equals(State.UPDATE) || state.equals(State.INSERT_TO_SELECT) ){
			inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
			inputData.valueProperty().bindBidirectional(getObject().dataProperty());
			
			inputPrestadorServico.setItems(prestadorServicoService.findAllAsObservableList());
			inputPrestadorServico.valueProperty().bindBidirectional(getObject().prestadorServicoProperty());
			
			inputValor.textProperty().bindBidirectional(getObject().valorProperty());
			MaskFieldUtil.moeda(inputValor);
		}
		
	}

	@Override
	protected boolean isInputValid() {
		return true;
	}

	@Override
	protected String getFormName() {
		return "view/servico/ServicoForm.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Serviço";
	}
	
	@Override
	protected Servico getObject() {
		return (Servico)super.object;
	}

	@Override
	@Resource(name = "servicoService")
	protected void setService(IService<Integer, Servico> service) {
		super.setService(service);
	}
	
	@FXML
	protected void openFormPrestadorServicoToInsertAndSelect() {
		prestadorServicoController.state = State.INSERT_TO_SELECT;
		prestadorServicoController.object = new PrestadorServico();
		prestadorServicoController.showForm(null);
		if ( prestadorServicoController.getObject() != null ){
			inputPrestadorServico.getItems().add(prestadorServicoController.getObject());
			inputPrestadorServico.getSelectionModel().select(prestadorServicoController.getObject());
		}
	}

}
