package br.com.milkmoney.controller.animal;

import java.util.function.Function;

import javafx.fxml.FXML;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.milkmoney.MainApp;
import br.com.milkmoney.components.CustomAlert;
import br.com.milkmoney.components.CustomTreeItem;
import br.com.milkmoney.controller.AbstractOverviewController;
import br.com.milkmoney.controller.aborto.AbortoFormController;
import br.com.milkmoney.controller.cobertura.CoberturaFormController;
import br.com.milkmoney.controller.cobertura.CoberturaOverviewController;
import br.com.milkmoney.controller.confirmacaoPrenhes.ConfirmacaoPrenhesFormController;
import br.com.milkmoney.controller.fichaAnimal.FichaAnimalOverviewController;
import br.com.milkmoney.controller.indicador.IndicadorOverviewController;
import br.com.milkmoney.controller.lactacao.LactacaoOverviewController;
import br.com.milkmoney.controller.morteAnimal.MorteAnimalFormController;
import br.com.milkmoney.controller.parto.PartoFormController;
import br.com.milkmoney.controller.producaoIndividual.ProducaoIndividualOverviewController;
import br.com.milkmoney.controller.raca.RacaOverviewController;
import br.com.milkmoney.controller.raca.RacaReducedOverviewController;
import br.com.milkmoney.controller.touro.TouroReducedOverviewController;
import br.com.milkmoney.controller.vendaAnimal.VendaAnimalFormController;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.MorteAnimal;
import br.com.milkmoney.model.SituacaoAnimal;
import br.com.milkmoney.model.VendaAnimal;
import br.com.milkmoney.service.AnimalService;
import br.com.milkmoney.service.CoberturaService;
import br.com.milkmoney.service.FichaAnimalService;
import br.com.milkmoney.service.IService;
import br.com.milkmoney.service.LactacaoService;
import br.com.milkmoney.service.MorteAnimalService;
import br.com.milkmoney.service.ParametroService;
import br.com.milkmoney.service.PartoService;
import br.com.milkmoney.service.ProcedimentoService;
import br.com.milkmoney.service.RelatorioService;
import br.com.milkmoney.service.VendaAnimalService;

@Controller
public class PainelControleAnimalController extends AbstractOverviewController<Integer, Animal> {

	@FXML private TreeView<String> menu;
	@FXML private VBox vbMain;
	
	//services
	@Autowired private MorteAnimalService morteAnimalService;
	@Autowired private VendaAnimalService vendaAnimalService;
	@Autowired private FichaAnimalService fichaAnimalService;
	@Autowired private LactacaoService lactacaoService;
	@Autowired private RelatorioService relatorioService;
	@Autowired private ParametroService parametroService;
	@Autowired private PartoService partoService;
	@Autowired private CoberturaService coberturaService;
	@Autowired private ProcedimentoService procedimentoService;
	@Autowired private AnimalService animalService;
	
	//controllers
	@Autowired private AnimalReducedOverviewController animalReducedOverviewController;
	@Autowired private AnimalFormController animalFormController;
	@Autowired private TouroReducedOverviewController touroReducedOverviewController;
	@Autowired private RacaReducedOverviewController racaReducedOverviewController;
	@Autowired private ConfirmacaoPrenhesFormController confirmacaoPrenhesFormController;
	@Autowired private CoberturaOverviewController coberturaOverviewController;
	@Autowired private CoberturaFormController coberturaFormController;
	@Autowired private RacaOverviewController racaController;
	@Autowired private AnimalReducedOverviewController animalReducedController;
	@Autowired private MorteAnimalFormController morteAnimalFormController;
	@Autowired private VendaAnimalFormController vendaAnimalFormController;
	@Autowired private LactacaoOverviewController lactacaoOverviewController;
	@Autowired private FichaAnimalOverviewController fichaAnimalOverviewController;
	@Autowired private ProducaoIndividualOverviewController producaoIndividualOverviewController;
	@Autowired private IndicadorOverviewController indicadorOverviewController;
	//@Autowired private PartoOverviewController partoOverviewController;
	@Autowired private PartoFormController partoFormController;
	@Autowired private AbortoFormController abortoFormController;
	
	private Animal animal;
	
	@FXML
	public void initialize() {
		
		configuraMenu();
		
	}
	
