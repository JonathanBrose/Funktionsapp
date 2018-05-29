package view;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
/**
 * Erstellt einen {@link MehrfachEingabeController} und öffnet das Fenster.
 * @author Jonathan Brose
 * 
 * 
 */
public class EingabeLauncher extends Application {
	/**
	 * Das Usprungs Layout. Wird von MehrfachEingabe.fxml geladen.
	 */
	private BorderPane dasUrsprungsBorderPane;
	/**
	 * Das Fenster der Mehrfacheingabe.
	 */
	private Stage dieHauptStage;
	/**
	 * Der Controller f&uuml;r {@link #dasUrsprungsBorderPane}
	 */
	private MehrfachEingabeController derMehrfachEingabeController;
	
	/**
	 * &Ouml;ffnet das Fenster.
	 */
	public  void öffne() {
		dieHauptStage.show();
	}
	/**
	 * Getter f&uuml;r den erstellten {@link MehrfachEingabeController}.
	 * @return {@link #derMehrfachEingabeController}
	 */
	public MehrfachEingabeController gibMehrfachEingabeController() {
		return derMehrfachEingabeController;
	}
	/**
	 * L&auml;dt {@link #dasUrsprungsBorderPane} und initialisiert {@link #derMehrfachEingabeController}
	 */
	private void initialisiereMehrfachEingabeController() {
		try {
			FXMLLoader derLoader = new FXMLLoader();
			derLoader.setLocation(App.class.getResource("MehrfachEingabe.fxml"));
			dasUrsprungsBorderPane = (BorderPane) derLoader.load();
			derMehrfachEingabeController=(MehrfachEingabeController)derLoader.getController();
			derMehrfachEingabeController.setzeEingabeLauncher(this);
	
			Scene dieSzene = new Scene(dasUrsprungsBorderPane);
			dieHauptStage.setScene(dieSzene);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * L&auml;dt die erste Instanz einer Eingabe und setzt sie in {@link #derMehrfachEingabeController}
	 */
	private void initialisiereEingabeController1() {
		try {
			FXMLLoader derLoader = new FXMLLoader();
			derLoader.setLocation(App.class.getResource("Eingabe.fxml"));
			derMehrfachEingabeController.ladeEingabeBorderPane1((BorderPane) derLoader.load());
			EingabeController derEingabeController=(EingabeController)derLoader.getController();
			derMehrfachEingabeController.setEingabe1(derEingabeController);
			derEingabeController.setzeResetButtonAktiv(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * L&auml;dt die zweite Instanz einer Eingabe und setzt sie in {@link #derMehrfachEingabeController}
	 */
	private void initialisiereEingabeController2() {
		try {
			FXMLLoader derLoader = new FXMLLoader();
			derLoader.setLocation(App.class.getResource("Eingabe.fxml"));
			derMehrfachEingabeController.ladeEingabeBorderPane2((BorderPane) derLoader.load());
			EingabeController derEingabeController=(EingabeController)derLoader.getController();
			derMehrfachEingabeController.setEingabe2(derEingabeController);
			derEingabeController.setzeResetButtonAktiv(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Initialisiert und &ouml;ffnet das Fenster
	 */
	@Override
	public void start(Stage dieStage) throws Exception {
		this.dieHauptStage=dieStage;
		initialisiereMehrfachEingabeController();
		initialisiereEingabeController1();
		initialisiereEingabeController2();
		this.dieHauptStage.getIcons().add(new Image(getClass().getResourceAsStream("iconPi.jpg")));
		öffne();
	}
	/**
	 * Setzt den Titel des Fensters ({@link #dieHauptStage}).
	 * @param derTitel Der neue Titel
	 */
	public void setzeTitel(String derTitel) {
		dieHauptStage.setTitle(derTitel);
	}
	
	/**
	 * Schlie&szlig;t das Fenster wieder.
	 */
	public void schließe() {
		dieHauptStage.close();
		try {
			this.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
