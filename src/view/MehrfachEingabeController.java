package view;

import static src.Hilfsmethoden.zeigeDialog;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import src.AddVerknuepfung;
import src.FaktorVerknuepfung;
import src.Funktionsteil;
import src.PotenzFunktion;
import src.Verkettung;

/**
 * Controller f&uuml;r MehrfachEingabe.fxml. Verwaltet ein Fenster zur Eingabe komplexer Funktionen, welches zwei 
 * {@link EingabeController} in einem {@link Accordion} anzeigt.
 * @author Jonathan
 *
 */
public class MehrfachEingabeController {

	@FXML
	private Button derHinzufuegenButton;
	@FXML
	private Button derFertigButton;
	@FXML
	private Button derCancelButton;
	@FXML
	private Label dasFunktionsLabel;
	@FXML
	private TitledPane dasTitledPane1;
	@FXML
	private TitledPane dasTitledPane2;
	@FXML
	private Accordion dasAkkordeon;
	/**
	 * Referenz zum Controller der ersten Eingabe.
	 */
	private EingabeController derEingabeController1;
	/**
	 * Referenz zum Controller der zweiten Eingabe.
	 */
	private EingabeController derEingabeController2;
	/**
	 * Gibt an welche Art von {@link Funktionsteil} eingegeben werden soll.
	 */
	private Funktionstyp derFunktionstyp;
	/**
	 * Gibt an ob dieser {@link MehrfachEingabeController} bereit f&uuml;r {@link #gibFunktionsteil()} ist.
	 */
	private Boolean bereit;
	/**
	 * Das {@link Funktionsteil}, das aus den Eingaben generiert wird.
	 */
	private Funktionsteil dasFunktionsteil;
	/**
	 * Liste in die {@link #derEingabeController1} seine Eingaben einf&uuml;gt.
	 */
	private ArrayList<Funktionsteil> dieFunktionsteile;
	/**
	 * Liste in die {@link #derEingabeController2} seine Eingaben einf&uuml;gt.
	 */
	private ArrayList<Funktionsteil> iFunktionsTeile;
	/**
	 * Der {@link EingabeLauncher}, der diese Instanz erstellt hat.
	 */
	private EingabeLauncher derEingabeLauncher;

