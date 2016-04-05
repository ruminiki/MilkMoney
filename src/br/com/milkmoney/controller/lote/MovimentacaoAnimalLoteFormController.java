package br.com.milkmoney.controller.lote;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.controller.AbstractFormController;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Lote;
import br.com.milkmoney.service.AnimalService;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.service.LoteService;

@Controller
public class MovimentacaoAnimalLoteFormController extends AbstractFormController<Integer, Lote>  {
	
	@FXML private TextField inputLote;
	@FXML private ListView<Animal> listAnimaisSelecionados;
	@FXML private Label lblTotalAnimais, lblMediaLactacoes, lblMediaProducao, lblMediaIdade;
	
	@Autowired protected AnimalService animalService;
	@Autowired protected LoteReducedOverviewController loteReducedOverviewController;
	
	private ObservableList<Animal> animaisSelecionados;
	private Lote loteSelecionado;

	@FXML
	public void initialize() {
		
		listAnimaisSelecionados.setItems(animaisSelecionados);
		atualizaResumo();
		
	}
	
	private void atualizaResumo(){
		lblTotalAnimais.setText(String.valueOf(animaisSelecionados.size()));
		lblMediaIdade.setText(String.valueOf(((LoteService)service).getMediaIdadeAnimais(animaisSelecionados)));
		lblMediaLactacoes.setText(String.valueOf(((LoteService)service).getMediaLactacoesAnimais(animaisSelecionados)));
		lblMediaProducao.setText(String.valueOf(((LoteService)service).getMediaProducaoAnimais(animaisSelecionados)));
	}
	
	@FXML
	private void handleSelecionarLote() {
		
		loteReducedOverviewController.showForm();
		loteSelecionado = loteReducedOverviewController.getObject();
		if ( loteSelecionado != null )
			inputLote.setText(loteSelecionado.getDescricao());
		
	}
	
	public void setAnimaisSelecionados(ObservableList<Animal> animaisSelecionados) {
		this.animaisSelecionados = animaisSelecionados;
	}
	
	@Override
	protected void handleSave() {
		if ( loteSelecionado != null ){
			for ( Animal a : animaisSelecionados ){
				a.setLote(loteSelecionado);
			}
			
			loteSelecionado.setAnimais(animaisSelecionados);
			service.save(loteSelecionado);
			
			super.closeForm();
		}
	}
	
	@Override
	public void handleCancel() {
		super.closeForm();
	}

	@Override
	public String getFormName() {
		return "view/lote/MovimentacaoAnimalLoteForm.fxml";
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
