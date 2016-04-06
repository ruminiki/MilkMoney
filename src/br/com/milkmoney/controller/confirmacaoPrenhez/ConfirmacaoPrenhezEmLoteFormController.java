package br.com.milkmoney.controller.confirmacaoPrenhez;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.components.TableCellDateFactory;
import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Cobertura;
import br.com.milkmoney.model.MetodoConfirmacaoPrenhez;
import br.com.milkmoney.model.SituacaoCobertura;
import br.com.milkmoney.service.CoberturaService;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.util.DateUtil;

@Controller
public class ConfirmacaoPrenhezEmLoteFormController extends AbstractFormController<Integer, Cobertura> {

	@FXML private TableView<Cobertura>           tableCoberturasSelecionados;
	@FXML private TableColumn<Cobertura, String> animalColumn, reprodutorColumn, dataCoberturaColumn, situacaoColumn;
	@FXML private DatePicker                     inputData;
	@FXML private ChoiceBox<String>              inputSituacaoCobertura, inputMetodoConfirmacao;
	@FXML private UCTextField                    inputObservacao;
	
	@Autowired private CoberturaService          coberturaService;
	
	private ObservableList<Cobertura>            coberturasAnimaisSelecionados = FXCollections.observableArrayList();
	
	@FXML
	public void initialize() {
		
		animalColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("femea"));
		dataCoberturaColumn.setCellFactory(new TableCellDateFactory<Cobertura,String>("data"));
		reprodutorColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("reprodutor"));
		situacaoColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("situacaoCobertura"));
		
		inputSituacaoCobertura.setItems(SituacaoCobertura.getItems());
		inputMetodoConfirmacao.setItems(MetodoConfirmacaoPrenhez.getItems());
		
		tableCoberturasSelecionados.setItems(coberturasAnimaisSelecionados);
		
	}
	
	public void setAnimaisSelecionados(ObservableList<Animal> animaisSelecionados) {
		coberturasAnimaisSelecionados.clear();
		//percorrer a lista de animais e buscar a última cobertura de cada um deles
		//tirar da lista os animais que não tem a última cobertura válida (avisar usuário)
		if ( animaisSelecionados != null ){
			
			for ( Animal a : animaisSelecionados ){
				Cobertura cobertura = coberturaService.findLastCoberturaAnimal(a);
				
				if ( cobertura != null && 
						cobertura.getSituacaoCobertura().matches( SituacaoCobertura.NAO_CONFIRMADA + "|" + 
															   SituacaoCobertura.PRENHA + "|" + 
															   SituacaoCobertura.VAZIA ) ){
					coberturasAnimaisSelecionados.add(cobertura);
				}
			}
		}
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
		//setar os dados na cobertura de cada animal
		//salvar cada cobertura
		//fechar a tela e verificar se ocorre o refresh da tabela de animais
		
		for ( Cobertura c : coberturasAnimaisSelecionados ){
			c.setDataConfirmacaoPrenhez(DateUtil.asDate(inputData.getValue()));
			c.setSituacaoConfirmacaoPrenhez(inputSituacaoCobertura.getValue());
			c.setSituacaoCobertura(inputSituacaoCobertura.getValue());
			c.setMetodoConfirmacaoPrenhez(inputMetodoConfirmacao.getValue());
			c.setObservacaoConfirmacaoPrenhez(inputObservacao.getText());
			coberturaService.save(c);
		}
		
		super.closeForm();
		
	}
	
	@Override
	public void handleCancel() {
		super.closeForm();
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
