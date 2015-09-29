package br.com.milkmoney.controller.animal.renderer;

import java.util.function.Function;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import br.com.milkmoney.model.Animal;
import br.com.milkmoney.model.Sexo;
import br.com.milkmoney.model.SituacaoAnimal;

@SuppressWarnings("hiding")
public class TableCellOpcoesFactory<S, String> implements Callback<TableColumn<S, String>, TableCell<S, String>>{
	
	private Function<Integer, Boolean> encerrarLactacaoFunction;
	private Function<Integer, Boolean> registrarVendaFunction;
	private Function<Integer, Boolean> registrarMorteFunction;
	private Function<Integer, Boolean> registrarProducaoFunction;
	private Function<Integer, Boolean> registrarCoberturaFunction;
	private Function<Integer, Boolean> fichaAnimalFunction;
	
	public TableCellOpcoesFactory(Function<Integer, Boolean> registrarCoberturaFunction, Function<Integer, Boolean> encerrarLactacaoFunction,
			Function<Integer, Boolean> registrarVendaFunction, Function<Integer, Boolean> registrarMorteFunction,
			Function<Integer, Boolean> registrarProducaoFunction, Function<Integer, Boolean> fichaAnimalFunction) {
		this.encerrarLactacaoFunction = encerrarLactacaoFunction;
		this.registrarMorteFunction = registrarMorteFunction;
		this.registrarVendaFunction = registrarVendaFunction;
		this.registrarProducaoFunction = registrarProducaoFunction;
		this.registrarCoberturaFunction = registrarCoberturaFunction;
		this.fichaAnimalFunction = fichaAnimalFunction;
	}

