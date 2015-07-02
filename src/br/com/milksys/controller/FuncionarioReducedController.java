package br.com.milksys.controller;

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
import br.com.milksys.model.Funcionario;
import br.com.milksys.model.State;
import br.com.milksys.service.IService;

@Controller
public class FuncionarioReducedController extends AbstractController<Integer, Funcionario> {

	@FXML private TableColumn<Funcionario, String> nomeColumn;
	@FXML private TableColumn<Funcionario, String> telefoneColumn;
	@FXML private TableColumn<Funcionario, String> emailColumn;
	
	@Autowired private FuncionarioController funcionarioController;
	
	@FXML
	public void initialize() {
		
		if ( state.equals(State.LIST) ){
			nomeColumn.setCellValueFactory(new PropertyValueFactory<Funcionario,String>("nome"));
			telefoneColumn.setCellValueFactory(new PropertyValueFactory<Funcionario,String>("telefone"));
			emailColumn.setCellValueFactory(new PropertyValueFactory<Funcionario,String>("email"));
			
			funcionarioController.data.addListener(new ListChangeListener<Funcionario>(){
				@Override
				public void onChanged(ListChangeListener.Change<? extends Funcionario> c) {
						data.clear();
						data.addAll(funcionarioController.data);
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
			CustomAlert.mensagemInfo("Por favor, selecione um funcionário na tabela.");
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
		funcionarioController.handleNew();
	}
	
	@Override
	protected void handleEdit() {
		if ( table != null && table.getSelectionModel().getSelectedItem() != null ){
			funcionarioController.setObject(table.getSelectionModel().getSelectedItem());
			funcionarioController.handleEdit();
		}else{
			CustomAlert.mensagemInfo("Por favor, selecione um funcionário na tabela.");
		}
	}
	
	@Override
	protected void handleDelete() {
		int index = table.getSelectionModel().getSelectedIndex();
		super.handleDelete();
		if ( funcionarioController.data.size() >= index )
			funcionarioController.data.remove(index);
	}
	
	@Override
	protected String getFormName() {
		return "view/funcionario/FuncionarioReducedOverview.fxml";
	}

	@Override
	protected String getFormTitle() {
		return "Funcionário";
	}
	
	@Override
	protected Funcionario getObject() {
		return (Funcionario)super.object;
	}

	@Override
	@Resource(name = "funcionarioService")
	protected void setService(IService<Integer, Funcionario> service) {
		super.setService(service);
	}

}
