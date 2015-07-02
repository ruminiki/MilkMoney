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
		alert.setTitle("Nenhuma Seleção");
		alert.setHeaderText("Nenhum registro selecionado");
		alert.setContentText("Selecione pelo menos um registro na tabela!");
		alert.showAndWait();
	}

	public static Optional<ButtonType> confirmarExclusao() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmação");
		alert.setHeaderText("Confirme a exclusão do registro");
		alert.setContentText("Tem certeza que deseja remover o registro selecionado?");
		return alert.showAndWait();
	}
	
	public static Optional<ButtonType> confirmarExclusao(String header, String text) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmação");
		alert.setHeaderText(header);
		alert.setContentText(text);
		return alert.showAndWait();
	}

	public static void campoObrigatorio(String field) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Campo Requerido");
		alert.setHeaderText("Validação de Formulário");
		alert.setContentText("O campo obrigatório [" + field + "] não foi preenchido. Por favor, informe-o para prosseguir.");
		alert.showAndWait();
	}

	public static void mensagemAlerta(String tipo, String mensagem) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Atenção");
		alert.setHeaderText(tipo);
		alert.setContentText(mensagem);
		alert.showAndWait();
	}

	public static void mensagemInfo(String string) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Atenção");
		alert.setHeaderText("Informação");
		alert.setContentText(string);
		alert.showAndWait();
		
	}

	public static void showMessage(Hashtable<String, String> erros) {
		
		//o hastable só terá um item a cada validação para não encher de mensagem de 
		//uma única vez
		for ( Map.Entry<String, String> entry : erros.entrySet() ) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Atenção");
			alert.setHeaderText(entry.getKey());
			alert.setContentText(entry.getValue());
			alert.showAndWait();
			return;
		}
		
	}
	
	
}
