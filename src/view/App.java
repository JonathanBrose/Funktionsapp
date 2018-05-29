package view;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
/**
 * L&auml;dt die fxml Dateien, erstellt und &ouml;fnet das Hauptfenster.
 *<br> Startpunkt der Applikation {@link #main(String[])}
 * @author Jonathan Brose
 *
 *
 */
public class App extends Application {
	/**
	 * Das Hauptfenster der Applikation. 
	 */
	private Stage dieHauptStage;
	/**
	 * Das Ursprungslayout der Szene. Wird von HauptFenster.fxml geladen.
	 */
	private BorderPane dasUrsprungsBorderPane;
	
	public App() {
	}
	/**
     * Initialisiert das Fenster. Wird direkt nach dem Konstruktor durch JavaFx aufgerufen.
    */
	@Override
	public void start(Stage dieHauptStage) throws Exception {
		this.dieHauptStage = dieHauptStage;
		this.dieHauptStage.setTitle("FunktionsApp");
		this.dieHauptStage.getIcons().add(new Image(getClass().getResourceAsStream("iconPi.jpg")));
		initialisiereHauptFenster();
	}

	/**
	 * L‰dt HaupFenster.fxml und initialisiert die zugeh&ouml;rige Controller Klasse {@link HauptFensterController}. 
	 */
	private void initialisiereHauptFenster() {
		try {
			FXMLLoader derLoader = new FXMLLoader();
			derLoader.setLocation(App.class.getResource("HauptFenster.fxml"));
			dasUrsprungsBorderPane = (BorderPane) derLoader.load();
			
			HauptFensterController derHauptFensterController =derLoader.getController();
			derHauptFensterController.setzeApp(this);
			
			Scene scene = new Scene(dasUrsprungsBorderPane);
			scene.addEventFilter(MouseEvent.DRAG_DETECTED , new EventHandler<MouseEvent>() {
			    @Override
			    public void handle(MouseEvent mouseEvent) {
			        scene.startFullDrag();
			    }
			});
			dieHauptStage.setScene(scene);
			dieHauptStage.show();
			dieHauptStage.widthProperty().addListener((p,oW, nW)-> derHauptFensterController.aendereGroeﬂe());
			dieHauptStage.heightProperty().addListener((p,oH, nH)-> derHauptFensterController.aendereGroeﬂe());
			dieHauptStage.maximizedProperty().addListener((b,o,n)->derHauptFensterController.aendereGroeﬂe());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Beendet beim schlieﬂen der Fensters alle Threads.
	 */
	@Override
	public void stop(){
	    System.exit(0);
	}


	public Stage gibHauptStage() {
		return dieHauptStage;
	}
	/**
	 * HauptMethode
	 * @param args die Java Argumente
	 */
	public static void main(String[] args) {
		launch(args);
	}

	

}
