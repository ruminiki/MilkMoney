package br.com.milksys.controller.cobertura;

import java.time.LocalDate;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.CustomAlert;
import br.com.milksys.components.TableCellDateFactory;
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
	@FXML private TableColumn<TipoCobertura, String> tipoCoberturaColumn;
	@FXML private TableColumn<SituacaoCobertura, String> situacaoCoberturaColumn;
	@FXML private TableColumn<Cobertura, LocalDate> repeticaoCioColumn;
	@FXML private TableColumn<Cobertura, String> primeiroToqueColumn;
	@FXML private TableColumn<Cobertura, String> reconfirmacaoColumn;
	
	@Autowired private CoberturaFormController coberturaFormController;
	@Autowired private PrimeiroToqueFormController primeiroToqueFormController;
	@Autowired private ReconfirmacaoFormController reconfirmacaoFormController;
	@Autowired private RepeticaoCioFormController repeticaoCioFormController;
	
	@Autowired private PartoFormController partoFormController;
	
	@FXML
	public void initialize() {
		
		dataColumn.setCellFactory(new TableCellDateFactory<Cobertura,String>("data"));
		femeaColumn.setCellValueFactory(new PropertyValueFactory<Animal,String>("femea"));
		reprodutorColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("reprodutor"));
		previsaoPartoColumn.setCellFactory(new TableCellDateFactory<Cobertura,String>("previsaoParto"));
		tipoCoberturaColumn.setCellValueFactory(new PropertyValueFactory<TipoCobertura,String>("tipoCobertura"));
		situacaoCoberturaColumn.setCellValueFactory(new PropertyValueFactory<SituacaoCobertura,String>("situacaoCobertura"));
		situacaoCoberturaColumn.setCellFactory(new Callback<TableColumn<SituacaoCobertura,String>, TableCell<SituacaoCobertura,String>>(){
			@Override
			public TableCell<SituacaoCobertura, String> call(TableColumn<SituacaoCobertura, String> param) {
				TableCell<SituacaoCobertura, String> cell = new TableCell<SituacaoCobertura, String>(){
					@Override
					public void updateItem(String item, boolean empty) {
						if ( table.getItems().size() > tableRowProperty().get().getIndex() ){
							if(item!=null){
								
								HBox cell = new HBox();
								cell.setAlignment(Pos.CENTER_LEFT);
								cell.setSpacing(2);
								
								HBox color= new HBox();
								color.setMinWidth(10);
								color.setMaxWidth(10);
								if ( item.equals(SituacaoCobertura.PARIDA) )
									color.setStyle("-fx-background-color: #CCFF99");
								if ( item.equals(SituacaoCobertura.VAZIA) || item.equals(SituacaoCobertura.REPETIDA) )
									color.setStyle("-fx-background-color: #FF6600");
								if ( item.equals(SituacaoCobertura.PRENHA) )
									color.setStyle("-fx-background-color: #FFCC00");
								if ( item.equals(SituacaoCobertura.INDEFINIDA) )
									color.setStyle("-fx-background-color: #7C867C");
								cell.getChildren().add(color);
								cell.getChildren().add(new Label(item));
								
								setGraphic(cell);
								
							} 
						}
					}
				};                           
				return cell;
			}
		});
		repeticaoCioColumn.setCellFactory(new TableCellDateFactory<Cobertura,LocalDate>("dataRepeticaoCio"));
		primeiroToqueColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("primeiroToque"));
		reconfirmacaoColumn.setCellValueFactory(new PropertyValueFactory<Cobertura,String>("reconfirmacao"));

		super.initialize(coberturaFormController);
			
	}
	
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
	
	@FXML
	private void openRegistroPrimeiroToqueForm(){
		
		if ( table.getSelectionModel().getSelectedItem() == null ){
			CustomAlert.nenhumRegistroSelecionado();
			return;
		}
		primeiroToqueFormController.setObject(getObject());
    	primeiroToqueFormController.showForm();
    	
	}
	
	@FXML
	private void openRegistroReconfirmacaoForm(){
		
		if ( table.getSelectionModel().getSelectedItem() == null ){
			CustomAlert.nenhumRegistroSelecionado();
			return;
		}
		reconfirmacaoFormController.setObject(getObject());
    	reconfirmacaoFormController.showForm();
    	
	}
	
	@FXML
	private void openRegistroRepeticaoCioForm(){
		
		if ( table.getSelectionModel().getSelectedItem() == null ){
			CustomAlert.nenhumRegistroSelecionado();
			return;
		}
		repeticaoCioFormController.setObject(getObject());
    	repeticaoCioFormController.showForm();
    	
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
	
}
