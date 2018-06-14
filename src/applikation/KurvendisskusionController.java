package applikation;

import static funktionen.Hilfsmethoden.parseDouble;
import static funktionen.Hilfsmethoden.ueberpruefeDouble;

import java.util.ArrayList;

import funktionen.Funktion;
import funktionen.Intervall;
import funktionen.Punkt;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
/**
 * Verwaltet ein Fenster, welches in einem eingegebenen Intervall, analytische Berechnungen &uuml;ber {@link #dieFunktion} ausf&uuml;hrt. 
 * @author Jonathan
 *
 */
public class KurvendisskusionController {
	
	@FXML
	private TextField dasTextField1;
	@FXML
	private TextField dasTextField2;
	@FXML
	private TitledPane dasNullstellenTitledPane;
	@FXML
	private TitledPane dasExtrempunkteTitledPane;
	@FXML
	private TitledPane dasWendepunkteTitledPane;
	@FXML
	private TitledPane dasSattelpunkteTitledPane;
	@FXML
	private TitledPane dasIntegralTitledPane;
	@FXML
	private TitledPane dasFlaechenTitledPane;
	@FXML
	private TitledPane dasBogenlaengeTitledPane;
	@FXML
	private TitledPane dasMittewertTitledPane;
	@FXML
	private TitledPane dasRotationsvolumenTitledPane;
	@FXML
	private Button derAuswahlButton;
	@FXML
	private ProgressIndicator derProgressIndicator;
	@FXML
	private CheckBox diePunkteAnzeigenCheckBox;
	@FXML 
	private Button derStopButton;
	@FXML 
	private Button derDefaultButton;
	/**
	 * {@link Thread}, der zum berechnen der berechne...Methoden benutzt wird. So kann die Berechnung parallel laufen.
	 */
	private Thread derRechenThread;
	/**
	 * {@link Runnable} die f&uuml;r den {@link #derRechenThread} genutzt wird.
	 */
	private Runnable dieRunnable;
	/**
	 * Gibt an ob eine Berechung in dem {@link #derRechenThread} l&auml;ft.
	 */
	private volatile boolean laeuft;
	/**
	 * Referenz zu der Funktion aus {@link #dieSteuerung}.
	 */
	private Funktion dieFunktion;
	/**
	 * Intervall, in dem berechnet wird.
	 */
	private Intervall derIntervall;
	/**
	 * Nullstellen von {@link #dieFunktion}. K&ouml;nnen in {@link UebersichtController} gezeichnet werden.
	 */
	private ArrayList<Punkt> dieNullstellen;
	/**
	 * Extrempunkte von {@link #dieFunktion}. K&ouml;nnen in {@link UebersichtController} gezeichnet werden.
	 */
	private ArrayList<Punkt> dieExtrempunkte;
	/**
	 * Wendepunkte von {@link #dieFunktion}. K&ouml;nnen in {@link UebersichtController} gezeichnet werden.
	 */
	private ArrayList<Punkt> dieWendepunkte;
	/**
	 * Sattelpunkte von {@link #dieFunktion}. K&ouml;nnen in {@link UebersichtController} gezeichnet werden.
	 */
	private ArrayList<Punkt> dieSattelpunkte;
	/**
	 * Refernez zum Controller des HauptFensters.
	 */
	private Steuerung dieSteuerung;