	@SuppressWarnings("unchecked")
	private void configuraMenu(){
		
		//CustomTreeItem menuCadastro = new CustomTreeItem("Cadastro", false, cadastro);
		
		CustomTreeItem menuReproducao = new CustomTreeItem("Reprodução", false, null);
		menuReproducao.getChildren().add(new CustomTreeItem("Coberturas", false, coberturas));
		menuReproducao.getChildren().add(new CustomTreeItem("Partos", false, partos));
		
		CustomTreeItem menuProducao = new CustomTreeItem("Produção", false, null);
		menuProducao.getChildren().add(new CustomTreeItem("Lactações", false, lactacoes));
		menuProducao.getChildren().add(new CustomTreeItem("Controle Leiteiro", false, controleLeiteiro));
		
/*		CustomTreeItem menuSaida = new CustomTreeItem("Saída Animal", false, null);
		menuSaida.getChildren().add(new CustomTreeItem("Registrar Venda", false, registrarVenda));
		menuSaida.getChildren().add(new CustomTreeItem("Registrar Morte", false, registrarMorte));*/
		
		CustomTreeItem menuRoot = new CustomTreeItem("Menu", false, null);
		menuRoot.getChildren().addAll(menuReproducao, menuProducao);
		
		menu.setRoot(menuRoot);
		menu.setShowRoot(false);
		
		// captura o evento de double click da tree
		menu.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if ( menu.getSelectionModel().getSelectedItem() != null ){
				Function<Integer, Boolean> f = ((CustomTreeItem)menu.getSelectionModel().getSelectedItem()).getFunction();
				if ( f != null ){
					f.apply(0);	
				}
			}
		});
		
		menu.getSelectionModel().clearAndSelect(2);
		
		if ( animal.getSituacaoAnimal().equals(SituacaoAnimal.MORTO) ){
			
		}
		
	}
	
	Function<Integer, Boolean> cadastro = index -> {
		
		animalFormController.setObject(animal);
		animalFormController.showForm();
		return true;
		
	};
	
	Function<Integer, Boolean> coberturas = index -> {
		
		coberturaOverviewController.setFemea(animal);
		changeForm((AnchorPane) MainApp.load(coberturaOverviewController.getFormName()));
		return true;
		
	};
	
	Function<Integer, Boolean> partos = index -> {
		
		lactacaoOverviewController.setAnimal(animal);
		changeForm((AnchorPane) MainApp.load(lactacaoOverviewController.getFormName()));
		return true;
		
	};
	
	Function<Integer, Boolean> lactacoes = index -> {
		
		lactacaoOverviewController.setAnimal(animal);
		changeForm((AnchorPane) MainApp.load(lactacaoOverviewController.getFormName()));
		return true;
		
	};
	
	Function<Integer, Boolean> controleLeiteiro = index -> {
		
		producaoIndividualOverviewController.setAnimal(animal);
		changeForm((AnchorPane) MainApp.load(producaoIndividualOverviewController.getFormName()));
		return true;
		
	};
	
	Function<Integer, Boolean> registrarMorte = index -> {
		
		if ( animal.getSituacaoAnimal().equals(SituacaoAnimal.VENDIDO) ){
			CustomAlert.mensagemInfo("O animal já foi vendido, não é possível registrar a morte.");
		}else{
			MorteAnimal morteAnimal = morteAnimalService.findByAnimal(animal);
			if ( morteAnimal == null ){
				morteAnimal = new MorteAnimal(animal);
			}
			morteAnimalFormController.setObject(morteAnimal);
			morteAnimalFormController.showForm();
		}
		return true;
		
	};
	
	Function<Integer, Boolean> registrarVenda = index -> {
		
		if ( animal.getSituacaoAnimal().equals(SituacaoAnimal.MORTO) ){
			CustomAlert.mensagemInfo("O animal está morto, não é possível registrar a venda.");
		}else{
			VendaAnimal vendaAnimal = vendaAnimalService.findByAnimal(animal);
			if ( vendaAnimal == null ){
				vendaAnimal = new VendaAnimal(animal);
			}
			vendaAnimalFormController.setObject(vendaAnimal);
			vendaAnimalFormController.showForm();
		}
		return true;
		
	};
	
	private void changeForm(AnchorPane form){
		vbMain.getChildren().clear();
		vbMain.getChildren().add(form);
		VBox.setVgrow(form, Priority.SOMETIMES);
        HBox.setHgrow(form, Priority.SOMETIMES);
	}
	
	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	@Override
	public String getFormName() {
		return "view/animal/PainelControleAnimal.fxml";
	}

	@Override
	public String getFormTitle() {
		return "Painel Controle Animal";
	}
	
	@Override
	@Resource(name = "animalService")
	protected void setService(IService<Integer, Animal> service) {
		super.setService(service);
	}

	
}
