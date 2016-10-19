package br.com.milkmoney;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.milkmoney.controller.applicationUpdate.ApplicationUpdateController;
import br.com.milkmoney.exception.GlobalExceptionHandler;
import br.com.milkmoney.service.ApplicationUpdateService;

public class MainApp extends Application {
	
	public  static Stage              primaryStage;
	public  static BorderPane         rootLayout;
	private static ApplicationContext context;
	
	public  static final String       APPLICATION_ICON = "img/icon.png";
	private static final String       SPLASH_IMAGE     = "img/splash.png";
    private static final int          SPLASH_HEIGHT    = 182;
  	private static final String       DATABASE_START   = "database\\bin\\mysqld.exe";
  	//private static final String       DATABASE_STOP    = "database\\bin\\mysqld.exe -u root shutdown";

    private Pane                      splashLayout;
    //private ProgressBar               loadProgress;
    private Label                     progressText;
    
	private static final boolean      SPLASH           = false;
	private static final boolean      START_DATABASE   = false;
		
	private static ObjectProperty<Cursor> cursor = new SimpleObjectProperty<>(Cursor.DEFAULT);
	
	public MainApp() {
		
		//INICIA O BANCO DE DADOS
    	if ( START_DATABASE ){
    		try{
    			ProcessBuilder databaseProcess = new ProcessBuilder(DATABASE_START);
    			databaseProcess.start();
    		}catch (IOException e){
    		    e.printStackTrace();
    		}
    	}
		
		ApplicationUpdateService applicationService = new ApplicationUpdateService();
		String novaVersao = applicationService.getNumeroNovaVersao();
		
		if ( novaVersao != null ){
			
			try (InputStream fxmlStream = MainApp.class.getResourceAsStream("view/applicationUpdate/ApplicationUpdateForm.fxml")) {
				FXMLLoader loader = new FXMLLoader();
				AnchorPane applicationUpdate = (AnchorPane) loader.load(fxmlStream);
				Stage s = new Stage();
				s.setTitle("Milk Money - Atualização");
				Scene scene = new Scene(applicationUpdate);
				s.setScene(scene);
				
				s.setResizable(false);
				
				ApplicationUpdateController controller = loader.getController();
				controller.setService(applicationService);
				controller.setVersao(novaVersao);
				
				s.showAndWait();
			} catch (IOException ioException) {
				throw new RuntimeException(ioException);
			}
			
		}
		
	}
	
	@Override
	public void start(Stage primaryStage) {
		
		if ( SPLASH ){
			final Task<ObservableList<String>> task = new Task<ObservableList<String>>() {
	            @Override
	            protected ObservableList<String> call() throws InterruptedException {
	            	//INJEÇÃO DE DEPENDÊNCIA
	            	updateMessage("Carregando aplicação . . .");
	            	context = new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml", "services.xml", "controllers.xml", "daos.xml"});
	            	
	                updateMessage("Todos os arquivos foram carregados.");
	                return null;
	            }
	        };
	
	        showSplash(primaryStage,task,() -> showMainStage(primaryStage));
	        
	        new Thread(task).start();
		}else{
			context = new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml", "services.xml", "controllers.xml", "daos.xml"});
			showMainStage(primaryStage);			
		}
		
	}
	
	@Override
    public void init() {
    	
    	if ( SPLASH ){

    		progressText = new Label("Carregando dados . . .");
	        progressText.setTextFill(Color.BLACK);
	        splashLayout = new VBox();
	        splashLayout.setMaxHeight(SPLASH_HEIGHT);
	        
	        HBox hbox = new HBox();
	        hbox.setAlignment(Pos.CENTER);
	        hbox.getChildren().add(new ImageView(new Image(ClassLoader.getSystemResourceAsStream(SPLASH_IMAGE))));
	        
	        splashLayout.getChildren().addAll(new ImageView(new Image(ClassLoader.getSystemResourceAsStream(SPLASH_IMAGE))), progressText);
	        
	        progressText.setAlignment(Pos.CENTER);
	        
	        splashLayout.setStyle(
	                "-fx-padding: 1; " +
	                "-fx-background-color: white; " +
	                "-fx-border-width: 0.5; " +
	                "-fx-border-color:gray; "
	        );
	        splashLayout.setEffect(new DropShadow());
    	}
    	
    }
	
	private void showSplash(final Stage initStage,Task<?> task,InitCompletionHandler initCompletionHandler) {
        progressText.textProperty().bind(task.messageProperty());
        task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                initStage.toFront();
                FadeTransition fadeSplash = new FadeTransition(Duration.seconds(1.2), splashLayout);
                fadeSplash.setFromValue(1.0);
                fadeSplash.setToValue(0.0);
                fadeSplash.setOnFinished(actionEvent -> initStage.hide());
                fadeSplash.play();

                initCompletionHandler.complete();
            } // todo add code to gracefully handle other task states.
        });

        Scene splashScene = new Scene(splashLayout);
        initStage.initStyle(StageStyle.UNDECORATED);
        initStage.setScene(splashScene);
        initStage.centerOnScreen();
        initStage.show();
    }

    public interface InitCompletionHandler {
        public void complete();
    }
	
    private void showMainStage(Stage stage) {
    	try {
    		
	    	//FXMLLoader loader = new FXMLLoader();
	       // loader.setLocation(MainApp.class.getResource());
	        rootLayout = (BorderPane) load("view/root/RootLayout.fxml");
	        primaryStage = new Stage(StageStyle.DECORATED);
	        primaryStage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream(APPLICATION_ICON)));
	        primaryStage.setTitle("Milk Money");
	        AnchorPane centeredMenu = (AnchorPane) load("view/root/CenteredMenu.fxml");
	        VBox.setVgrow(centeredMenu, Priority.SOMETIMES);
	        HBox.setHgrow(centeredMenu, Priority.SOMETIMES);
	        rootLayout.setCenter(centeredMenu);
	        
	        /*primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
        			if ( START_DATABASE ){
        				try {
	        				Runtime.getRuntime().exec(DATABASE_STOP);
						} catch (IOException e) {
							e.printStackTrace();
						}
        			}
				}
		    });*/ 
	        
	        Scene scene = new Scene(rootLayout);
	        scene.getStylesheets().add("css/style.css");
	        
	        primaryStage.setScene(scene);
	        primaryStage.getScene().cursorProperty().bind(cursor);
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
	
	public static void setCursor(Cursor c) {
		cursor.set(c);
	}

	public static void main(String[] args) {
		
		Locale locale = new Locale("pt", "BR");
		Locale.setDefault(locale);
		
		Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());  
		
		launch(args);
		
	}

}
