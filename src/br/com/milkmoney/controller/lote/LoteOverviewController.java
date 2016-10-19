package br.com.milkmoney.controller.lote;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Lote;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.service.LoteService;

@Controller
public class LoteOverviewController extends AbstractOverviewController<Integer, Lote>  {
	
	@FXML private TableColumn<Lote, String> descricaoColumn, finalidadeColumn;
	@FXML private ListView<Animal> listAnimais;
	@FXML private Label lblTotalAnimais, lblMediaLactacoes, lblMediaProducao, lblMediaIdade;
	
	@FXML
	public void initialize() {
		
		descricaoColumn.setCellValueFactory(new PropertyValueFactory<Lote,String>("descricao"));
		finalidadeColumn.setCellValueFactory(new PropertyValueFactory<Lote,String>("finalidadeLote"));
		super.initialize( (LoteFormController) MainApp.getBean(LoteFormController.class) );
		
	}
	
	@Override
	protected void selectRowTableHandler(Lote lote) {
		boolean isNewObject = getObject() == null || ( getObject() != null && lote != null && getObject().getId() != lote.getId());
		super.selectRowTableHandler(lote);
		
		if ( isNewObject && lote != null){
			table.getScene().setCursor(Cursor.WAIT);
			listAnimais.setItems(FXCollections.observableArrayList(lote.getAnimais()));
			Task<Void> task = new Task<Void>() {
				@Override
				public Void call() throws InterruptedException {
					try{
						listAnimais.setDisable(true);
						handleSelectItemTable();	
						
						Platform.runLater(new Runnable() {
						    @Override public void run() {
								lblTotalAnimais.setText(String.valueOf(lote.getAnimais() != null ? lote.getAnimais().size() : 0));
								lblMediaIdade.setText(String.valueOf(((LoteService)service).getMediaIdadeAnimais(lote.getAnimais())));
								lblMediaLactacoes.setText(String.valueOf(((LoteService)service).getMediaLactacoesAnimais(lote.getAnimais())));
								lblMediaProducao.setText(String.valueOf(((LoteService)service).getMediaProducaoAnimais(lote.getAnimais())));
						    }
						});
						
					}catch(Exception e){
						e.printStackTrace();
					}
					return null;	
				}
			};
			
			Thread thread = new Thread(task);				
			thread.setDaemon(true);
			thread.start();
			
			task.setOnSucceeded(e -> {
				listAnimais.setDisable(false);
				table.getScene().setCursor(Cursor.DEFAULT);
			});
		}
		
	}
	
	@Override
	public void refreshObjectInTableView(Lote object) {
		super.refreshObjectInTableView(object);
		handleSelectItemTable();
	}
	
	@Override
	public String getFormName() {
		return "view/lote/LoteOverview.fxml";
	}
	
	@Override
	public String getFormTitle() {
		return "Lote";
	}
	
	@Override
	@Resource(name = "loteService")
	protected void setService(IService<Integer, Lote> service) {
		super.setService(service);
	}

	
}
