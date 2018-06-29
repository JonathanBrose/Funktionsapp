package applikation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.controlsfx.control.Notifications;
import funktionen.Funktion;
import funktionen.Funktionsteil;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;
import static stringEingabe.FunktionBuilder.stringZuFunktion;

/**
 * L&auml;dt die fxml Dateien, erstellt und &ouml;fnet das Hauptfenster. <br>
 * Startpunkt der Applikation {@link #main(String[])}
 * 
 * @author Jonathan Brose
 *
 *
 */
public class Steuerung extends Application {
	/**
	 * Das Hauptfenster der Applikation.
	 */
	private Stage dieHauptStage;
	/**
	 * Das Ursprungslayout der Szene. Wird von HauptFenster.fxml geladen.
	 */
	private BorderPane dasUrsprungsBorderPane;
	/**
	 * Die {@link Funktion} mit der gerechnet und gearbeitet wird.
	 */
	private Funktion dieFunktion;
	/**
	 * Das Layout von {@link #derKurvendisskusionController}
	 */
	private BorderPane dasKurvendisskusionBorderPane;
	/**
	 * Das Layout von {@link #derUebersichtController}
	 */
	private BorderPane dasUebersichtBorderPane;
	/**
	 * Der Controller von {@link #dasUebersichtBorderPane}
	 */
	private UebersichtController derUebersichtController;

	/**
	 * Der Controller von {@link #dasKurvendisskusionBorderPane}
	 */
	private KurvendisskusionController derKurvendisskusionController;
	
	private EinstellungenController derEinstellungsController;
	private BorderPane dasEinstellungsBorderPane;
	
	/**
	 * Speichert den Dateipfad aus dem der Nutzer zuletzt etwas geladen und/oder
	 * gespeichert hat.
	 */
	private File derLetzteDateiPfad;

	/**
	 * Initialisiert das Fenster. Wird direkt nach dem Konstruktor durch JavaFx
	 * aufgerufen.
	 */
	@Override
	public void start(Stage dieHauptStage) throws Exception {
		this.dieHauptStage = dieHauptStage;
		this.dieHauptStage.setTitle("FunktionsApp");
		this.dieHauptStage.getIcons().add(new Image(getClass().getResourceAsStream("iconPi.jpg")));
		initialisiereKurvendisskusionController();
		initialisiereUebersichtController();
		initialisiereEinstellungenController();
		initialisiereHauptFenster();
		this.setzeFunktion(new Funktion());
		
	}

