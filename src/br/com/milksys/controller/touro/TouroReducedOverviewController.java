package br.com.milksys.controller.touro;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milksys.components.CustomAlert;
import br.com.milksys.controller.AbstractReducedOverviewController;
import br.com.milksys.model.Touro;
import br.com.milksys.service.IService;

@Controller
public class TouroReducedOverviewController extends AbstractReducedOverviewController<Integer, Touro> {

	@FXML private TableColumn<Touro, String> codigoColumn;
	@FXML private TableColumn<Touro, String> nomeColumn;
	
	@Autowired private TouroFormController touroFormController;
	
	@FXML
	public void initialize() {
		
		codigoColumn.setCellValueFactory(new PropertyValueFactory<Touro,String>("codigo"));
		nomeColumn.setCellValueFactory(new PropertyValueFactory<Touro, String>("nome"));
		
		super.initialize(touroFormController);
		
	}

	@FXML
	private void selecionar(){
		
		if ( table != null && table.getSelectionModel().getSelectedItem() != null ){
			super.closeForm();
		}else{
			CustomAlert.mensagemInfo("Por favor, selecione um touro na tabela.");
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
		return "view/touro/TouroReducedOverview.fxml";
	}
	
	@Override
	protected String getFormTitle() {
		return "Touro";
	}
	
	@Override
	public Touro getObject() {
		return (Touro) super.object;
	}

	@Override
	@Resource(name = "touroService")
	protected void setService(IService<Integer, Touro> service) {
		super.setService(service);
	}
	
}
