package br.com.milkmoney;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.milkmoney.exception.GlobalExceptionHandler;
import br.com.milkmoney.service.ApplicationService;

public class MainApp extends Application {
	
	public static Stage primaryStage;
	public static BorderPane rootLayout;
	private static ApplicationContext context;
	
	//private static final String DATABASE_START = "D:\\MilkMoney\\database\\bin\\mysqld.exe";
	//private static final String DATABASE_STOP  = "D:\\MilkMoney\\database\\bin\\mysqld.exe -u root shutdown";
	
	private static final String DATABASE_START = "database\\bin\\mysqld.exe";
	private static final String DATABASE_STOP  = "database\\bin\\mysqld.exe -u root shutdown";
		
	public MainApp() {
		context = new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml", "services.xml", "controllers.xml", "daos.xml"});
    	ApplicationService applicationService = (ApplicationService)getBean(ApplicationService.class);
		applicationService.initilizeDatabase();
	}
	
	
	@Override
	public void start(Stage primaryStage) {
		showMainStage(primaryStage);
	}
	
    private void showMainStage(Stage stage) {
    	try {
    		
    		primaryStage = stage;
    		
	    	FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("view/root/RootLayout.fxml"));
	        rootLayout = (BorderPane) loader.load();
	       
	        AnchorPane centeredMenu = (AnchorPane) load("view/root/CenteredMenu.fxml");
	        VBox.setVgrow(centeredMenu, Priority.SOMETIMES);
	        HBox.setHgrow(centeredMenu, Priority.SOMETIMES);
	        rootLayout.setCenter(centeredMenu);
	        
	        primaryStage.setTitle("Milk Money");
	        
	        Scene scene = new Scene(rootLayout);
	        scene.getStylesheets().add("css/style.css");
	        
	        primaryStage.setScene(scene);
	        primaryStage.show();
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
    }

	public static void resetLayout() {
	    try {
	    	
	        AnchorPane centeredMenu = (AnchorPane) load("view/root/CenteredMenu.fxml");
	        VBox.setVgrow(centeredMenu, Priority.SOMETIMES);
	        HBox.setHgrow(centeredMenu, Priority.SOMETIMES);
	        rootLayout.setCenter(centeredMenu);
	        
	    	
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	}

	/**
	 * Retorna um objeto controller instanciado pelo spring
	 */
	public static Object getBean(Class<?> clazz){
		return context.getBean(clazz);
	}
	/**
	 * Neste método é feita a substituição do objeto controller da interface
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
		
		Locale locale = new Locale("pt", "BR");
		Locale.setDefault(locale);
		
		Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());  
		
		launch(args);
		
	}
}
