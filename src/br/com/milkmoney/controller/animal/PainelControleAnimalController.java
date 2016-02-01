package br.com.milkmoney.controller.animal;

import java.util.function.Function;

import javafx.fxml.FXML;
import javafx.scene.control.TreeView;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

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
import br.com.milkmoney.model.Sexo;
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
	@Autowired private PartoFormController partoFormController;
	@Autowired private AbortoFormController abortoFormController;
	
	private Animal animal;
	
	@FXML
	public void initialize() {
		
		configuraMenu();
		
		
	}
	
	@SuppressWarnings("unchecked")
	private void configuraMenu(){
		
		CustomTreeItem menuCadastro = new CustomTreeItem("Cadastro", false, lactacoes);
		
		CustomTreeItem menuReproducao = new CustomTreeItem("Reprodução", false, lactacoes);
		menuReproducao.getChildren().add(new CustomTreeItem("Coberturas", false, lactacoes));
		menuReproducao.getChildren().add(new CustomTreeItem("Partos", false, lactacoes));
		menuReproducao.getChildren().add(new CustomTreeItem("Abortos", false, lactacoes));
		menuReproducao.getChildren().add(new CustomTreeItem("Repetições de Cio", false, lactacoes));
		
		CustomTreeItem menuProducao = new CustomTreeItem("Produção", false, lactacoes);
		menuProducao.getChildren().add(new CustomTreeItem("Lactações", false, lactacoes));
		menuProducao.getChildren().add(new CustomTreeItem("Controle Leiteiro", false, lactacoes));
		
		CustomTreeItem menuSaida = new CustomTreeItem("Saída Animal", false, lactacoes);
		menuSaida.getChildren().add(new CustomTreeItem("Registrar Venda", false, lactacoes));
		menuSaida.getChildren().add(new CustomTreeItem("Registrar Morte", false, lactacoes));
		
		
		CustomTreeItem menuRoot = new CustomTreeItem("Menu", false, lactacoes);
		menuRoot.getChildren().addAll(menuCadastro, menuReproducao, menuProducao, menuSaida);
		
		menu.setRoot(menuRoot);
		menu.setShowRoot(false);
		
		//menu.selectionModelProperty().addListener(arg0);
		
	}
	
	
	@FXML@SuppressWarnings("rawtypes")
	Function lactacoes = index -> {
		lactacaoOverviewController.setAnimal(animal);
		lactacaoOverviewController.showForm();
		return true;
	};
	
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