	/**
	 * Initialisiert das KurvendisskusionFenster.
	 * Wird direkt nach dem Konstruktor aufgerufen.
	 */
	@FXML
	private void initialize() {
		dieNullstellen = new ArrayList<Punkt>();
		dieExtrempunkte = new ArrayList<Punkt>();
		dieWendepunkte = new ArrayList<Punkt>();
		dieSattelpunkte = new ArrayList<Punkt>();
		derAuswahlButton.setTooltip(new Tooltip("Berechnet das ausgeklappte Fenster."));
		derDefaultButton.setTooltip(new Tooltip("Setzt den Intervall auf [-50:50] zur¸ck."));
		derStopButton.setTooltip(new Tooltip("Stoppt alle laufenden Berechnungen und lˆscht die Ergebnisse."));
		diePunkteAnzeigenCheckBox.setTooltip(new Tooltip("Zeichnet die berechneten Punkte in der Ansicht der Funktion ein."));
		diePunkteAnzeigenCheckBox.selectedProperty().addListener((p,o,n)->dieSteuerung.aendereGroeﬂe());
		setzeAufStandard();

	}
	/**
	 * Setzt die Intervall anzeige und {@link #derIntervall} auf Standard ([-50;50]);
	 */
	@FXML
	private void setzeAufStandard() {
		derIntervall = new Intervall(-50, 50);
		aktualisereIntervallAnzeige();
	}
	/**
	 * Setzt den Text von {@link #dasTextField1} und {@link #dasTextField2} nach den Werten von {@link #derIntervall}
	 */
	private void aktualisereIntervallAnzeige() {
		dasTextField1.setText(derIntervall.gibLinkenWert() + "");
		dasTextField2.setText(derIntervall.gibRechtenWert() + "");
	}
	/**
	 * Setzt {@link #dieFunktion}. Dabei wird alles zur&uuml;ckgesetzt. 
	 * @param dieFunktion die neue {@link Funktion}.
	 * @see #reset()
	 */
	public void setzeFunktion(Funktion dieFunktion) {
		this.dieFunktion = dieFunktion;
		reset();
	}
	/**
	 * Wird aufgerufen wenn das {@link Accordion} mit den {@link TitledPane}s angeklickt wird, oder sich ver&auml;ndert.
	 * <br> Setzt den Text und disable von {@link #derAuswahlButton} passend zur Auswahl.
	 */
	@FXML
	public void ueberpruefeAkkordeon() {
		if (dasNullstellenTitledPane.isExpanded()) {
			derAuswahlButton.setText("Nullstellen berechnen");
			derAuswahlButton.setDisable(false);
		} else if (dasExtrempunkteTitledPane.isExpanded()) {
			derAuswahlButton.setText("Extrempunkte berechnen");
			derAuswahlButton.setDisable(false);
		} else if (dasWendepunkteTitledPane.isExpanded()) {
			derAuswahlButton.setText("Wendepunkte berechnen");
			derAuswahlButton.setDisable(false);
		} else if (dasSattelpunkteTitledPane.isExpanded()) {
			derAuswahlButton.setText("Sattelpunkte berechnen");
			derAuswahlButton.setDisable(false);
		} else if (dasIntegralTitledPane.isExpanded()) {
			derAuswahlButton.setText("Integral berechnen");
			derAuswahlButton.setDisable(false);
		} else if (dasFlaechenTitledPane.isExpanded()) {
			derAuswahlButton.setText("Fl‰che berechnen");
			derAuswahlButton.setDisable(false);
		} else if (dasBogenlaengeTitledPane.isExpanded()) {
			derAuswahlButton.setText("Bogenl‰nge berechnen");
			derAuswahlButton.setDisable(false);
		} else if (dasMittewertTitledPane.isExpanded()) {
			derAuswahlButton.setText("Mittelwert berechnen");
			derAuswahlButton.setDisable(false);
		} else if (dasRotationsvolumenTitledPane.isExpanded()) {
			derAuswahlButton.setText("Rotationsvolumen berechnen");
			derAuswahlButton.setDisable(false);
		} else {
			derAuswahlButton.setText("Auswahl berechnen");
			derAuswahlButton.setDisable(true);
		}
	}
	/**
	 * Setzt den Fortschritt von {@link #derProgressIndicator}.<br>
	 * Wird immer auf dem JavafX-Thread ausgef&uuml;hrt.
	 * @param fortschritt der neue Fortschritt von 0 - 1.0
	 */
	private void aktualisiereFortschritt(double fortschritt) {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				derProgressIndicator.setProgress(fortschritt);				
			}
		});
	}
	/**
	 * Stoppt alle Berechnungen und setzt die ganze Kurvendisskusionklasse, bis auf {@link #dieFunktion} und {@link #derIntervall} auf Standard zur&uuml;ck.
	 */
	@FXML
	public void reset() {
		stoppeRechenThread();
		setzeText("", dasNullstellenTitledPane);
		setzeText("", dasExtrempunkteTitledPane);
		setzeText("", dasWendepunkteTitledPane);
		setzeText("", dasSattelpunkteTitledPane);
		setzeText("", dasIntegralTitledPane);
		setzeText("", dasFlaechenTitledPane);
		setzeText("", dasBogenlaengeTitledPane);
		setzeText("", dasMittewertTitledPane);
		setzeText("",dasRotationsvolumenTitledPane);
		this.dieNullstellen.clear();
		this.dieExtrempunkte.clear();
		this.dieWendepunkte.clear();
		this.dieSattelpunkte.clear();
	}
	/**
	 * Berechnet alle Analytischen Berechnungen von {@link #dieFunktion}. Dabei ist die Auswahl egal.
	 */
	@FXML
	public void berechneAlle() {
		if (!laeuft) {
			leseIntervallEin();
			dieRunnable= new Runnable() {

				@Override
				public void run() {
					setzeLaeuft(true);
					Runnable r = ()->dieSteuerung.gibUebersichtController().zuHauptFunktionsButtonsAktiv(false);
					Platform.runLater(r);
					aktualisiereFortschritt(0.1);
					berechneNullstellen(derIntervall);
					aktualisiereFortschritt(0.2);
					berechneExtrempunkte(derIntervall);
					aktualisiereFortschritt(0.3);
					berechneWendepunkte(derIntervall);
					aktualisiereFortschritt(0.4);
					berechneSattelpunkte(derIntervall);
					aktualisiereFortschritt(0.5);
					Runnable task2 = (() -> aktualisiereZeichnung());
					Platform.runLater(task2);
					berechneIntegral(derIntervall);
					if(ueberpruefeInterrupt())return;
					aktualisiereFortschritt(0.6);
					berechneFlaeche(derIntervall);
					if(ueberpruefeInterrupt())return;
					aktualisiereFortschritt(0.7);
					berechneRotationsVolumen(derIntervall);
					if(ueberpruefeInterrupt())return;
					aktualisiereFortschritt(0.8);
					berechneMittelwert(derIntervall);
					if(ueberpruefeInterrupt())return;
					aktualisiereFortschritt(0.9);
					berechneBogenlaenge(derIntervall);
					if(ueberpruefeInterrupt())return;
					aktualisiereFortschritt(1);
					setzeLaeuft(false);
					r = ()->dieSteuerung.gibUebersichtController().zuHauptFunktionsButtonsAktiv(true);
					Platform.runLater(r);

				}
			};
			derRechenThread=new Thread(dieRunnable);
			derRechenThread.start();
		}

	}
	/**
	 * &Uuml;berpr&uuml;ft ob der Thread interrupted wurde, und setzt dann einige Objekte zur&uuml;ck.
	 * @return gibt zur&uuml;ck ob der Thread interrupted ist oder nicht.
	 */
	private boolean ueberpruefeInterrupt() {
		boolean interrupted = Thread.interrupted();
		if(interrupted) {
			setzeLaeuft(false);
			aktualisiereFortschritt(0);
			Runnable r = ()->dieSteuerung.gibUebersichtController().zuHauptFunktionsButtonsAktiv(true);
			Platform.runLater(r);
		}
		return interrupted;
	}
	/**
	 * Bezweckt eine neuberechnung von dem {@link javafx.scene.canvas.Canvas} und dessen Gr&ouml;&szlig;e im {@link UebersichtController}. 
	 * @see HauptFensterController#aendereGroeﬂe()
	 */
	private void aktualisiereZeichnung() {
		dieSteuerung.aendereGroeﬂe();
	}
	/**
	 * Beendet den {@link #derRechenThread}, bzw. setzt sein interrupt auf true.
	 */
	public void stoppeRechenThread() {
		if(this.derRechenThread!=null)
			this.derRechenThread.interrupt();
		
	}
	/**
	 * Setter f&uuml;r {@link #laeuft}. L‰uft gibt an ob noch Berechnungen in {@link #derRechenThread} laufen.
	 * @param laeuft gibt an ob {@link #derRechenThread} noch rechnet.
	 */
	private void setzeLaeuft(boolean laeuft) {
		this.laeuft=laeuft;
		this.dieSteuerung.gibUebersichtController().setzeFunktionEingabeAktiv(!laeuft);
		this.derStopButton.setDisable(!laeuft);
	}

	/**
	 * Berechnet das in dem {@link Accordion} ge&ouml;ffnete Element.
	 */
	@FXML
	private void berechneAuswahl() {

		if (!laeuft) {
			leseIntervallEin();
			dieSteuerung.gibUebersichtController().zuHauptFunktionsButtonsAktiv(false);
			dieRunnable=new Runnable() {

				@Override
				public void run() {
					setzeLaeuft(true);
					aktualisiereFortschritt(0.1);
					if(ueberpruefeInterrupt())return;
					if (dasNullstellenTitledPane.isExpanded())
						berechneNullstellen(derIntervall);
					else if (dasExtrempunkteTitledPane.isExpanded())
						berechneExtrempunkte(derIntervall);
					else if (dasWendepunkteTitledPane.isExpanded())
						berechneWendepunkte(derIntervall);
					else if (dasSattelpunkteTitledPane.isExpanded())
						berechneSattelpunkte(derIntervall);
					else if (dasIntegralTitledPane.isExpanded())
						berechneIntegral(derIntervall);
					else if (dasFlaechenTitledPane.isExpanded())
						berechneFlaeche(derIntervall);
					else if (dasBogenlaengeTitledPane.isExpanded())
						berechneBogenlaenge(derIntervall);
					else if (dasMittewertTitledPane.isExpanded())
						berechneMittelwert(derIntervall);
					 else if (dasRotationsvolumenTitledPane.isExpanded()) 
						berechneRotationsVolumen(derIntervall);
					
					setzeLaeuft(false);
					if(ueberpruefeInterrupt())return;
					aktualisiereFortschritt(1);
					Runnable r = ()->dieSteuerung.gibUebersichtController().zuHauptFunktionsButtonsAktiv(true);
					Platform.runLater(r);
				}
			};
			derRechenThread=new Thread(dieRunnable);
			derRechenThread.start();
		}

	}
	
	/**
	 * Liest die Daten von {@link #dasTextField1} und {@link #dasTextField2} und erstellt dauraus einen neuen Intervall.
	 * Dieser &Uuml;berschreibt dann {@link #derIntervall}
	 */
	private void leseIntervallEin() {
		this.derIntervall = new Intervall(parseDouble(dasTextField1), parseDouble(dasTextField2));
	}
	/**
	 * Berechnet das Rotationsvolumen von {@link #dieFunktion}
	 * @param derIntervall {@link Intervall} in dem gerechnet werden soll.
	 * @see Funktion#berechneRotationsVolumen(Intervall)
	 */
	private void berechneRotationsVolumen(Intervall derIntervall) {
		setzeText("wird berechnet...", dasRotationsvolumenTitledPane);
		Double rotationsVolumen= this.dieFunktion.berechneRotationsVolumen(derIntervall);
		String text = "Rotationsvolumen in"+derIntervall.toString()+": "+ueberpruefeDouble(rotationsVolumen);
		setzeText(text, dasRotationsvolumenTitledPane);
		
	}
	/**
	 * Berechnet {@link #dieNullstellen} von {@link #dieFunktion}
	 * @param derIntervall {@link Intervall} in dem gerechnet werden soll.
	 * @see Funktion#berechneNullstellen(Intervall)
	 */
	private void berechneNullstellen(Intervall derIntervall) {
		setzeText("wird berechnet...", dasNullstellenTitledPane);
		ArrayList<Punkt> dieNullstellen = this.dieFunktion.berechneNullstellen(derIntervall);
		this.dieNullstellen.clear();
		this.dieNullstellen.addAll(dieNullstellen);
		String text = this.wandleZuString(dieNullstellen);
		setzeText(text, dasNullstellenTitledPane);
	}
	/**
	 * Berechnet {@link #dieExtrempunkte} von {@link #dieFunktion}
	 * @param derIntervall {@link Intervall} in dem gerechnet werden soll.
	 * @see Funktion#berechneExtrempunkte(Intervall)
	 */
	private void berechneExtrempunkte(Intervall derIntervall) {
		setzeText("wird berechnet...", dasExtrempunkteTitledPane);
		ArrayList<Punkt> dieExtrempunkte = this.dieFunktion.berechneExtrempunkte(derIntervall);
		this.dieExtrempunkte.clear();
		this.dieExtrempunkte.addAll(dieExtrempunkte);
		String text = this.wandleZuString(dieExtrempunkte);
		setzeText(text, dasExtrempunkteTitledPane);
	}
	/**
	 * Berechnet {@link #dieWendepunkte} von {@link #dieFunktion}
	 * @param derIntervall {@link Intervall} in dem gerechnet werden soll.
	 * @see Funktion#berechneWendepunkte(Intervall)
	 */
	private void berechneWendepunkte(Intervall derIntervall) {
		setzeText("wird berechnet...", dasWendepunkteTitledPane);
		ArrayList<Punkt> dieWendepunkte = this.dieFunktion.berechneWendepunkte(derIntervall);
		this.dieWendepunkte.clear();
		this.dieWendepunkte.addAll(dieWendepunkte);
		String text = this.wandleZuString(dieWendepunkte);
		setzeText(text, dasWendepunkteTitledPane);
	}
	/**
	 * Berechnet {@link #dieSattelpunkte} von {@link #dieFunktion}
	 * @param derIntervall {@link Intervall} in dem gerechnet werden soll.
	 * @see Funktion#berechneSattelpunkte(Intervall)
	 */
	private void berechneSattelpunkte(Intervall derIntervall) {
		setzeText("wird berechnet...", dasSattelpunkteTitledPane);
		ArrayList<Punkt> dieSattelpunkte = this.dieFunktion.berechneSattelpunkte(derIntervall);
		this.dieSattelpunkte.clear();
		this.dieSattelpunkte.addAll(dieSattelpunkte);
		String text = this.wandleZuString(dieSattelpunkte);
		setzeText(text, dasSattelpunkteTitledPane);
	}
	/**
	 * Berechnet das Integral von {@link #dieFunktion}
	 * @param derIntervall {@link Intervall} in dem gerechnet werden soll.
	 * @see Funktion#berechneIntegral(Intervall)
	 */
	public void berechneIntegral(Intervall derIntervall) {
		setzeText("wird berechnet...", dasIntegralTitledPane);
		double integral = this.dieFunktion.berechneIntegral(derIntervall);
		setzeText("Integral in " + derIntervall.toString() + ": " + ueberpruefeDouble(integral), dasIntegralTitledPane);
	}
	/**
	 * Berechnet die Fl&auml;che von {@link #dieFunktion}
	 * @param derIntervall {@link Intervall} in dem gerechnet werden soll.
	 * @see Funktion#berechneFlaeche(Intervall)
	 */
	public void berechneFlaeche(Intervall derIntervall) {
		setzeText("wird berechnet...", dasFlaechenTitledPane);
		double flaeche = this.dieFunktion.berechneFlaeche(derIntervall);
		setzeText("Fl‰che in " + derIntervall.toString() + ": " + ueberpruefeDouble(flaeche), dasFlaechenTitledPane);
	}
	/**
	 * Berechnet die Bogenl&auml;nge von {@link #dieFunktion}
	 * @param derIntervall {@link Intervall} in dem gerechnet werden soll.
	 * @see Funktion#berechneBogenlaenge(Intervall)
	 */
	public void berechneBogenlaenge(Intervall derIntervall) {
		setzeText("wird berechnet...", dasBogenlaengeTitledPane);
		double bogenlaenge = this.dieFunktion.berechneBogenlaenge(derIntervall);
		setzeText("L‰nge des Bogens in " + derIntervall.toString() + ": " + ueberpruefeDouble(bogenlaenge), dasBogenlaengeTitledPane);
	}
	/**
	 * Berechnet den Mittelwert von {@link #dieFunktion}
	 * @param derIntervall {@link Intervall} in dem gerechnet werden soll.
	 * @see Funktion#berechneMittelwert(Intervall)
	 */
	public void berechneMittelwert(Intervall derIntervall) {
		setzeText("wird berechnet...", dasMittewertTitledPane);
		double mittelwert = this.dieFunktion.berechneMittelwert(derIntervall);
		setzeText("Mittelwert in " + derIntervall.toString() + ": " + ueberpruefeDouble(mittelwert), dasMittewertTitledPane);
	}
	/**
	 * Wandelt ein {@link ArrayList} von {@link Punkt}en zu einem String um.
	 * Dabei wird nach jedem {@link Punkt} eine neue Zeile begonnen.
	 * @param diePunkte die umzuwandelnde Liste
	 * @return der umgewandelte String.
	 */
	private String wandleZuString(ArrayList<Punkt> diePunkte) {

		if (diePunkte.isEmpty())
			return "Nichts gefunden";

		String derString = "";

		for (Punkt derPunkt :diePunkte) {
			derString += derPunkt.toString() + System.lineSeparator();
		}

		return derString;
	}
	/**
	 * Setzt den Text des parameters dasTitledPane.<br>
	 * Wird immer auf dem JavaFx Thread ausgef&uuml;hrt.
	 * @param derString der neue Text.
	 * @param dasTitledPane das {@link TitledPane}, dessen Text durch derString gesetzt werden soll.
	 */
	private void setzeText(String derString, TitledPane dasTitledPane) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				ScrollPane dasScrollPane = (ScrollPane) dasTitledPane.getContent();
				TextFlow dasTextFlow = (TextFlow) dasScrollPane.getContent();
				Text derText = new Text(derString);
				derText.setFont(new Font(16));
				dasTextFlow.getChildren().clear();
				dasTextFlow.getChildren().add(derText);

			}
		});

	}
	/**
	 * Getter f&uuml; {@link #dieNullstellen}
	 * @return {@link #dieNullstellen}
	 */
	public ArrayList<Punkt> gibNullstellen() {
		return dieNullstellen;
	}
	/**
	 * Getter f&uuml; {@link #dieExtrempunkte}
	 * @return {@link #dieExtrempunkte}
	 */
	public ArrayList<Punkt> gibExtrempunkte() {
		return dieExtrempunkte;
	}
	/**
	 * Getter f&uuml; {@link #dieWendepunkte}
	 * @return {@link #dieWendepunkte}
	 */
	public ArrayList<Punkt> gibWendepunkte() {
		return dieWendepunkte;
	}
	/**
	 * Getter f&uuml; {@link #dieSattelpunkte}
	 * @return {@link #dieSattelpunkte}
	 */
	public ArrayList<Punkt> gibSattelpunkte() {
		return dieSattelpunkte;
	}
	/**
	 * Gibt zur&uuml;ck ob {@link #diePunkteAnzeigenCheckBox} ausgew&auml;hlt ist.
	 * @return {@link CheckBox#isSelected()}
	 */
	public boolean sollenPunkteAngezeigtWerden() {
		return diePunkteAnzeigenCheckBox.isSelected();
	}
	/**
	 * Setter f&uuml; {@link #dieSteuerung}
	 * @param dieSteuerung neue {@link #dieSteuerung}
	 */
	public void setzeSteuerung(Steuerung dieSteuerung) {
		this.dieSteuerung = dieSteuerung;
	}
	/**
	 * Getter f&uuml; {@link #dieNullstellen}
	 * @return {@link #dieNullstellen}
	 */
	public boolean gibLaeuft() {
		return this.laeuft;
	}

}
