package br.com.milksys.controller;

import java.time.LocalDate;

import javafx.collections.ListChangeListener;
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
import br.com.milksys.model.Semen;
import br.com.milksys.model.State;
import br.com.milksys.service.IService;

@Controller
public class SemenReducedController extends AbstractController<Integer, Semen> {

	@FXML private TableColumn<Semen, String> descricaoColumn;
	@FXML private TableColumn<Semen, String> touroColumn;
	@FXML private TableColumn<Semen, LocalDate> dataCompraColumn;
	@FXML private TableColumn<Semen, String> quantidadeColumn;
	
	@Autowired private SemenController semenController;
	
	@FXML
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			
			descricaoColumn.setCellValueFactory(new PropertyValueFactory<Semen,String>("descricao"));
			touroColumn.setCellValueFactory(new PropertyValueFactory<Semen,String>("touro"));
			dataCompraColumn.setCellFactory(new TableCellDateFactory<Semen, LocalDate>("dataCompra"));
			quantidadeColumn.setCellValueFactory(new PropertyValueFactory<Semen,String>("quantidade"));
			
			semenController.data.addListener(new ListChangeListener<Semen>(){
				@Override
				public void onChanged(ListChangeListener.Change<? extends Semen> c) {
						data.clear();
						data.addAll(semenController.data);
				}
			});
			
			super.initialize();
			
		}
		
	}

	@FXML
	private void selecionar(){
		
		if ( table != null && table.getSelectionModel().getSelectedItem() != null ){
			super.dialogStage.close();
		}else{
			CustomAlert.mensagemInfo("Por favor, selecione um sêmen na tabela.");
		}
		
	}

	@FXML
	private void fechar(){
		this.setObject(null);
		super.dialogStage.close();
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
	protected void handleNew() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		semenController.handleNew();
	}
	
	@Override
	protected void handleEdit() {
		if ( table != null && table.getSelectionModel().getSelectedItem() != null ){
			semenController.setObject(table.getSelectionModel().getSelectedItem());
			semenController.handleEdit();
		}else{
			CustomAlert.mensagemInfo("Por favor, selecione um sêmen na tabela.");
		}
	}
	
	@Override
	protected void handleDelete() {
		int index = table.getSelectionModel().getSelectedIndex();
		super.handleDelete();
		if ( semenController.data.size() >= index )
			semenController.data.remove(index);
	}
	
	@Override
	protected String getFormName() {
		return "view/semen/SemenReducedOverview.fxml";
	}
	
	@Override
	protected String getFormTitle() {
		return "Semen";
	}
	
	@Override
	protected Semen getObject() {
		return (Semen) super.object;
	}

	@Override
	@Resource(name = "semenService")
	protected void setService(IService<Integer, Semen> service) {
		super.setService(service);
	}
	
}
