package br.com.milkmoney.controller.lote;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
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
	protected void handleSelectItemTable() {
		super.handleSelectItemTable();
		if ( getObject() != null ){
			
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					listAnimais.setItems(FXCollections.observableArrayList(getObject().getAnimais()));
					lblTotalAnimais.setText(String.valueOf(getObject().getAnimais().size()));
					lblMediaIdade.setText(String.valueOf(((LoteService)service).getMediaIdadeAnimais(getObject().getAnimais())));
					lblMediaLactacoes.setText(String.valueOf(((LoteService)service).getMediaLactacoesAnimais(getObject().getAnimais())));
					lblMediaProducao.setText(String.valueOf(((LoteService)service).getMediaProducaoAnimais(getObject().getAnimais())));						
				}
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
