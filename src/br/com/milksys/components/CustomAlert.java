package br.com.milksys.components;


import java.util.Hashtable;
import java.util.Map;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class CustomAlert extends Alert {

	public CustomAlert(AlertType arg0) {
		super(arg0);
	}

	public static void nenhumRegistroSelecionado(){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Nenhuma Sele��o");
		alert.setHeaderText("Nenhum registro selecionado");
		alert.setContentText("Selecione pelo menos um registro na tabela!");
		alert.showAndWait();
	}

	public static Optional<ButtonType> confirmarExclusao() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirma��o");
		alert.setHeaderText("Confirme a exclus�o do registro");
		alert.setContentText("Tem certeza que deseja remover o registro selecionado?");
		return alert.showAndWait();
	}
	
	public static Optional<ButtonType> confirmarExclusao(String header, String text) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirma��o");
		alert.setHeaderText(header);
		alert.setContentText(text);
		return alert.showAndWait();
	}

	public static void campoObrigatorio(String field) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Campo Requerido");
		alert.setHeaderText("Valida��o de Formul�rio");
		alert.setContentText("O campo obrigat�rio [" + field + "] n�o foi preenchido. Por favor, informe-o para prosseguir.");
		alert.showAndWait();
	}

	public static void mensagemAlerta(String tipo, String mensagem) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Aten��o");
		alert.setHeaderText(tipo);
		alert.setContentText(mensagem);
		alert.showAndWait();
	}

	public static void mensagemInfo(String string) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Aten��o");
		alert.setHeaderText("Informa��o");
		alert.setContentText(string);
		alert.showAndWait();
		
	}

	public static void showMessage(Hashtable<String, String> erros) {
		
		//o hastable s� ter� um item a cada valida��o para n�o encher de mensagem de 
		//uma �nica vez
		for ( Map.Entry<String, String> entry : erros.entrySet() ) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Aten��o");
			alert.setHeaderText(entry.getKey());
			alert.setContentText(entry.getValue());
			alert.showAndWait();
			return;
		}
		
	}
	
	
}
