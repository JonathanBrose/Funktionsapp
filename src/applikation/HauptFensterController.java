package applikation;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
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
	/**
	 * Speichert die Tabs von {@link #dasTabPane}
	 */
	private ObservableList<Tab> dieTabs;
	
	/**
	 * Referenz zu der Klasse die die Applikation und das Fenster erstellt hat. 
	 */
	private Steuerung dieSteuerung;
	
	/**
	 * Initialisiert das Komplette UrsprungsLayout, dessen Controller dies Klasse ist.
	 */
	@FXML
	private void initialize() {
		dieTabs = dasTabPane.getTabs();
		dasLoeschenMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN));
		dasSpeichernMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
		dasLadenMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
	}
	
	
	/**
	 * Ruft {@link applikation.KurvendisskusionController#ueberpruefeAkkordeon()} auf.
	 */
	@FXML
	private void ueberpruefeAkkordeon() {
		dieSteuerung.gibKurvendisskusionController().ueberpruefeAkkordeon();
	}
	@FXML
	private void lade() {
		dieSteuerung.lade();
	}
	@FXML
	private void speichere() {
		dieSteuerung.speichere();
	}
	@FXML
	private void loescheFunktion() {
		dieSteuerung.loescheFunktion();
	}
	
	/**
	 * Setter f&uuml;r {@link #dieSteuerung}
	 * @param dieApp neue {@link Steuerung}
	 */
	public void setzeSteuerung(Steuerung dieApp) {
		this.dieSteuerung = dieApp;
	}
	public void setzeKurvendisskusion(BorderPane dasKurvendisskusionPane) {
		this.dieTabs.get(1).setContent(dasKurvendisskusionPane);
	}
	public void setzeUebersicht(BorderPane dasUebersichtPane) {
		this.dieTabs.get(0).setContent(dasUebersichtPane);
	}
	public void setzeEinstellung(BorderPane dasEinstellungsBorderPane) {
		this.dieTabs.get(2).setContent(dasEinstellungsBorderPane);
	}

}
