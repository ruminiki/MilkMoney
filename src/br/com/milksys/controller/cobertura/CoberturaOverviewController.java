package br.com.milksys.controller.cobertura;

import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.CustomAlert;
import br.com.milksys.components.TableCellDateFactory;
import br.com.milksys.components.TableCellHyperlinkFactory;
import br.com.milksys.components.TableCellSituacaoCoberturaFactory;
import br.com.milksys.controller.AbstractOverviewController;
import br.com.milksys.controller.parto.PartoFormController;
import br.com.milksys.model.Animal;
import br.com.milksys.model.Cobertura;
import br.com.milksys.model.Parto;
import br.com.milksys.model.SituacaoCobertura;
import br.com.milksys.model.State;
import br.com.milksys.model.TipoCobertura;
import br.com.milksys.service.CoberturaService;
import br.com.milksys.service.IService;

@Controller
public class CoberturaOverviewController extends AbstractOverviewController<Integer, Cobertura> {

	@FXML private TableColumn<Cobertura, String> dataColumn;
	@FXML private TableColumn<Animal, String> femeaColumn;
	@FXML private TableColumn<Cobertura, String> reprodutorColumn;
	@FXML private TableColumn<Cobertura, String> previsaoPartoColumn;
	@FXML private TableColumn<Cobertura, String> previsaoSecagemColumn;
	@FXML private TableColumn<TipoCobertura, String> tipoCoberturaColumn;
	@FXML private TableColumn<SituacaoCobertura, String> situacaoCoberturaColumn;
	@FXML private TableColumn<Cobertura, String> repeticaoCioColumn;
	@FXML private TableColumn<Cobertura, String> confirmacaoPrenhezColumn;
	@FXML private TableColumn<Cobertura, String> reconfirmacaoPrenhezColumn;
	
	@Autowired private CoberturaFormController coberturaFormController;
	@Autowired private ConfirmacaoPrenhezFormController confirmacaoPrenhezFormController;
	@Autowired private ReconfirmacaoPrenhezFormController reconfirmacaoPrenhezFormController;
	@Autowired private RepeticaoCioFormController repeticaoCioFormController;
	
	@Autowired private PartoFormController partoFormController;
	
