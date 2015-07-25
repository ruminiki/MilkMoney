package br.com.milkmoney;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import javafx.animation.FadeTransition;
import javafx.application.Application;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
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

public class MainSplash extends Application {
	
    public static final String APPLICATION_ICON = "img/icon.png";
    public static final String SPLASH_IMAGE     = "img/splash.png";

    private Pane splashLayout;
    private ProgressBar loadProgress;
    private Label progressText;
    private static final int SPLASH_WIDTH = 836;
    private static final int SPLASH_HEIGHT = 357;
    
	public static Stage primaryStage;
	public Stage dialogStage;
	public static BorderPane rootLayout;
	private static ApplicationContext context;
	
	//private static final String DATABASE_START = "D:\\MilkMoney\\database\\bin\\mysqld.exe";
	//private static final String DATABASE_STOP  = "D:\\MilkMoney\\database\\bin\\mysqld.exe -u root shutdown";
	
	private static final String DATABASE_START = "database\\bin\\mysqld.exe";
	private static final String DATABASE_STOP  = "database\\bin\\mysqld.exe -u root shutdown";
	private static final boolean SPLASH = false;
		
	public MainSplash() {
		context = new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml", "services.xml", "controllers.xml", "daos.xml"});
    	ApplicationService applicationService = (ApplicationService)getBean(ApplicationService.class);
		applicationService.initilizeDatabase();
		showMainStage();
	}
	
	
	@Override
	public void start(Stage primaryStage) {
		
		if ( SPLASH ){
			final Task<ObservableList<String>> task = new Task<ObservableList<String>>() {
	            @Override
	            protected ObservableList<String> call() throws InterruptedException {
	            	//INICIA O BANCO DE DADOS
	            	updateMessage("Iniciando banco de dados . . .");
	        		/*try{
	        			
	        			ProcessBuilder databaseProcess = new ProcessBuilder(DATABASE_START);
	        			databaseProcess.start();
	        		}catch (IOException e){
	        		    e.printStackTrace();
	        		}*/
	            	
	            	//INJEÇÃO DE DEPENDÊNCIA
	            	updateMessage("Carregando aplicação . . .");
	            	context = new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml", "services.xml", "controllers.xml", "daos.xml"});
	            	
	            	updateMessage("Inicializando banco de dados . . .");
	            	ApplicationService applicationService = (ApplicationService)getBean(ApplicationService.class);
	        		applicationService.initilizeDatabase();
	            	
	        		//AVANÇA O PROGRESS BAR
	                for (int i = 0; i < 50; i++) {
	                    Thread.sleep(100);
	                    updateProgress(i + 1, 50);
	                    updateMessage("Carregando . . . " + (i * 2) + "%");
	                }
	                Thread.sleep(400);
	                updateMessage("Todos os arquivos foram carregados.");
	                return null;
	            }
	        };
	
	        showSplash(primaryStage,task,() -> showMainStage());
	        
	        new Thread(task).start();
		}
	}
	
    @Override
    public void init() {
    	
    	if ( SPLASH ){
	        loadProgress = new ProgressBar();
	        loadProgress.setPrefWidth(SPLASH_WIDTH - 20);
	        progressText = new Label("Carregando dados . . .");
	        progressText.setTextFill(Color.WHITE);
	        splashLayout = new VBox();
	        splashLayout.setPrefHeight(SPLASH_HEIGHT);
	        ((VBox)splashLayout).setAlignment(Pos.BOTTOM_LEFT);
	        splashLayout.getChildren().addAll(progressText, loadProgress);
	        progressText.setAlignment(Pos.CENTER);
	        splashLayout.setStyle(
	                "-fx-padding: 1; " +
	                "-fx-background-image: url('" + SPLASH_IMAGE + "'); " +		
	                "-fx-background-color: cornsilk; " +
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

    
    private void showMainStage() {
    	try {
    		
	    	FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
	        rootLayout = (BorderPane) loader.load();
	        
	        primaryStage = new Stage(StageStyle.DECORATED);
	        primaryStage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream(APPLICATION_ICON)));
	        primaryStage.setTitle("Milk Money");
	        
	        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
        			/*try {
        				Runtime.getRuntime().exec(DATABASE_STOP);
					} catch (IOException e) {
						e.printStackTrace();
					}*/
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
       // final Rectangle2D bounds = Screen.getPrimary().getBounds();
        initStage.setScene(splashScene);
        initStage.centerOnScreen();
        //initStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2);
        //initStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2);
        initStage.show();
    }

    public interface InitCompletionHandler {
        public void complete();
    }
    
	public static void resetLayout() {
	    try {
	    	rootLayout.getChildren().remove(1);
	    	
	    	/*FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
	        rootLayout = (BorderPane) loader.load();
	    	*/
	    	Scene scene = new Scene(rootLayout);
	        scene.getStylesheets().add("css/style.css");
	        
	        primaryStage.setScene(scene);
	        primaryStage.centerOnScreen();
	    	
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
