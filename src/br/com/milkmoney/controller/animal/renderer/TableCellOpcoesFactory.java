package br.com.milkmoney.controller.animal.renderer;

import java.util.function.Function;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
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
	private Function<Integer, Boolean> linhaTempoAnimalFunction;
	
	public TableCellOpcoesFactory(Function<Integer, Boolean> registrarCoberturaFunction, Function<Integer, Boolean> encerrarLactacaoFunction,
			Function<Integer, Boolean> registrarVendaFunction, Function<Integer, Boolean> registrarMorteFunction,
			Function<Integer, Boolean> registrarProducaoFunction, Function<Integer, Boolean> fichaAnimalFunction, Function<Integer, Boolean> linhaTempoAnimalFunction) {
		this.encerrarLactacaoFunction = encerrarLactacaoFunction;
		this.registrarMorteFunction = registrarMorteFunction;
		this.registrarVendaFunction = registrarVendaFunction;
		this.registrarProducaoFunction = registrarProducaoFunction;
		this.registrarCoberturaFunction = registrarCoberturaFunction;
		this.fichaAnimalFunction = fichaAnimalFunction;
		this.linhaTempoAnimalFunction = linhaTempoAnimalFunction;
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
							cell.setSpacing(8);
							
							VBoxOption btnCobertura = new VBoxOption("img/reproducao16.png", "Coberturas");
							btnCobertura._setDisabled(animal.getSexo().equals(Sexo.MACHO) || animal.getSituacaoAnimal().matches(SituacaoAnimal.MORTO + "|" + SituacaoAnimal.VENDIDO));
							btnCobertura.setOnMouseReleased(new EventHandler<Event>() {
					        	@Override
					        	public void handle(Event event) {
					        		if ( btnCobertura._isDisabled() ) return;
					        		tableViewProperty().get().getSelectionModel().select(tableRowProperty().get().getIndex());
									registrarCoberturaFunction.apply(tableRowProperty().get().getIndex());
					        	}
					        });
							cell.getChildren().add(btnCobertura);
							
							VBoxOption btnSecar = new VBoxOption("img/secar16.png", "Lactações");
							btnSecar._setDisabled(animal.getSexo().equals(Sexo.MACHO) || animal.getSituacaoAnimal().matches(SituacaoAnimal.MORTO + "|" + SituacaoAnimal.VENDIDO));
							btnSecar.setOnMouseReleased(new EventHandler<Event>() {
					        	@Override
					        	public void handle(Event event) {
					        		if ( btnSecar._isDisabled() ) return;
					        		tableViewProperty().get().getSelectionModel().select(tableRowProperty().get().getIndex());
					        		encerrarLactacaoFunction.apply(tableRowProperty().get().getIndex());
					        	}
					        });
							cell.getChildren().add(btnSecar);
							
							
							VBoxOption btnRegistrarVenda = new VBoxOption("img/venda16.png", 
									animal.getSituacaoAnimal().equals(SituacaoAnimal.VENDIDO) ? "Desfazer Venda" : "Registrar Venda");
							btnRegistrarVenda._setDisabled(animal.getSexo().equals(Sexo.MACHO) || animal.getSituacaoAnimal().equals(SituacaoAnimal.MORTO));
							btnRegistrarVenda.setOnMouseReleased(new EventHandler<Event>() {
					        	@Override
					        	public void handle(Event event) {
					        		if ( btnRegistrarVenda._isDisabled() ) return;
					        		tableViewProperty().get().getSelectionModel().select(tableRowProperty().get().getIndex());
					        		registrarVendaFunction.apply(tableRowProperty().get().getIndex());
					        	}
					        });
							cell.getChildren().add(btnRegistrarVenda);
							
							VBoxOption btnRegistrarMorte = new VBoxOption("img/morte16.png",
									animal.getSituacaoAnimal().equals(SituacaoAnimal.MORTO) ? "Desfazer Registro Morte" : "Registrar Morte");
							btnRegistrarMorte._setDisabled(animal.getSexo().equals(Sexo.MACHO) || animal.getSituacaoAnimal().equals(SituacaoAnimal.VENDIDO));
							btnRegistrarMorte.setOnMouseReleased(new EventHandler<Event>() {
					        	@Override
					        	public void handle(Event event) {
					        		if ( btnRegistrarMorte._isDisabled() ) return;
					        		tableViewProperty().get().getSelectionModel().select(tableRowProperty().get().getIndex());
					        		registrarMorteFunction.apply(tableRowProperty().get().getIndex());
					        	}
					        });
							cell.getChildren().add(btnRegistrarMorte);
								
							VBoxOption btnProducao = new VBoxOption("img/producao16.png", "Registro Produção");
							btnProducao._setDisabled(animal.getSexo().equals(Sexo.MACHO) || animal.getSituacaoAnimal().matches(SituacaoAnimal.MORTO + "|" + SituacaoAnimal.VENDIDO));
							btnProducao.setOnMouseReleased(new EventHandler<Event>() {
					        	@Override
					        	public void handle(Event event) {
					        		if ( btnProducao._isDisabled() ) return;
					        		tableViewProperty().get().getSelectionModel().select(tableRowProperty().get().getIndex());
					        		registrarProducaoFunction.apply(tableRowProperty().get().getIndex());
					        	}
					        });
							cell.getChildren().add(btnProducao);
							
							VBoxOption btnFicha = new VBoxOption("img/ficha16.png", "Ficha do Animal");
							btnFicha.setOnMouseReleased(new EventHandler<Event>() {
					        	@Override
					        	public void handle(Event event) {
					        		if ( btnFicha._isDisabled() ) return;
					        		tableViewProperty().get().getSelectionModel().select(tableRowProperty().get().getIndex());
					        		fichaAnimalFunction.apply(tableRowProperty().get().getIndex());
					        	}
					        });
							cell.getChildren().add(btnFicha);
							
							VBoxOption btnEventos = new VBoxOption("img/timeline16.png", "Linha do Tempo do Animal");
							btnEventos.setOnMouseReleased(new EventHandler<Event>() {
					        	@Override
					        	public void handle(Event event) {
					        		if ( btnEventos._isDisabled() ) return;
					        		tableViewProperty().get().getSelectionModel().select(tableRowProperty().get().getIndex());
					        		linhaTempoAnimalFunction.apply(tableRowProperty().get().getIndex());
					        	}
					        });
							cell.getChildren().add(btnEventos);
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
