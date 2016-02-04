package br.com.milkmoney.components;


import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class CustomAlert {

	public static ButtonType response = ButtonType.CANCEL;
	
	public static void nenhumRegistroSelecionado(){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Nenhum registro selecionado");
		alert.setHeaderText("Ooops!! Precisamos rever alguma coisa!");
		alert.setContentText("Por favor, selecione um registro na tabela para continuar.");
		alert.show();
	}

	public static void confirmarExclusao() {
		Alert alert = new Alert(AlertType.CONFIRMATION,
					"Tem certeza que deseja remover o registro selecionado?", 
					ButtonType.OK, ButtonType.CANCEL);
		alert.showAndWait();
		response = alert.getResult();
	}
	
	public static void confirmarExclusao(String header, String text) {
		Alert alert = new Alert(AlertType.CONFIRMATION,
				"Tem certeza que deseja remover o registro selecionado?", 
				ButtonType.OK, ButtonType.CANCEL);
		alert.showAndWait();
		response = alert.getResult();
	}

	public static void campoObrigatorio(String field) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Campo obrigatório");
		alert.setHeaderText("Ooops!! Precisamos rever alguma coisa!");
		alert.setContentText("O campo obrigatório [" + field + "] não foi preenchido. Por favor, informe-o para prosseguir.");
		alert.show();
	}

	public static void mensagemAlerta(String tipo, String mensagem) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(tipo);
		alert.setHeaderText("Ooops!! Alguma coisa não parece bem.");
		alert.setContentText(mensagem);
		alert.show();
	}

	public static void mensagemInfo(String string) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Importante");
		alert.setHeaderText("Ooops!! Precisamos rever alguma coisa!");
		alert.setContentText(string);
		alert.show();
	}

	public static Optional<ButtonType> confirmar(String header, String message) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmação");
		alert.setHeaderText(header);
		alert.setContentText(message);
		return alert.showAndWait();
	}

	public static void mensagemErro(String string) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Erro");
		alert.setHeaderText("Ooops!! Um erro aconteceu :(");
		alert.setContentText(string);
		alert.show();
	}

}
