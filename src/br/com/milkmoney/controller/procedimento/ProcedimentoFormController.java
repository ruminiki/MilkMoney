package br.com.milkmoney.controller.procedimento;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.components.MaskFieldUtil;
import br.com.milkmoney.components.UCTextField;
import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.controller.servico.ServicoFormController;
import br.com.milkmoney.controller.tipoProcedimento.TipoProcedimentoReducedOverviewController;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Limit;
import br.com.milkmoney.model.Procedimento;
import br.com.milkmoney.model.Servico;
import br.com.milkmoney.model.State;
import br.com.milkmoney.model.TipoProcedimento;
import br.com.milkmoney.service.AnimalService;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.util.DateUtil;

@Controller
public class ProcedimentoFormController extends AbstractFormController<Integer, Procedimento>  {
	
	@FXML private UCTextField inputPesquisa, inputDescricao, inputTipoProcedimento, inputServico, inputResponsavel, inputCarencia;
	@FXML private TextArea inputObservacao;
	@FXML private DatePicker inputDataAgendada, inputDataRealizacao;
	@FXML private ListView<Animal> listAnimais, listAnimaisSelecionados;
	@FXML private Button btnAdicionar, btnAdicionarTodos, btnRemover, btnRemoverTodos, btnCadastrarServico, btnRemoverServico;
	
	@Autowired private AnimalService animalService;
	@Autowired private TipoProcedimentoReducedOverviewController tipoProcedimentoReducedController;
	@Autowired private ServicoFormController servicoFormController;

	@FXML
	public void initialize() {
		
		if ( inputPesquisa != null ){
			inputPesquisa.textProperty().addListener((observable, oldValue, newValue) -> {
				listAnimais.setItems(animalService.defaultSearch(newValue, Limit.TRINTA));
			});
		}
		
		listAnimais.setItems(animalService.findAllFemeasAtivasAsObservableList(DateUtil.today, Limit.TRINTA));
		listAnimais.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		listAnimaisSelecionados.setItems(FXCollections.observableArrayList(getObject().getAnimais()));
		
		// captura o evento de double click da table
		listAnimais.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				
				if (event.isPrimaryButtonDown()	&& event.getClickCount() == 2) {
					if ( !animalAlreadySelected(listAnimais.getSelectionModel().getSelectedItem()) ){
						adicionarAnimalAoProcedimento(listAnimais.getSelectionModel().getSelectedItem());
					}
					listAnimais.getSelectionModel().clearSelection();
				}
			}
		});
		
		btnAdicionar.setOnAction(action -> {
			
			if ( listAnimais.getSelectionModel().getSelectedItems() != null ){
				
				for ( Animal animal : listAnimais.getSelectionModel().getSelectedItems() ){
					
					if ( !animalAlreadySelected(animal) ){
						adicionarAnimalAoProcedimento(animal);
					}
					
				}
				
				listAnimais.getSelectionModel().clearSelection();
				
			}
			
		});
		
		btnAdicionarTodos.setOnAction(action -> {
			
			for ( Animal animal : listAnimais.getItems() ){
				
				if ( !animalAlreadySelected(animal) ){
					adicionarAnimalAoProcedimento(animal);
				}
				
			}
			
		});
		
		btnRemover.setOnAction(action -> {
			if ( listAnimaisSelecionados.getSelectionModel().getSelectedItem() != null ){
				removerAnimalProcedimento(listAnimaisSelecionados.getSelectionModel().getSelectedItem());
			}
		});
		
		btnRemoverTodos.setOnAction(action -> {
			listAnimaisSelecionados.getItems().clear();
		});
		
		inputDescricao.textProperty().bindBidirectional(getObject().descricaoProperty());
		inputCarencia.textProperty().bindBidirectional(getObject().diasCarenciaProperty());
		inputResponsavel.textProperty().bindBidirectional(getObject().responsavelProperty());
		inputDataAgendada.valueProperty().bindBidirectional(getObject().dataAgendadaProperty());
		inputDataRealizacao.valueProperty().bindBidirectional(getObject().dataRealizacaoProperty());
		inputObservacao.textProperty().bindBidirectional(getObject().observacaoProperty());
		
		if ( getObject() != null && getObject().getTipoProcedimento() != null ){
			inputTipoProcedimento.setText(getObject().getTipoProcedimento().getDescricao());
		}
		
		if ( getObject() != null && getObject().getServico() != null ){
			inputServico.setText(getObject().getServico().toString());
		}
		
		//só permite números no campo dias de carência
		MaskFieldUtil.numeroInteiroWithouMask(inputCarencia);
		
		habilitaDesabilitaBotoesCadastroServico();
		
	}
	
	private boolean animalAlreadySelected(Animal animal){
		
		for ( Animal a : listAnimaisSelecionados.getItems() ){
			if ( a.getId() == animal.getId() )
				return true;
		}
		
		return false;
	}
	
	private void adicionarAnimalAoProcedimento(Animal animal){
		listAnimaisSelecionados.getItems().add(animal);
		getObject().getAnimais().add(animal);
	}
	
	private void removerAnimalProcedimento(Animal animal){
		listAnimaisSelecionados.getItems().remove(animal);
		getObject().getAnimais().remove(animal);
	}
	
	@FXML
	private void handleSelecionarTipoProcedimento(){
		
		tipoProcedimentoReducedController.setObject(new TipoProcedimento());
		tipoProcedimentoReducedController.showForm();
		
		if ( tipoProcedimentoReducedController.getObject() != null && tipoProcedimentoReducedController.getObject().getId() > 0 ){
			getObject().setTipoProcedimento(tipoProcedimentoReducedController.getObject());
		}
		
		if ( getObject().getTipoProcedimento() != null ){
			inputTipoProcedimento.textProperty().set(getObject().getTipoProcedimento().toString());	
		}else{
			inputTipoProcedimento.textProperty().set("");
		}
		
	}
	
	@FXML
	private void handleCadastrarServico(){
		
		servicoFormController.setState(State.CREATE_TO_SELECT);
		servicoFormController.setObject(new Servico("PROCEDIMENTO " +
				(getObject().getTipoProcedimento() != null ? getObject().getDescricao() : getObject().getTipoProcedimento()), "PROCEDIMENTO ZOOTÉCNICO"));
		servicoFormController.showForm();
		
		getObject().setServico(servicoFormController.getObject());
		inputServico.setText(servicoFormController.getObject().toString());
		habilitaDesabilitaBotoesCadastroServico();
		
	}
	
	@FXML
	private void handleRemoverServico(){
		getObject().setServico(null);
		inputServico.setText(null);
		btnCadastrarServico.requestFocus();
		habilitaDesabilitaBotoesCadastroServico();
	}
	
	private void habilitaDesabilitaBotoesCadastroServico(){
		btnCadastrarServico.setDisable(getObject().getServico() != null);
		btnRemoverServico.setDisable(getObject().getServico() == null);
	}
	
	@Override
	public String getFormName() {
		return "view/procedimento/ProcedimentoForm.fxml";
	}
	
	@Override
	public String getFormTitle() {
		return "Procedimento";
	}
	
	@Override
	@Resource(name = "procedimentoService")
	protected void setService(IService<Integer, Procedimento> service) {
		super.setService(service);
	}

	
}
