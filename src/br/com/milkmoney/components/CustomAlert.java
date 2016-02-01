package br.com.milkmoney.components;


import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import org.controlsfx.control.Notifications;

public class CustomAlert {

	public static ButtonType response = ButtonType.CANCEL;
	
	public static void nenhumRegistroSelecionado(){
		 Notifications.create()
         .title("Nenhuma Seleção")
         .text("Por favor, selecione um registro na tabela para continuar.")
         .showInformation();
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
		 Notifications.create()
         .title("Ooops!! Precisamos rever alguma coisa!")
         .text("O campo obrigatório [" + field + "] não foi preenchido. Por favor, informe-o para prosseguir.")
         .showInformation();
	}

	public static void mensagemAlerta(String tipo, String mensagem) {
		 Notifications.create()
         .title(tipo)
         .text(mensagem)
         .showWarning();
	}

	public static void mensagemInfo(String string) {
		Notifications.create()
        .title("Atenção")
        .text(string)
        .showInformation();
	}

	public static Optional<ButtonType> confirmar(String header, String message) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmação");
		alert.setHeaderText(header);
		alert.setContentText(message);
		return alert.showAndWait();
	}

}