	@FXML
	public void initialize() {
		
		dataColumn.setCellFactory(new TableCellDateFactory<Cobertura,String>("data"));
		femeaColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("femea"));
		reprodutorColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("reprodutor"));
		previsaoPartoColumn.setCellFactory(new TableCellDateFactory<Cobertura,String>("previsaoParto"));
		previsaoSecagemColumn.setCellFactory(new TableCellDateFactory<Cobertura,String>("previsaoSecagem"));
		tipoCoberturaColumn.setCellValueFactory(new PropertyValueFactory<TipoCobertura,String>("tipoCobertura"));
		
		//situacaoCoberturaColumn.setCellValueFactory(new PropertyValueFactory<SituacaoCobertura,String>("situacaoCobertura"));
		situacaoCoberturaColumn.setCellFactory(new TableCellSituacaoCoberturaFactory<SituacaoCobertura,String>("situacaoCobertura"));
		
		repeticaoCioColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("repeticao"));
		repeticaoCioColumn.setCellFactory(new TableCellHyperlinkFactory<Cobertura, String>(repeticaoCioHandler));
		
		confirmacaoPrenhezColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("confirmacaoPrenhez"));
		confirmacaoPrenhezColumn.setCellFactory(new TableCellHyperlinkFactory<Cobertura, String>(confirmacaoPrenhezHandler));
		
		reconfirmacaoPrenhezColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("reconfirmacaoPrenhez"));
		reconfirmacaoPrenhezColumn.setCellFactory(new TableCellHyperlinkFactory<Cobertura, String>(reconfirmacaoPrenhezPrenhezHandler));
		
		super.initialize(coberturaFormController);
			
	}
	
	@Override
	protected String getFormTitle() {
		return "Cobertura";
	}
	
	@Override
	protected String getFormName() {
		return "view/cobertura/CoberturaOverview.fxml";
	}

	@Override
	@Resource(name = "coberturaService")
	protected void setService(IService<Integer, Cobertura> service) {
		super.setService(service);
	}
	
	//====HANDLERS
	
	@FXML
	private void handleRegistrarParto(){
		
		if ( table.getSelectionModel().getSelectedItem() == null ){
			CustomAlert.nenhumRegistroSelecionado();
			return;
		}
		
		if ( getObject().getSituacaoCobertura().equals(SituacaoCobertura.PRENHA) ||
				getObject().getSituacaoCobertura().equals(SituacaoCobertura.INDEFINIDA) ||
						getObject().getSituacaoCobertura().equals(SituacaoCobertura.PARIDA) ){
			
			partoFormController.setState(State.CREATE_TO_SELECT);
			
			if ( getObject().getParto() != null && getObject().getParto().getId() > 0 ){
				partoFormController.setObject(getObject().getParto());
			}else{
				partoFormController.setObject(new Parto(getObject()));
			}
			
			partoFormController.showForm();
			
			if ( partoFormController.getObject() != null ){
				getObject().setParto(partoFormController.getObject());
				((CoberturaService)service).registrarParto(getObject());
			}	
		}else{
			CustomAlert.mensagemAlerta("Regra de Negócio", "A cobertura selecionada tem situação igual a " + getObject().getSituacaoCobertura() + 
					" não sendo possível registrar o parto.");
		}
		
	}
	
	@FXML
	private void handleRemoverParto(){
		
		if ( table.getSelectionModel().getSelectedItem() == null ){
			CustomAlert.nenhumRegistroSelecionado();
			return;
		}
		
		if ( getObject().getParto() != null && getObject().getParto().getId() > 0 ){
			
			Optional<ButtonType> result = CustomAlert.confirmarExclusao("Confirmar remoção registro", "Tem certeza que deseja remover o parto registrado?");
			if (result.get() == ButtonType.OK) {
				((CoberturaService)service).removerParto(getObject());
			}
			
		}else{
			CustomAlert.mensagemAlerta("Regra de Negócio", "A cobertura selecionada não tem parto registrado.");
		}
		
	}
	
	EventHandler<ActionEvent> confirmacaoPrenhezHandler = new EventHandler<ActionEvent>() {
	    @Override public void handle(ActionEvent e) {
	    	if ( table.getSelectionModel().getSelectedItem() == null ){
				CustomAlert.nenhumRegistroSelecionado();
				return;
			}
			
			if ( getObject().getSituacaoCobertura().equals(SituacaoCobertura.PARIDA) ){
				CustomAlert.mensagemInfo("A cobertura já tem parto registrado, não sendo possível alteração.");
				return;
			}
			
			confirmacaoPrenhezFormController.setObject(getObject());
	    	confirmacaoPrenhezFormController.showForm();
	    }
	};
	
	EventHandler<ActionEvent> reconfirmacaoPrenhezPrenhezHandler = new EventHandler<ActionEvent>() {
	    @Override public void handle(ActionEvent e) {
	    	if ( table.getSelectionModel().getSelectedItem() == null ){
				CustomAlert.nenhumRegistroSelecionado();
				return;
			}
			
			if ( getObject().getSituacaoCobertura().equals(SituacaoCobertura.PARIDA) ){
				CustomAlert.mensagemInfo("A cobertura já tem parto registrado, não sendo possível alteração.");
				return;
			}

			reconfirmacaoPrenhezFormController.setObject(getObject());
	    	reconfirmacaoPrenhezFormController.showForm();
	    }
	};
	
	EventHandler<ActionEvent> repeticaoCioHandler = new EventHandler<ActionEvent>() {
	    @Override public void handle(ActionEvent e) {
	    	if ( table.getSelectionModel().getSelectedItem() == null ){
				CustomAlert.nenhumRegistroSelecionado();
				return;
			}
			
			if ( getObject().getSituacaoCobertura().equals(SituacaoCobertura.PARIDA) ){
				CustomAlert.mensagemInfo("A cobertura já tem parto registrado, não sendo possível alteração.");
				return;
			}

			repeticaoCioFormController.setObject(getObject());
	    	repeticaoCioFormController.showForm();
	    }
	};
	
}
