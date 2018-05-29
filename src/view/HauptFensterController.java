package view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.controlsfx.control.Notifications;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;
import src.Funktion;
import src.Funktionsteil;
/**
 * Verwaltet das HauptFenster der Apllikation, welches ein {@link UebersichtController}, ein {@link KurvendisskusionController} und
 * ein {@link EingabeController} beinhaltet.
 * @author Jonathan
 *
 */
public class HauptFensterController {

	@FXML
	private TabPane dasTabPane;
	@FXML
	private MenuItem dasSpeichernMenuItem;
	@FXML
	private MenuItem dasLadenMenuItem;
	@FXML
	private MenuItem dasLoeschenMenuItem;
	@FXML
	private MenuItem dasEingabeMenuItem;
	/**
	 * Speichert die Tabs des Haupfensters um in sie {@link #dasUebersichtBorderPane}, {@link #dasKurvendisskusionBorderPane} und {@link #dasEingabeBorderPane}
	 * einzuf&uuml;gen.
	 */
	private ObservableList<Tab> dieTabs;
	/**
	 * Speichert die {@link Funktionsteil}e aus denen {@link #dieFunktion} generiert wird.
	 */
	private ArrayList<Funktionsteil> dieFunktionsteile;
	/**
	 * Die {@link Funktion} mit der gerechnet und gearbeitet wird.
	 */
	private Funktion dieFunktion;
	/**
	 * Das Layout von {@link #derKurvendisskusionController}
	 */
	private BorderPane dasKurvendisskusionBorderPane;
	/**
	 * Das Layout von {@link #derEingabeController}
	 */
	private BorderPane dasEingabeBorderPane;
	/**
	 * Das Layout von {@link #derUebersichtController}
	 */
	private BorderPane dasUebersichtBorderPane;
	/**
	 * Der Controller von {@link #dasUebersichtBorderPane}
	 */
	private UebersichtController derUebersichtController;
	/**
	 * Der Controller von {@link #dasEingabeBorderPane}
	 */
	private EingabeController derEingabeController;
	/**
	 * Der Controller von {@link #dasKurvendisskusionBorderPane}
	 */
	private KurvendisskusionController derKurvendisskusionController;
	/**
	 * Referenz zu der Klasse die die Applikation und das Fenster erstellt hat. 
	 */
	private App dieApp;
	/**
	 * Speichert den Dateipfad aus dem der Nutzer zuletzt etwas geladen und/oder gespeichert hat.
	 */
	private File derLetzteDateiPfad;
	/**
	 * Initiaisiert {@link #dieFunktionsteile} und {@link #dieFunktion}
	 */
	public HauptFensterController() {
		dieFunktionsteile = new ArrayList<Funktionsteil>();
		this.dieFunktion = new Funktion();

	}
	/**
	 * Initialisiert das Komplette UrsprungsLayout, dessen Controller dies Klasse ist.
	 */
	@FXML
	private void initialize() {
		dieTabs = dasTabPane.getTabs();
		initialisiereUebersichtController();
		initialisiereEingabeController();
		generiereFunktion();
		initialisiereKurvendisskusionController();
		dasLoeschenMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN));
		dasEingabeMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.SHIFT_DOWN));
		dasSpeichernMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
		dasLadenMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));

	}
	/**
	 * L&auml;dt Uebersicht.fxml und initialisiert {@link #derUebersichtController}
	 */
	private void initialisiereUebersichtController() {
		try {
			FXMLLoader derLoader = new FXMLLoader();
			derLoader.setLocation(App.class.getResource("Uebersicht.fxml"));
			dasUebersichtBorderPane = (BorderPane) derLoader.load();
			dieTabs.get(0).setContent(dasUebersichtBorderPane);
			derUebersichtController = derLoader.getController();
			derUebersichtController.setzeHauptFenster(this);
			derUebersichtController.setzeFunktion(dieFunktion);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * L&auml;dt Kurvendisskusion.fxml und initialisiert {@link #derKurvendisskusionController}
	 */
	private void initialisiereKurvendisskusionController() {
		try {
			FXMLLoader derLoader = new FXMLLoader();
			derLoader.setLocation(App.class.getResource("Kurvendisskusion.fxml"));
			dasKurvendisskusionBorderPane = (BorderPane) derLoader.load();
			dieTabs.get(1).setContent(dasKurvendisskusionBorderPane);
			derKurvendisskusionController = derLoader.getController();
			derKurvendisskusionController.setzeHauptFensterController(this);
			derKurvendisskusionController.setzeFunktion(dieFunktion);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * L&auml;dt Eingabe.fxml und initialisiert {@link #derEingabeController}
	 */
	private void initialisiereEingabeController() {
		try {
			FXMLLoader derLoader = new FXMLLoader();
			derLoader.setLocation(App.class.getResource("Eingabe.fxml"));
			dasEingabeBorderPane = (BorderPane) derLoader.load();
			dieTabs.get(2).setContent(dasEingabeBorderPane);
			derEingabeController = derLoader.getController();
			derEingabeController.setzteHauptFenster(this);
			derEingabeController.setzeFunktionsteileListe(dieFunktionsteile);
			derEingabeController.setzeResetButtonAktiv(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Ruft {@link view.KurvendisskusionController#ueberpruefeAkkordeon()} auf.
	 */
	@FXML
	private void ueberpruefeAkkordeon() {
		derKurvendisskusionController.ueberpruefeAkkordeon();
	}
	/**
	 * Erstellt aus {@link #dieFunktionsteile} ein {@link Funktion} und speichert diese in {@link #dieFunktion}.
	 */
	public void generiereFunktion() {
		this.dieFunktion = new Funktion(this.dieFunktionsteile);
		this.derUebersichtController.setzeFunktion(dieFunktion);
		this.derUebersichtController.initialisiereLabels();
		this.derEingabeController.setzeFunktionsText(dieFunktion);
		this.derUebersichtController.zeichne();
		if (derKurvendisskusionController != null) {
			this.derKurvendisskusionController.setzeFunktion(dieFunktion);
			this.derKurvendisskusionController.stoppeRechenThread();
			if(!derKurvendisskusionController.gibLaeuft())
				this.derUebersichtController.ueberpruefeHauptFunktionsButtons();
		}
	}
	/**
	 * 
	 */
	public void aktualisiereKurvendisskusionController() {
		derKurvendisskusionController.reset();
	}
	/**
	 * Aktualisiert die Gr&ouml;&szlig;e von dem Canvas in {@link #derUebersichtController}.
	 * @see view.UebersichtController#aktualisiereCanvas()
	 */
	public void aendereGroeße() {
		derUebersichtController.aktualisiereCanvas();
	}
	/**
	 * &Ouml;ffnet einen Dateidialog und speichert {@link #dieFunktion} unter dem gew&auml;hlten Dateipfad.
	 */
	@FXML
	public void speichere() {
		String derDateiPfad;
		
		derDateiPfad = öffneDateiSpeichernDialog();
		if (derDateiPfad == null) {
			return;
		}
		this.dieFunktion.speichere(derDateiPfad);
		zeigeBenachrichtigung("Datei gespeichert", derDateiPfad);
		generiereFunktion();
	}
	/**
	 * F&uuml;r Shortcut. <br>
	 * &Ouml;ffnet den Tab mit der Eingabe.
	 */
	@FXML
	public void oeffneEingabe() {
		dasTabPane.getSelectionModel().select(2);
		derEingabeController.beantrageFokusFuerComboBox();
	}
	/**
	 * &Ouml;ffnet einen Dateidialog und l&auml;dt {@link #dieFunktion} von der gew&auml;hlten Datei.
	 */
	@FXML
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
		this.dieFunktionsteile.clear();
		this.dieFunktionsteile.addAll(dieFunktion.gibFunktionsTeile());
		this.generiereFunktion();

	}
	/**
	 * L&ouml;scht {@link #dieFunktionsteile} und {@link #dieFunktion}.
	 */
	@FXML
	private void loescheFunktion() {
		zeigeBenachrichtigung("Funktion gelöscht", dieFunktion.toString());
		beendeBerechnungen();
		this.dieFunktionsteile.clear();
		generiereFunktion();
	}
	/**
	 * Zeigt eine Benachrichtigung am unteren rechten Bildschirmrand.
	 * @param titel Der Titel der Benachrichtigung.
	 * @param nachricht Die Nachicht der Benachrichtigung
	 */
	private void zeigeBenachrichtigung(String titel, String nachricht) {

		Notifications derNotificationBuilder = Notifications.create().title(titel).text(nachricht)
				.hideAfter(new Duration(6000)).position(Pos.BOTTOM_RIGHT);
		derNotificationBuilder.darkStyle();
		derNotificationBuilder.show();

	}
	/**
	 * &Ouml;ffnet einen Dialog zum ausw&auml;hlen einer .func Datei.
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
	 * Stoppt die Berechnungen in {@link #derKurvendisskusionController}, falls vorhanden.
	 * @see view.KurvendisskusionController#stoppeRechenThread()
	 */
	public void beendeBerechnungen() {
		this.derKurvendisskusionController.stoppeRechenThread();
	}
	/**
	 * Getter f&uuml;r {@link #derEingabeController}
	 * @return {@link #derEingabeController}
	 */
	public EingabeController gibEingabeController() {
		return this.derEingabeController;
	}
	/**
	 * Getter f&uuml;r {@link #derUebersichtController}
	 * @return {@link #derUebersichtController}
	 */
	public UebersichtController gibUebersichtController() {
		return this.derUebersichtController;
	}
	/**
	 * Getter f&uuml;r die Breite des Fensters.
	 * @return die Breite des Fensters.
	 */
	public double gibBreite() {
		return this.dieApp.gibHauptStage().getWidth();
	}
	/**
	 * Getter f&uuml;r die H&ouml;he des Fensters.
	 * @return die H&ouml;he des Fensters.
	 */
	public double gibHoehe() {
		return this.dieApp.gibHauptStage().getHeight();
	}
	/**
	 * Getter f&uuml;r {@link #derKurvendisskusionController}
	 * @return {@link #derKurvendisskusionController}
	 */
	public KurvendisskusionController gibKurvendisskusionController() {
		return this.derKurvendisskusionController;
	}
	/**
	 * Getter f&uuml;r {@link #dieFunktion}
	 * @return {@link #dieFunktion}
	 */
	public Funktion gibFunktion() {
		return dieFunktion;
	}
	/**
	 * Setter f&uuml;r {@link #dieApp}
	 * @param dieApp neue {@link App}
	 */
	public void setzeApp(App dieApp) {
		this.dieApp = dieApp;
	}
	/**
	 * Setter f&uuml;r {@link #dieFunktionsteile}
	 * @param gibFunktionsTeile die neue {@link ArrayList} von {@link Funktionsteil}en.
	 */
	public void setzeFunktionsteile(ArrayList<Funktionsteil> gibFunktionsTeile) {
		this.dieFunktionsteile=gibFunktionsTeile;
		
	}

}
