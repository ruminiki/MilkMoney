package br.com.milkmoney.controller.lactacao;

import java.util.Optional;
import java.util.function.Function;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.components.TableCellDateFactory;
import br.com.milkmoney.components.TableCellIndexFactory;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.controller.lactacao.renderer.TableCellOpcoesFactory;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Lactacao;
import br.com.milkmoney.service.LactacaoService;

@Controller
public class LactacaoOverviewController extends AbstractOverviewController<Integer, Lactacao> {

	@FXML private TableColumn<Lactacao, String> numeroLactacaoColumn;
	@FXML private TableColumn<Lactacao, String> dataInicioLactacaoColumn;
	@FXML private TableColumn<Lactacao, String> dataTerminoLactacaoColumn;
	@FXML private TableColumn<Lactacao, String> diasEmLactacaoColumn;
	@FXML private TableColumn<Lactacao, String> mesesEmLactacaoColumn;
	@FXML private TableColumn<Lactacao, String> mediaProducaoColumn;
	@FXML private TableColumn<Lactacao, String> opcoesColumn;
	
	@FXML private Label lblHeader;
	
	@Autowired LactacaoService service;
	@Autowired LactacaoFormController formController;
	private Animal animal;
	
	@FXML
	public void initialize() {
		
		//tabela lactações
		numeroLactacaoColumn.setCellFactory(new TableCellIndexFactory<Lactacao,String>());
		dataInicioLactacaoColumn.setCellFactory(new TableCellDateFactory<Lactacao,String>("dataInicio"));
		dataTerminoLactacaoColumn.setCellFactory(new TableCellDateFactory<Lactacao,String>("dataFim"));
		diasEmLactacaoColumn.setCellValueFactory(new PropertyValueFactory<Lactacao,String>("diasLactacao"));
		mesesEmLactacaoColumn.setCellValueFactory(new PropertyValueFactory<Lactacao,String>("mesesLactacao"));
		mediaProducaoColumn.setCellValueFactory(new PropertyValueFactory<Lactacao,String>("mediaProducao"));
		opcoesColumn.setCellValueFactory(new PropertyValueFactory<Lactacao,String>("diasLactacao"));
		opcoesColumn.setCellFactory(new TableCellOpcoesFactory<Lactacao,String>(encerrarLactacaoAnimal, desfazerEncerramentoLactacaoAnimal));
		
		lblHeader.setText(animal != null ? "LACTAÇÕES " + animal.toString() : "LACTAÇÕES");
		
		super.service = service;
		super.initialize(formController);
		
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}
	
	
	Function<Integer, Boolean> encerrarLactacaoAnimal = index -> {
		if ( table.getSelectionModel().getSelectedItem() != null ){
			formController.setObject(table.getSelectionModel().getSelectedItem());
			formController.showForm();
			refreshObjectInTableView.apply(service.findById(getObject().getId()));
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		return true;
	};
	
	Function<Integer, Boolean> desfazerEncerramentoLactacaoAnimal = index -> {
		if ( table.getSelectionModel().getSelectedItem() != null ){
			Optional<ButtonType> result = CustomAlert.confirmar("Desfazer Encerramento Lactação", "Tem certeza que deseja desfazer o encerramento da lactação?");
			if (result.get() == ButtonType.OK) {
				service.desfazerEncerramentoLactacao(table.getSelectionModel().getSelectedItem());
			}
			refreshObjectInTableView.apply(service.findById(getObject().getId()));
		}else{
			CustomAlert.nenhumRegistroSelecionado();
		}
		return true;
	};
	
	@Override
	protected void refreshTableOverview() {
		this.data.clear();
		this.table.getItems().clear();
		this.data.addAll(service.findLactacoesAnimal(animal));
		this.table.setItems(data);
		this.table.layout();
	}

	@Override
	public String getFormTitle() {
		return "Lactações do Animal";
	}
	
	@Override
	public String getFormName() {
		return "view/lactacao/LactacaoOverview.fxml";
	}
	
}
