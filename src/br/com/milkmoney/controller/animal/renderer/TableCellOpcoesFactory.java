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

@SuppressWarnings("hiding")
public class TableCellOpcoesFactory<S, String> implements Callback<TableColumn<S, String>, TableCell<S, String>>{
	
	private Function<Integer, Boolean> encerrarLactacaoFunction;
	private Function<Integer, Boolean> registrarVendaFunction;
	private Function<Integer, Boolean> registrarMorteFunction;
	private Function<Integer, Boolean> registrarProducaoFunction;
	private Function<Integer, Boolean> registrarCoberturaFunction;
	
	public TableCellOpcoesFactory(Function<Integer, Boolean> registrarCoberturaFunction, Function<Integer, Boolean> encerrarLactacaoFunction,
			Function<Integer, Boolean> registrarVendaFunction, Function<Integer, Boolean> registrarMorteFunction,
			Function<Integer, Boolean> registrarProducaoFunction) {
		this.encerrarLactacaoFunction = encerrarLactacaoFunction;
		this.registrarMorteFunction = registrarMorteFunction;
		this.registrarVendaFunction = registrarVendaFunction;
		this.registrarProducaoFunction = registrarProducaoFunction;
		this.registrarCoberturaFunction = registrarCoberturaFunction;
	}

	@Override
	public TableCell<S, String> call(TableColumn<S, String> param) {
		
		  TableCell<S, String> cell = new TableCell<S, String>() {
			  
		        @Override
		        protected void updateItem(String item, boolean empty) {
		        	if ( tableRowProperty().getValue().getItem() != null ){
		        		if(item!=null){
							HBox cell = new HBox();
							cell.setAlignment(Pos.CENTER);
							cell.setSpacing(1);
							
							Button btnSecar = new Button("", new ImageView(new Image(ClassLoader.getSystemResourceAsStream("img/secar16.png"))));
							btnSecar.setTooltip(new Tooltip("Secar Animal"));
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
							btnCobertura.setTooltip(new Tooltip("Registrar Cobertura"));
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
							btnRegistrarMorte.setCursor(Cursor.HAND);
							btnRegistrarMorte.setTooltip(new Tooltip("Registrar Morte"));
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
							btnRegistrarVenda.setTooltip(new Tooltip("Registrar Venda"));
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
							
							setGraphic(cell);
						}else{
							setGraphic(null);
						}
					}
		            setStyle("-fx-alignment: CENTER");
		        }
		    };
		    return cell;
	}
	
}
