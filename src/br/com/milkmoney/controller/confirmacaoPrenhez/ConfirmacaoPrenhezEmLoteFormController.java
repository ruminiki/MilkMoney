package br.com.milkmoney.controller.confirmacaoPrenhez;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.components.TableCellDateFactory;
import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.controller.cobertura.renderer.TableCellRegistrarPartoHyperlinkFactory;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.service.CoberturaService;
import br.com.milkmoney.service.IService;

@Controller
public class ConfirmacaoPrenhezEmLoteFormController extends AbstractFormController<Integer, Cobertura> {

	@FXML private TableView<Animal>              tableAnimaisSelecionados;
	@FXML private TableColumn<Cobertura, String> animalColumn;
	@FXML private TableColumn<Cobertura, String> reprodutorColumn;
	@FXML private TableColumn<Cobertura, String> dataCoberturaColumn;
	@FXML private DatePicker                     inputData;
	@FXML private ChoiceBox<String>              inputSituacaoCobertura, inputMetodoConfirmacao;
	@FXML private UCTextField                    inputObservacao;
	
	private ObservableList<Animal>               animaisSelecionados;
	
	@FXML
	public void initialize() {
		
		animalColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("animal"));
		dataCoberturaColumn.setCellFactory(new TableCellDateFactory<Cobertura,String>("data"));
		reprodutorColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("reprodutor"));
		
		tableAnimaisSelecionados.setItems(animaisSelecionados);
		
	}
	
	public void setAnimaisSelecionados(ObservableList<Animal> animaisSelecionados) {
		this.animaisSelecionados = animaisSelecionados;
	}
	
	@FXML
	private void handleDesfazerConfirmacao(){
		CustomAlert.confirmarExclusao("Desfazer Confirmação de Prenhez", "Tem certeza que deseja desfazer a confirmação de prenhez?");
		if (CustomAlert.response == ButtonType.OK) {
			((CoberturaService)service).desfazerConfirmacaoPrenhez(getObject());
			closeForm();
		}
	}
	
	@Override
	protected void handleSave() {
		//percorrer a lista de animais e buscar a última cobertura de cada um deles
		//tirar da lista os animais que não tem a última cobertura válida (avisar usuário)
		//setar os dados na cobertura de cada animal
		//salvar cada cobertura
		//fechar a tela e verificar se ocorre o refresh da tabela de animais
	}
	
	@Override
	public String getFormName() {
		return "view/confirmacaoPrenhez/ConfirmacaoPrenhezEmLoteForm.fxml";
	}

	@Override
	public String getFormTitle() {
		return "Confirmação Prenhez";
	}
	
	@Override
	@Resource(name = "coberturaService")
	protected void setService(IService<Integer, Cobertura> service) {
		super.setService(service);
	}

}
