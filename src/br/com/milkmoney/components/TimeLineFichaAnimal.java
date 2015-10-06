package br.com.milkmoney.components;

import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import br.com.milkmoney.model.FichaAnimal;
import br.com.milkmoney.util.DateUtil;

public class TimeLineFichaAnimal extends VBox {
	
	List<FichaAnimal> eventos;

	public TimeLineFichaAnimal(List<FichaAnimal> eventos) {
		this.eventos = eventos;
		this.buildBox();
	}
	
	private void buildBox(){
		Pos align = Pos.CENTER_LEFT;
		for ( FichaAnimal ficha : eventos ){

			Label labelEvento;
			HBox  hbox = new HBox();
			hbox.setAlignment(align);
			if ( align.equals(Pos.CENTER_LEFT) ){
				labelEvento = new Label(ficha.getEvento() + "-" + DateUtil.format(ficha.getData()));
				hbox.getChildren().addAll(labelEvento, new ImageView(new Image(ClassLoader.getSystemResourceAsStream("img/reproducao16.png"))));
			}else{
				labelEvento = new Label(DateUtil.format(ficha.getData()) + "-" + ficha.getEvento());
				hbox.getChildren().addAll(new ImageView(new Image(ClassLoader.getSystemResourceAsStream("img/reproducao16.png"))), labelEvento);
			}
			
			align = align.equals(Pos.CENTER_LEFT) ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT;
			
			this.getChildren().add(hbox);
			
		}
	}
}
