package br.com.milkmoney;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
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
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.milkmoney.exception.GlobalExceptionHandler;
import br.com.milkmoney.service.ApplicationService;

public class MainApp extends Application {
	
	public  static Stage              primaryStage;
	public  static BorderPane         rootLayout;
	private static ApplicationContext context;
	
	public  static final String       APPLICATION_ICON = "img/icon.png";
	private static final String       ICON_REBANHO     = "img/rebanho.png";
	private static final String       ICON_REPRODUCAO  = "img/reproducao.png";
	private static final String       ICON_INDICADORES = "img/indicadores.png";
	private static final String       ICON_PRODUCAO    = "img/producao.png";
	private static final int          SPLASH_WIDTH     = 628;
    private static final int          SPLASH_HEIGHT    = 140;
    //private static final String       DATABASE_START   = "D:\\MilkMoney\\database\\bin\\mysqld.exe";
  	//private static final String       DATABASE_STOP    = "D:\\MilkMoney\\database\\bin\\mysqld.exe -u root shutdown";
  	private static final String       DATABASE_START = "database\\bin\\mysqld.exe";
  	private static final String       DATABASE_STOP  = "database\\bin\\mysqld.exe -u root shutdown";

    private Pane                      splashLayout;
    private ProgressBar               loadProgress;
    private Label                     progressText;
    
	private static final boolean      SPLASH           = false;
	private static final boolean      START_DATABASE   = false;
		
	public MainApp() {
		if ( !SPLASH ){
			context = new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml", "services.xml", "controllers.xml", "daos.xml"});
			
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					ApplicationService applicationService = (ApplicationService)getBean(ApplicationService.class);
					applicationService.initilizeDatabase();
				}
			});
			
		}
	}
	
	@Override
	public void start(Stage primaryStage) {
		
		if ( SPLASH ){
			final Task<ObservableList<String>> task = new Task<ObservableList<String>>() {
	            @Override
	            protected ObservableList<String> call() throws InterruptedException {
	            	//INICIA O BANCO DE DADOS
	            	if ( START_DATABASE ){
	            		try{
	            			updateMessage("Iniciando banco de dados . . .");
		        			ProcessBuilder databaseProcess = new ProcessBuilder(DATABASE_START);
		        			databaseProcess.start();
		        		}catch (IOException e){
		        		    e.printStackTrace();
		        		}
	            	}
	            	
	            	//INJE��O DE DEPEND�NCIA
	            	updateMessage("Carregando aplica��o . . .");
	            	context = new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml", "services.xml", "controllers.xml", "daos.xml"});
	            	
	            	//INICIALIZA��O BANCO DE DADOS
	            	Platform.runLater(new Runnable() {
	    				@Override
	    				public void run() {
	    					updateMessage("Inicializando banco de dados . . .");
	    	            	ApplicationService applicationService = (ApplicationService)getBean(ApplicationService.class);
	    	        		applicationService.initilizeDatabase();
	    				}
	    			});
	            	
	                updateMessage("Todos os arquivos foram carregados.");
	                return null;
	            }
	        };
	
	        showSplash(primaryStage,task,() -> showMainStage(primaryStage));
	        
	        new Thread(task).start();
		}else{
			showMainStage(primaryStage);			
		}
		
	}
	
	@Override
    public void init() {
    	
    	if ( SPLASH ){
	        loadProgress = new ProgressBar();
	        loadProgress.setPrefWidth(SPLASH_WIDTH);
	        progressText = new Label("Carregando dados . . .");
	        progressText.setTextFill(Color.BLACK);
	        splashLayout = new VBox();
	        splashLayout.setPrefHeight(SPLASH_HEIGHT);
	        ((VBox)splashLayout).setAlignment(Pos.BOTTOM_LEFT);
	        
	        HBox hbox = new HBox();
	        hbox.setAlignment(Pos.CENTER);
	        hbox.getChildren().add(new ImageView(new Image(ClassLoader.getSystemResourceAsStream(ICON_REBANHO))));
	        hbox.getChildren().add(new ImageView(new Image(ClassLoader.getSystemResourceAsStream(ICON_PRODUCAO))));
	        hbox.getChildren().add(new ImageView(new Image(ClassLoader.getSystemResourceAsStream(ICON_REPRODUCAO))));
	        hbox.getChildren().add(new ImageView(new Image(ClassLoader.getSystemResourceAsStream(ICON_INDICADORES))));
	        hbox.setSpacing(10);
	        
	        splashLayout.getChildren().addAll(hbox, progressText, loadProgress);
	        progressText.setAlignment(Pos.CENTER);
	        
	        //"-fx-background-image: url('" + SPLASH_IMAGE + "'); " +	
	        splashLayout.setStyle(
	                "-fx-padding: 1; " +
	                "-fx-background-color: white; " +
	                "-fx-border-width:0; " +
	                "-fx-border-color: " +
	                    "linear-gradient(" +
	                        "to bottom, " +
	                        "chocolate, " +
	                        "derive(chocolate, 50%)" +
	                    ");"
	        );
	        splashLayout.setEffect(new DropShadow());
    	}
    	
    }
	
	private void showSplash(final Stage initStage,Task<?> task,InitCompletionHandler initCompletionHandler) {
        progressText.textProperty().bind(task.messageProperty());
        loadProgress.progressProperty().bind(task.progressProperty());
        task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                loadProgress.progressProperty().unbind();
                loadProgress.setProgress(1);
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
    		
    		primaryStage = stage;
    		
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
	        
	        primaryStage.setTitle("Milk Money");
	        
	        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
        			if ( START_DATABASE ){
        				try {
	        				Runtime.getRuntime().exec(DATABASE_STOP);
						} catch (IOException e) {
							e.printStackTrace();
						}
        			}
				}
		    }); 
	        
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
		
		Locale locale = new Locale("pt", "BR");
		Locale.setDefault(locale);
		
		Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());  
		
		launch(args);
		
	}
}