	/**
	 * Konstruktor, erstellt {@link #dieFunktionsteile} und {@link #iFunktionsTeile}.
	 */
	public MehrfachEingabeController() {
		bereit = Boolean.valueOf(false);
		dieFunktionsteile = new ArrayList<Funktionsteil>();
		iFunktionsTeile = new ArrayList<Funktionsteil>();
	}
	/**
	 * Initialisiert die MehrfachEingabe, wir direkt nach dem Konstruktor aufgerufen.
	 */
	@FXML
	private void initialize() {
		dasFunktionsLabel.setText("Funktion noch nicht erstellt");
		dasAkkordeon.expandedPaneProperty().addListener(
				(ObservableValue<? extends TitledPane> observable, TitledPane altesPane, TitledPane neuesPane) 
				-> halteAkkordionImmerOffen(altesPane, neuesPane));
				
	}
	/**
	 * Falls diese Fenster zur Eingabe einer {@link Verkettung} erstellt wurde, wird {@link #derHinzufuegenButton} deaktiviert.
	 */
	public void ueberpruefeEnum() {
		if (derFunktionstyp.equals(Funktionstyp.Verkettung)) {
			derHinzufuegenButton.setVisible(false);
		}
	}
	/**
	 * Wird immer wenn sich bei {@link #dasAkkordeon} das Ausgeklappte {@link TitledPane} &auml;ndert aufgerufen.
	 * Sorgt daf&uuml;r, dass immer eines der beiden {@link TitledPane}s Ausgeplappt ist. 
	 * @param altesPane das zuletzt ausgeklappte {@link TitledPane}.
	 * @param neuesPane das neue ausgeklappte {@link TitledPane}, falls vorhanden.
	 */
	private void halteAkkordionImmerOffen(TitledPane altesPane, TitledPane neuesPane) {
		Boolean expand = true;
		for (TitledPane dasTitledPane : dasAkkordeon.getPanes()) {
			if (dasTitledPane.isExpanded()) {
				expand = false;
			}
		}

		if ((expand == true) && (altesPane != null)) {
			Runnable r = () -> {
				dasAkkordeon.setExpandedPane(altesPane);
			};
			if (neuesPane == null) {
				if (altesPane == dasTitledPane1)
					r = () -> {
						dasAkkordeon.setExpandedPane(dasTitledPane2);
					};
				else if (altesPane == dasTitledPane2)
					r = () -> {
						dasAkkordeon.setExpandedPane(dasTitledPane1);
					};
			}
			Platform.runLater(r);
		}
	}
	/**
	 * Best&auml;tigt die Eingabe und schlie&szlig;t das Fenster.
	 * Wird von {@link #derFertigButton} ausgef&uuml;hrt.
	 * @see #leseEingabenEin()
	 */
	@FXML
	public void fertig() {
		if (derEingabeController1.istBereit()) {
			if (derEingabeController2.istBereit()) {
				leseEingabenEin();
				bereit = true;
				derEingabeLauncher.schließe();
			} else {
				zeigeDialog("Eingabeproblem", "Bitte überprüfen sie ihre zweite Eingabe");
			}
		} else {
			zeigeDialog("Eingabeproblem", "Bitte überprüfen sie ihre erste Eingabe");
		}
	}
	/**
	 * Speichert die Eingaben und &ouml;ffnet die n&auml;chste.
	 * Wird beim klicken von {@link #derHinzufuegenButton} ausgef&uuml;hrt.
	 */
	@FXML
	private void klickeHinzufuegen() {
		if (derEingabeController1.istBereit()) {
			if (derEingabeController2.istBereit()) {
				leseEingabenEin();
				dasTitledPane2.setVisible(false);
				dasTitledPane1.setText("Eingabe");
				dasTitledPane1.setExpanded(true);
				derEingabeController1.setzeResetButtonAktiv(false);
				derEingabeController1.reset();
	
			} else {
				zeigeDialog("Eingabeproblem", "Bitte überprüfen sie ihre zweite Eingabe");
			}
		} else {
			zeigeDialog("Eingabeproblem", "Bitte überprüfen sie ihre erste Eingabe");
		}
	}
	/**
	 * Bricht die EIngabe ab und schlie&szlig;t das Fenster.
	 */
	@FXML
	private void cancel() {
		this.dasFunktionsteil = null;
		bereit = true;
		derEingabeLauncher.schließe();
	}
	/**
	 * Liest die beide {@link EingabeController} ein und erstellt das daraus resultierende {@link Funktionsteil}.
	 */
	public void leseEingabenEin() {

		switch (derFunktionstyp) {
		case Verkettung:
			Funktionsteil innen, aussen;
			innen = iFunktionsTeile.size() == 0 || iFunktionsTeile.get(0) == null ? new PotenzFunktion(1, 1)
					: iFunktionsTeile.get(0);
			aussen = dieFunktionsteile.size() == 0 || dieFunktionsteile.get(0) == null ? new PotenzFunktion(1, 1)
					: dieFunktionsteile.get(0);
			dasFunktionsteil = new Verkettung(aussen, innen);
			break;
		case AddVerknuepfung:
			dasFunktionsteil = new AddVerknuepfung(wandleListeZuArray());
			break;
		case FaktorVerknuepfung:
			dasFunktionsteil = new FaktorVerknuepfung(wandleListeZuArray());
			break;
		default:
			break;
		}
		dasFunktionsLabel.setText("f(x)= " + dasFunktionsteil.gibString(0, "x"));
	}
	/**
	 * Fasst die {@link Funktionsteil}e aus {@link #iFunktionsTeile} und {@link #dieFunktionsteile} in einem Array zusammen.
	 * @return Array mit allen {@link Funktionsteil}en beider {@link EingabeController}.
	 */
	private Funktionsteil[] wandleListeZuArray() {
		Funktionsteil[] dasFunktionsteil = new Funktionsteil[dieFunktionsteile.size() + iFunktionsTeile.size()];
		int i;
		for (i = 0; i < dieFunktionsteile.size(); i++) {
			dasFunktionsteil[i] = dieFunktionsteile.size() == 0 || dieFunktionsteile.get(i) == null
					? new PotenzFunktion(0, 0)
					: dieFunktionsteile.get(i);
		}
		for (int c = 0; c < iFunktionsTeile.size(); c++) {
			dasFunktionsteil[c + i] = iFunktionsTeile.size() == 0 || iFunktionsTeile.get(c) == null
					? new PotenzFunktion(0, 0)
					: iFunktionsTeile.get(c);
		}
		return dasFunktionsteil;
	}
	/**
	 * Gibt an ob der {@link MehrfachEingabeController} bereit f&uuml;r {@link #gibFunktionsteil()} ist.
	 * @return {@link #bereit}
	 */
	public boolean istBereit() {
		return bereit;
	}
	/**
	 * Setter f&uuml;r {@link #derFunktionstyp}
	 * @param derFunktionstyp {@link #derFunktionstyp}
	 */
	public void setzeFunktionstyp(Funktionstyp derFunktionstyp) {
		this.derFunktionstyp = derFunktionstyp;
		switch (derFunktionstyp) {
		case FaktorVerknuepfung:
		case AddVerknuepfung:
			dasTitledPane1.setText("Eingabe 1");
			dasTitledPane2.setText("Eingabe 2");
		default:
			break;
		}
		dasTitledPane1.setExpanded(true);
	}
	/**
	 * Setter f&uuml;r {@link #derEingabeLauncher}
	 * @param eingabeLauncher {@link EingabeLauncher}, der dieses Objekt erstellt hat.
	 */
	public void setzeEingabeLauncher(EingabeLauncher eingabeLauncher) {
		this.derEingabeLauncher = eingabeLauncher;
	}
	/**
	 * Setter f&uuml;r {@link #derEingabeController1}
	 * @param eingabe1 der {@link EingabeController} von der ersten Eingabe.
	 */
	public void setEingabe1(EingabeController eingabe1) {
		this.derEingabeController1 = eingabe1;
		eingabe1.setzeVaterController(this);
		eingabe1.setzeFunktionsteileListe(dieFunktionsteile);
	
	}
	/**
	 * Setter f&uuml;r {@link #derEingabeController2}
	 * @param eingabe2 der {@link EingabeController} von der zweiten Eingabe.
	 */
	public void setEingabe2(EingabeController eingabe2) {
		this.derEingabeController2 = eingabe2;
		eingabe2.setzeVaterController(this);
		eingabe2.setzeFunktionsteileListe(iFunktionsTeile);
	}
	/**
	 * Setzt dasBorderPane als Inhalt von {@link #dasTitledPane1}
	 * @param dasBorderPane der Inhalt (Eingabe)
	 */
	public void ladeEingabeBorderPane1(BorderPane dasBorderPane) {
		this.dasTitledPane1.setContent(dasBorderPane);
		dasTitledPane1.setExpanded(true);
	}
	/**
	 * Setzt dasBorderPane als Inhalt von {@link #dasTitledPane2}
	 * @param dasBorderPane der Inhalt (Eingabe)
	 */
	public void ladeEingabeBorderPane2(BorderPane dasBorderPane) {
		this.dasTitledPane2.setContent(dasBorderPane);
	}
	/**
	 * Getter f&uuml;r {@link #dasFunktionsteil}
	 * @return {@link #dasFunktionsteil}
	 */
	public Funktionsteil gibFunktionsteil() {
		return dasFunktionsteil;
	}
}