	/**
	 * Lädt HaupFenster.fxml und initialisiert die zugeh&ouml;rige Controller Klasse
	 * {@link HauptFensterController}.
	 */
	private void initialisiereHauptFenster() {
		try {
			FXMLLoader derLoader = new FXMLLoader();
			derLoader.setLocation(Steuerung.class.getResource("HauptFenster.fxml"));
			dasUrsprungsBorderPane = (BorderPane) derLoader.load();

			HauptFensterController derHauptFensterController = derLoader.getController();
			derHauptFensterController.setzeSteuerung(this);
			derHauptFensterController.setzeKurvendisskusion(dasKurvendisskusionBorderPane);
			derHauptFensterController.setzeUebersicht(dasUebersichtBorderPane);
			derHauptFensterController.setzeEinstellung(dasEinstellungsBorderPane);
			Scene scene = new Scene(dasUrsprungsBorderPane);
			scene.addEventFilter(MouseEvent.DRAG_DETECTED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					scene.startFullDrag();
				}
			});
			dieHauptStage.setScene(scene);
			dieHauptStage.show();
			dieHauptStage.widthProperty().addListener((p, oW, nW) -> aendereGroeße());
			dieHauptStage.heightProperty().addListener((p, oH, nH) -> aendereGroeße());
			dieHauptStage.maximizedProperty().addListener((b, o, n) -> aendereGroeße());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * L&auml;dt Uebersicht.fxml und initialisiert {@link #derUebersichtController}
	 */
	private void initialisiereUebersichtController() {
		try {
			FXMLLoader derLoader = new FXMLLoader();
			derLoader.setLocation(Steuerung.class.getResource("Uebersicht.fxml"));
			dasUebersichtBorderPane = (BorderPane) derLoader.load();
			derUebersichtController = derLoader.getController();
			derUebersichtController.setzeSteuerung(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void initialisiereEinstellungenController() {
		try {
			FXMLLoader derLoader = new FXMLLoader();
			derLoader.setLocation(Steuerung.class.getResource("Einstellungen.fxml"));
			dasEinstellungsBorderPane = (BorderPane) derLoader.load();
			derEinstellungsController = derLoader.getController();
			derEinstellungsController.setzeSteuerung(this);
			derUebersichtController.ladeFarben(derEinstellungsController);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void aktuallisiereFarbe() {
		derUebersichtController.aktualisiereCss();
		derUebersichtController.zeichne();
	}

	/**
	 * L&auml;dt Kurvendisskusion.fxml und initialisiert
	 * {@link #derKurvendisskusionController}
	 */
	private void initialisiereKurvendisskusionController() {
		try {
			FXMLLoader derLoader = new FXMLLoader();
			derLoader.setLocation(Steuerung.class.getResource("Kurvendisskusion.fxml"));
			dasKurvendisskusionBorderPane = (BorderPane) derLoader.load();
			derKurvendisskusionController = derLoader.getController();
			derKurvendisskusionController.setzeSteuerung(this);
			derKurvendisskusionController.setzeFunktion(dieFunktion);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 */
	

	public void erstelleFunktion(String funktionsterm) {
		this.setzeFunktion(stringZuFunktion(funktionsterm));
	}

	public void setzeFunktion(Funktion dieFunktion) {
		this.dieFunktion = dieFunktion;
		this.derUebersichtController.setzeFunktion(dieFunktion);
		this.derUebersichtController.zeichne();
		this.derKurvendisskusionController.stoppeRechenThread();
		this.derKurvendisskusionController.setzeFunktion(dieFunktion);
		if (!derKurvendisskusionController.gibLaeuft())
			this.derUebersichtController.ueberpruefeHauptFunktionsButtons();
	}

	/**
	 * Aktualisiert die Gr&ouml;&szlig;e von dem Canvas in
	 * {@link #derUebersichtController}.
	 * 
	 * @see applikation.UebersichtController#aktualisiereCanvas()
	 */
	public void aendereGroeße() {
		derUebersichtController.aktualisiereCanvas();
	}

	/**
	 * &Ouml;ffnet einen Dateidialog und speichert {@link #dieFunktion} unter dem
	 * gew&auml;hlten Dateipfad.
	 */
	public void speichere() {
		String derDateiPfad;

		derDateiPfad = öffneDateiSpeichernDialog();
		if (derDateiPfad == null) {
			return;
		}
		this.dieFunktion.speichere(derDateiPfad);
		zeigeBenachrichtigung("Datei gespeichert", derDateiPfad);
	}

	/**
	 * &Ouml;ffnet einen Dateidialog und l&auml;dt {@link #dieFunktion} von der
	 * gew&auml;hlten Datei.
	 */
	public void lade() {
		String dateiEingabe;
		dateiEingabe = öffneDateiDialog();
		if (dateiEingabe == null) {
			return;
		}
		Funktion dieFunktion = Funktion.lade(dateiEingabe);
		if (dieFunktion == null) {
			zeigeBenachrichtigung("Funktion konnten nicht geladen werden",
					"Datei nicht vorhanden oder Datei fehlerhaft");
			return;
		}
		zeigeBenachrichtigung("Funktion erfolgreich geladen:", dieFunktion.toString());
		this.setzeFunktion(dieFunktion);
	

	}

	/**
	 * L&ouml;scht {@link #dieFunktionsteile} und {@link #dieFunktion}.
	 */
	void loescheFunktion() {
		zeigeBenachrichtigung("Funktion gelöscht", dieFunktion.toString());
		beendeBerechnungen();
		this.setzeFunktion(new Funktion());
	}

	/**
	 * Zeigt eine Benachrichtigung am unteren rechten Bildschirmrand.
	 * 
	 * @param titel
	 *            Der Titel der Benachrichtigung.
	 * @param nachricht
	 *            Die Nachicht der Benachrichtigung
	 */
	private void zeigeBenachrichtigung(String titel, String nachricht) {

		Notifications derNotificationBuilder = Notifications.create().title(titel).text(nachricht)
				.hideAfter(new Duration(6000)).position(Pos.BOTTOM_RIGHT);
		derNotificationBuilder.darkStyle();
		derNotificationBuilder.show();

	}

	/**
	 * &Ouml;ffnet einen Dialog zum ausw&auml;hlen einer .func Datei.
	 * 
	 * @return Der Pfad zur gew&auml;hlten Datei.
	 */
	public String öffneDateiDialog() {

		FileChooser derFileChooser = new FileChooser();
		derFileChooser.getExtensionFilters().add(new ExtensionFilter("Funktionsdatei", "*.func"));
		if (derLetzteDateiPfad == null) {

			derFileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		} else {
			derFileChooser.setInitialDirectory(derLetzteDateiPfad);
		}
		File datei = derFileChooser.showOpenDialog(null);
		if (datei != null) {
			if (derLetzteDateiPfad == null)
				derLetzteDateiPfad = datei.getParentFile();
			return datei.getAbsolutePath();
		}
		return null;

	}

	/**
	 * &Ouml;ffnet einen Dialog zum ausw&auml;hlen speichern einer .func Datei.
	 * 
	 * @return Der Pfad zur gew&auml;hlten Dateipfad.
	 */
	public String öffneDateiSpeichernDialog() {

		FileChooser derFileChooser = new FileChooser();
		derFileChooser.getExtensionFilters().add(new ExtensionFilter("Funktionsdatei", "*.func"));
		if (derLetzteDateiPfad == null) {

			derFileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		} else {
			derFileChooser.setInitialDirectory(derLetzteDateiPfad);
		}
		File datei = derFileChooser.showSaveDialog(null);

		if (datei != null) {
			derLetzteDateiPfad = datei.getParentFile();
			return datei.getAbsolutePath();
		}

		return null;
	}

	/**
	 * Stoppt die Berechnungen in {@link #derKurvendisskusionController}, falls
	 * vorhanden.
	 * 
	 * @see applikation.KurvendisskusionController#stoppeRechenThread()
	 */
	public void beendeBerechnungen() {
		this.derKurvendisskusionController.stoppeRechenThread();
	}

	/**
	 * 
	 */
	public void aktualisiereKurvendisskusionController() {
		derKurvendisskusionController.reset();
	}

	/**
	 * Getter f&uuml;r {@link #derUebersichtController}
	 * 
	 * @return {@link #derUebersichtController}
	 */
	public UebersichtController gibUebersichtController() {
		return this.derUebersichtController;
	}

	/**
	 * Getter f&uuml;r die Breite des Fensters.
	 * 
	 * @return die Breite des Fensters.
	 */
	public double gibBreite() {
		return this.gibHauptStage().getWidth();
	}

	/**
	 * Getter f&uuml;r die H&ouml;he des Fensters.
	 * 
	 * @return die H&ouml;he des Fensters.
	 */
	public double gibHoehe() {
		return this.gibHauptStage().getHeight();
	}

	/**
	 * Getter f&uuml;r {@link #derKurvendisskusionController}
	 * 
	 * @return {@link #derKurvendisskusionController}
	 */
	public KurvendisskusionController gibKurvendisskusionController() {
		return this.derKurvendisskusionController;
	}

	/**
	 * Getter f&uuml;r {@link #dieFunktion}
	 * 
	 * @return {@link #dieFunktion}
	 */
	public Funktion gibFunktion() {
		return dieFunktion;
	}

	/**
	 * Beendet beim schließen der Fensters alle Threads.
	 */
	@Override
	public void stop() {
		System.exit(0);
	}

	public Stage gibHauptStage() {
		return dieHauptStage;
	}

	/**
	 * HauptMethode
	 * 
	 * @param args
	 *            die Java Argumente
	 */
	public static void main(String[] args) {
		launch(args);
	}

	public BorderPane gibUebersichtPane() {
		return this.dasUebersichtBorderPane;
	}

}
