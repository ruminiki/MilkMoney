package br.com.milksys.components;

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

	public static void campoObrigatorio(String field) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Campo Requerido");
		alert.setHeaderText("Campo obrigatório não informado");
		alert.setContentText("O campo obrigatório [" + field + "] não foi preenchido. É necessário informá-lo para prosseguir.");
		alert.showAndWait();
	}

	public static void mensagemAlerta(String string) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Atenção");
		alert.setHeaderText("Validação de Campo");
		alert.setContentText(string);
		alert.showAndWait();
	}
	
	
}
