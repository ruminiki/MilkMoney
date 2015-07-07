package br.com.milksys.controller.semen;

import java.time.LocalDate;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.CustomAlert;
import br.com.milksys.components.TableCellDateFactory;
import br.com.milksys.controller.AbstractReducedOverviewController;
import br.com.milksys.model.Semen;
import br.com.milksys.model.State;
import br.com.milksys.service.IService;

@Controller
public class SemenReducedOverviewController extends AbstractReducedOverviewController<Integer, Semen> {

	@FXML private TableColumn<Semen, String> descricaoColumn;
	@FXML private TableColumn<Semen, String> touroColumn;
	@FXML private TableColumn<Semen, LocalDate> dataCompraColumn;
	@FXML private TableColumn<Semen, String> quantidadeDisponivelColumn;
	
	@Autowired private SemenOverviewController semenOverviewController;
	@Autowired private SemenFormController semenFormController;
	
	@FXML
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			
			descricaoColumn.setCellValueFactory(new PropertyValueFactory<Semen,String>("descricao"));
			touroColumn.setCellValueFactory(new PropertyValueFactory<Semen,String>("touro"));
			dataCompraColumn.setCellFactory(new TableCellDateFactory<Semen, LocalDate>("dataCompra"));
			quantidadeDisponivelColumn.setCellValueFactory(new PropertyValueFactory<Semen,String>("quantidadeDisponivel"));
			
			super.initialize(semenFormController);
			
		}
		
	}

	@FXML
	private void selecionar(){
		
		if ( table != null && table.getSelectionModel().getSelectedItem() != null ){
			super.closeForm();
		}else{
			CustomAlert.mensagemInfo("Por favor, selecione um sêmen na tabela.");
		}
		
	}

	@FXML
	private void fechar(){
		this.setObject(null);
		super.closeForm();
	}
	
	@Override
	protected void configureDoubleClickTable(){
		// captura o evento de double click da table
		table.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown()	&& event.getClickCount() == 2) {
					selecionar();
				}
			}

		});
	}
	
	@Override
	public String getFormName() {
		return "view/semen/SemenReducedOverview.fxml";
	}
	
	@Override
	protected String getFormTitle() {
		return "Semen";
	}
	
	@Override
	public Semen getObject() {
		return (Semen) super.object;
	}

	@Override
	@Resource(name = "semenService")
	protected void setService(IService<Integer, Semen> service) {
		super.setService(service);
	}
	
}