	@Override
	public TableCell<S, String> call(TableColumn<S, String> param) {
		
		  TableCell<S, String> cell = new TableCell<S, String>() {
			  
		        @Override
		        protected void updateItem(String item, boolean empty) {
		        	if ( tableRowProperty().getValue().getItem() != null ){
		        		if(item!=null){
		        			
		        			Animal animal = (Animal) tableViewProperty().get().getItems().get(tableRowProperty().get().getIndex());
		        			
							HBox cell = new HBox();
							cell.setAlignment(Pos.CENTER);
							cell.setSpacing(1);
							cell.setMaxHeight(10);
							
							Button btnSecar = new Button("", new ImageView(new Image(ClassLoader.getSystemResourceAsStream("img/secar16.png"))));
							if ( animal.getSituacaoAnimal().equals(SituacaoAnimal.SECO) ){
								btnSecar.setTooltip(new Tooltip("Desfazer Encerramento Lactação"));
							}else{
								btnSecar.setTooltip(new Tooltip("Secar Animal"));
							}
							btnSecar.setMaxHeight(12);
							btnSecar.setDisable(animal.getSexo().equals(Sexo.MACHO) || animal.getSituacaoAnimal().matches(SituacaoAnimal.MORTO + "|" + SituacaoAnimal.VENDIDO + "|" + SituacaoAnimal.SECO));
							btnSecar.setCursor(Cursor.HAND);
							btnSecar.setFocusTraversable(false);
							btnSecar.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									tableViewProperty().get().getSelectionModel().select(tableRowProperty().get().getIndex());
									encerrarLactacaoFunction.apply(tableRowProperty().get().getIndex());
								}
							});
								
							cell.getChildren().add(btnSecar);
							
							Button btnCobertura = new Button("", new ImageView(new Image(ClassLoader.getSystemResourceAsStream("img/reproducao16.png"))));
							btnCobertura.setMaxHeight(12);
							btnCobertura.setTooltip(new Tooltip("Registrar Cobertura"));
							btnCobertura.setDisable(animal.getSexo().equals(Sexo.MACHO) || animal.getSituacaoAnimal().matches(SituacaoAnimal.MORTO + "|" + SituacaoAnimal.VENDIDO));
							btnCobertura.setCursor(Cursor.HAND);
							btnCobertura.setFocusTraversable(false);
							btnCobertura.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									tableViewProperty().get().getSelectionModel().select(tableRowProperty().get().getIndex());
									registrarCoberturaFunction.apply(tableRowProperty().get().getIndex());
								}
							});
								
							cell.getChildren().add(btnCobertura);
							
							Button btnRegistrarMorte = new Button("", new ImageView(new Image(ClassLoader.getSystemResourceAsStream("img/morte16.png"))));
							btnRegistrarMorte.setMaxHeight(12);
							btnRegistrarMorte.setDisable(animal.getSexo().equals(Sexo.MACHO) || animal.getSituacaoAnimal().equals(SituacaoAnimal.VENDIDO));
							btnRegistrarMorte.setCursor(Cursor.HAND);
							if ( animal.getSituacaoAnimal().equals(SituacaoAnimal.MORTO) ){
								btnRegistrarMorte.setTooltip(new Tooltip("Desfazer Registro Morte"));
							}else{
								btnRegistrarMorte.setTooltip(new Tooltip("Registrar Morte"));
							}
							btnRegistrarMorte.setFocusTraversable(false);
							btnRegistrarMorte.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									tableViewProperty().get().getSelectionModel().select(tableRowProperty().get().getIndex());
									registrarMorteFunction.apply(tableRowProperty().get().getIndex());
								}
							});
								
							cell.getChildren().add(btnRegistrarMorte);
								
							Button btnRegistrarVenda = new Button("", new ImageView(new Image(ClassLoader.getSystemResourceAsStream("img/venda16.png"))));
							btnRegistrarVenda.setMaxHeight(12);
							btnRegistrarVenda.setDisable(animal.getSexo().equals(Sexo.MACHO) || animal.getSituacaoAnimal().equals(SituacaoAnimal.MORTO));
							if ( animal.getSituacaoAnimal().equals(SituacaoAnimal.VENDIDO) ){
								btnRegistrarVenda.setTooltip(new Tooltip("Desfazer Registro Venda"));
							}else{
								btnRegistrarVenda.setTooltip(new Tooltip("Registrar Venda"));
							}
							btnRegistrarVenda.setCursor(Cursor.HAND);
							btnRegistrarVenda.setFocusTraversable(false);
							btnRegistrarVenda.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									tableViewProperty().get().getSelectionModel().select(tableRowProperty().get().getIndex());
									registrarVendaFunction.apply(tableRowProperty().get().getIndex());
								}
							});
								
							cell.getChildren().add(btnRegistrarVenda);
							
							Button btnProducao = new Button("", new ImageView(new Image(ClassLoader.getSystemResourceAsStream("img/producao16.png"))));
							btnProducao.setMaxHeight(12);
							btnProducao.setDisable(animal.getSexo().equals(Sexo.MACHO) || animal.getSituacaoAnimal().matches(SituacaoAnimal.MORTO + "|" + SituacaoAnimal.VENDIDO));
							btnProducao.setTooltip(new Tooltip("Registrar Produção"));
							btnProducao.setCursor(Cursor.HAND);
							btnProducao.setFocusTraversable(false);
							btnProducao.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									tableViewProperty().get().getSelectionModel().select(tableRowProperty().get().getIndex());
									registrarProducaoFunction.apply(tableRowProperty().get().getIndex());
								}
							});
								
							cell.getChildren().add(btnProducao);
							
							Button btnFicha = new Button("", new ImageView(new Image(ClassLoader.getSystemResourceAsStream("img/ficha16.png"))));
							btnFicha.setMaxHeight(12);
							btnFicha.setTooltip(new Tooltip("Ficha Animal"));
							btnFicha.setCursor(Cursor.HAND);
							btnFicha.setFocusTraversable(false);
							btnFicha.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									tableViewProperty().get().getSelectionModel().select(tableRowProperty().get().getIndex());
									fichaAnimalFunction.apply(tableRowProperty().get().getIndex());
								}
							});
								
							cell.getChildren().add(btnFicha);
							
							setGraphic(cell);
						}else{
							setGraphic(null);
						}
					}else{
						setGraphic(null);
					}
		            setStyle("-fx-alignment: CENTER");
		        }
		    };
		    return cell;
	}
	
}
