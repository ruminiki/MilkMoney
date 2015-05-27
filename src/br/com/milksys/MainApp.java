package br.com.milksys;

import java.io.IOException;
import java.io.InputStream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MainApp extends Application {

	public static Stage primaryStage;
	public static BorderPane rootLayout;
	private static ApplicationContext context;
	
	public MainApp() {
		context = new ClassPathXmlApplicationContext(new String[] {"services.xml", "controllers.xml", "daos.xml", "beans.xml"});
	}

	@Override
	public void start(Stage primaryStage) {
		MainApp.primaryStage = primaryStage;
		MainApp.primaryStage.setTitle("Milk Money");
		//MainApp.primaryStage.setMaximized(true);
		initRootLayout();
	}

	/**
	 * Inicializa o root layout e tenta carregar o �ltimo arquivo
	 * de pessoa aberto.
	 */
	public void initRootLayout() {
	    try {
	    	FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
	        rootLayout = (BorderPane) loader.load();
	        Scene scene = new Scene(rootLayout);
	        primaryStage.setScene(scene);
	        primaryStage.show();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	}
	/**
	 * Retorna um objeto controller instanciado pelo spring
	 */
	public static Object getController(Class<?> clazz){
		return context.getBean(clazz);
	}
	/**
	 * Neste m�todo � feita a substitui��o do objeto controller da interface
	 * pelo controller instanciado pelo Spring.
	 * @param url
	 * @return
	 */
	public static Object load(String url) {
		try (InputStream fxmlStream = MainApp.class.getResourceAsStream(url)) {
			FXMLLoader loader = new FXMLLoader();
			loader.setControllerFactory(new Callback<Class<?>, Object>() {
				@Override
				public Object call(Class<?> clazz) {
					return context.getBean(clazz);
				}
			});
			return loader.load(fxmlStream);
		} catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
