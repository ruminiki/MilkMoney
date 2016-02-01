package br.com.milkmoney.controller.lactacao.renderer;

import java.util.function.Function;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import br.com.milkmoney.model.Lactacao;

@SuppressWarnings("hiding")
public class TableCellOpcoesFactory<S, String> implements Callback<TableColumn<S, String>, TableCell<S, String>>{
	
	private Function<Integer, Boolean> encerrarLactacaoFunction;
	private Function<Integer, Boolean> desfazerEncerramentoLactacaoFunction;
	
	public TableCellOpcoesFactory(Function<Integer, Boolean> encerrarLactacaoFunction, Function<Integer, Boolean> desfazerEncerramentoLactacaoFunction) {
		this.encerrarLactacaoFunction = encerrarLactacaoFunction;
		this.desfazerEncerramentoLactacaoFunction = desfazerEncerramentoLactacaoFunction;
	}

	@Override
	public TableCell<S, String> call(TableColumn<S, String> param) {
		
		  TableCell<S, String> cell = new TableCell<S, String>() {
			  
		        @Override
		        protected void updateItem(String item, boolean empty) {
		        	if ( tableRowProperty().getValue().getItem() != null ){
		        		if(item!=null){
		        			
		        			Lactacao lactacao = (Lactacao) tableViewProperty().get().getItems().get(tableRowProperty().get().getIndex());
		        			
							HBox cell = new HBox();
							cell.setAlignment(Pos.CENTER);
							cell.setSpacing(3);
							cell.setMaxHeight(10);
							
							if ( lactacao.getDataFim() == null ){
								Hyperlink btnSecar = new Hyperlink("Encerrar");
								btnSecar.setMaxHeight(12);
								btnSecar.setCursor(Cursor.HAND);
								btnSecar.setFocusTraversable(false);
								btnSecar.setOnAction(new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent event) {
										tableViewProperty().get().getSelectionModel().select(tableRowProperty().get().getIndex());
										encerrarLactacaoFunction.apply(tableRowProperty().get().getIndex());
									}
								});
								VBox c = new VBox();
								c.setStyle("-fx-padding: 0; " +
										"-fx-min-height: 24; -fx-min-width: 24;" +
										"-fx-background-insets: 0;" +
									    "-fx-background-image: url('img/reabrir20.png'); " +	
									    "-fx-background-repeat: no-repeat; " + 
									    "-fx-background-position: center; " +
									    "-fx-cursor: HAND; " +
									    "-fx-border-width:0; ");
								cell.getChildren().add(c);
								cell.getChildren().add(btnSecar);
							}else{
								Hyperlink btnDesfazer = new Hyperlink("Reabrir");
								btnDesfazer.setMaxHeight(12);
								btnDesfazer.setCursor(Cursor.HAND);
								btnDesfazer.setFocusTraversable(false);
								btnDesfazer.setOnAction(new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent event) {
										tableViewProperty().get().getSelectionModel().select(tableRowProperty().get().getIndex());
										desfazerEncerramentoLactacaoFunction.apply(tableRowProperty().get().getIndex());
									}
								});
								VBox c = new VBox();
								c.setStyle("-fx-padding: 0; " +
										"-fx-min-height: 24; -fx-min-width: 24;" +
										"-fx-background-insets: 0;" +
									    "-fx-background-image: url('img/fechar20.png'); " +	
									    "-fx-background-repeat: no-repeat; " + 
									    "-fx-background-position: center; " +
									    "-fx-cursor: HAND; " +
									    "-fx-border-width:0; ");
								cell.getChildren().add(c);
								cell.getChildren().add(btnDesfazer);
							}
							
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
