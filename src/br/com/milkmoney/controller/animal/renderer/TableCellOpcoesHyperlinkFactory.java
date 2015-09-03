package br.com.milkmoney.controller.animal.renderer;

import java.util.function.Function;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

@SuppressWarnings("hiding")
public class TableCellOpcoesHyperlinkFactory<S, String> implements Callback<TableColumn<S, String>, TableCell<S, String>>{
	
	private Function<Integer, Boolean> encerrarLactacaoFunction;
	private Function<Integer, Boolean> registrarVendaFunction;
	private Function<Integer, Boolean> registrarMorteFunction;
	
	public TableCellOpcoesHyperlinkFactory(Function<Integer, Boolean> encerrarLactacaoFunction, 
			Function<Integer, Boolean> registrarMorteFunction, Function<Integer, Boolean> registrarVendaFunction) {
		this.encerrarLactacaoFunction = encerrarLactacaoFunction;
		this.registrarMorteFunction = registrarMorteFunction;
		this.registrarVendaFunction = registrarVendaFunction;
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
							cell.setSpacing(-4);
							
							Hyperlink hpS = new Hyperlink();
							hpS.setText("S");
							hpS.setTooltip(new Tooltip("Secar Animal"));
							hpS.setFocusTraversable(false);
							hpS.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									encerrarLactacaoFunction.apply(tableRowProperty().get().getIndex());
									hpS.setUnderline(false);
									hpS.setVisited(false);
								}
							});
								
							cell.getChildren().add(hpS);
								
							Hyperlink hpM = new Hyperlink();
							hpM.setText("M");
							hpM.setTooltip(new Tooltip("Registrar Morte Animal"));
							hpM.setFocusTraversable(false);
							hpM.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									registrarMorteFunction.apply(tableRowProperty().get().getIndex());
									hpM.setUnderline(false);
									hpM.setVisited(false);
								}
							});
							
							cell.getChildren().add(hpM);
							
							Hyperlink hpV = new Hyperlink();
							hpV.setText("V");
							hpV.setTooltip(new Tooltip("Registrar Venda Animal"));
							hpV.setFocusTraversable(false);
							hpV.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									registrarVendaFunction.apply(tableRowProperty().get().getIndex());
									hpV.setUnderline(false);
									hpV.setVisited(false);
								}
							});
							
							cell.getChildren().add(hpV);
							setGraphic(cell);
						} 
					}
		            setStyle("-fx-alignment: CENTER");
		        }
		    };
		    return cell;
	}
	
}
