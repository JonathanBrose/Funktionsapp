package view;

import static src.Hilfsmethoden.parseDouble;
import java.util.ArrayList;
import static src.Hilfsmethoden.zeigeDialog;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import src.Cos;
import src.ExponentialFunktion;
import src.Funktion;
import src.Funktionsteil;
import src.Ln;
import src.NatuerlicheExponentialFunktion;
import src.PotenzFunktion;
import src.Sin;
import src.XExponentialFunktion;

/**
 * Verwaltet ein Fenster, welches den Nutzer mit einer {@link ComboBox} aus {@link Funktionstyp} ausw&auml;hlen l&auml;sst, und
 * dann mithilfe von Eingabefeldern ({@link TextField}s) das passende {@link Funktionsteil} erstellt.
 * @author Jonathan Brose
 *
 */
public class EingabeController {

	@FXML
	private Label dasFunktionsLabel;
	@FXML
	private Label dasLabel1;
	@FXML
	private Label dasLabel2;
	@FXML
	private TextField dasTextField1;
	@FXML
	private TextField dasTextField2;
	@FXML
	private ComboBox<Funktionstyp> dieComboBox;
	@FXML
	private Button derOkButton;
	@FXML
	private Button derResetButton;
	@FXML
	private TextArea dieBeschreibungsTextArea;
	@FXML
	private GridPane dasButtonGridPane;
	@FXML
	private BorderPane dasBorderPane;
	/**
	 * Wird gesetzt, falls dieser {@link EingabeController} von einem {@link MehrfachEingabeController} erzeugt wurde.
	 */
	private MehrfachEingabeController derVaterController;
	/**
	 * Speichert bzw. setzt die Auswahl von {@link #dieComboBox}.
	 */
	private SelectionModel<Funktionstyp> dasSelectionModel;
	/**
	 * Funktionsteil in dem das aus den aus {@link #dasTextField1} und
	 * {@link #dasTextField2} eingelesenen Daten erstellte Funktionsteil gespeichert
	 * wird.
	 */
	private Funktionsteil dasFunktionsteil;
	/**
	 * Eine Zeiger auf dieFunktionsListe in {@link #derHauptFensterController}<br>
	 * &Uuml;br diese {@link ArrayList} erh&auml;lt
	 * {@link #derHauptFensterController} {@link #dasFunktionsteil}.
	 */
	private ArrayList<Funktionsteil> dieFunktionsTeile;
	/**
	 * Speichert den Status der Eingabe. Erst wenn bereit auf true steht kann ein
	 * {@link MehrfachEingabeController}, der zwei {@link EingabeController}
	 * enth&auml;lt sich deren Funktionsteile holen.
	 * 
	 */
	private boolean bereit;
	/**
	 * Gibt an ob {@link #derResetButton} zu sehen sein soll. Bei der Eingabe im
	 * Hauptfenster soll dieser nich tzu sehen sein.
	 */
	private boolean resetButtonDeaktiviert = true;
	/**
	 * Launcher um den {@link #derMehrfachEingabeController} zu erstellen und zu
	 * &ouml;ffnen. Dies ist nur bei komplexen Eingaben wie
	 * {@link Funktionstyp#Verkettung} oder {@link Funktionstyp#FaktorVerknuepfung}
	 * n&ouml;tig.
	 */
	private EingabeLauncher derEingabeLauncher;
	/**
	 * Verwaltet das von {@link #derEingabeLauncher} erstellte Fenster.
	 */
	private MehrfachEingabeController derMehrfachEingabeController;
	/**
	 * Referenz zum Controller des Hauptfensters.
	 */
	private HauptFensterController derHauptFensterController;

	/**
	 * Initialisiert das Fenster. Wird direkt nach dem Konstruktor durch JavaFx
	 * aufgerufen.
	 */
	@FXML
	private void initialize() {
		dieComboBox.getItems().setAll(Funktionstyp.values());
		dieComboBox.setStyle("-fx-font: 16px \"System\";");
		dasSelectionModel = dieComboBox.getSelectionModel();
		dasSelectionModel.select(Funktionstyp.Polynom);
		setzeFunktionsText("Keine Funktion gespeichert");
		wennComboBoxAktuallisiert();
		passeTextFelderGroeßeAn();
		organisiereButtons();
	}

	/**
	 * Erstellt {@link ChangeListener} f&uuml;r {@link #dasTextField1} und
	 * {@link #dasTextField2} um deren breite anzupassen. Ausserdem wird mit
	 * {@link #organisiereButtons()} &Uuml;berpr&uuml;ft welche Buttons aktiv sind.
	 */
	private void passeTextFelderGroeßeAn() {
		dasTextField1.textProperty().addListener(
				(ObservableValue<? extends String> arg0, String arg1, String arg2)-> {
				dasTextField1.setPrefWidth(28 + dasTextField1.getText().length() * 8); 
				organisiereButtons();
		});
		dasTextField2.textProperty().addListener(
				(ObservableValue<? extends String> arg0, String arg1, String arg2)-> {
				dasTextField2.setPrefWidth(28 + dasTextField2.getText().length() * 8); 
				organisiereButtons();
		});
	}

	/**
	 * &Uuml;berpr&uuml;ft ob {@link #derOkButton} gedr&uuml;ckt werden darf.
	 * 
	 * @return true, wenn {@link #derOkButton} aktiv sein soll, sonst false.
	 */
	private boolean istOkButtonFreigegeben() {
		if (dasSelectionModel.getSelectedItem().equals(Funktionstyp.AddVerknuepfung))
			return true;
		if (dasSelectionModel.getSelectedItem().equals(Funktionstyp.FaktorVerknuepfung))
			return true;
		if (dasSelectionModel.getSelectedItem().equals(Funktionstyp.Verkettung))
			return true;
		if (dasTextField1.isVisible() && dasTextField1.getText().equals(""))
			return false;
		if (dasTextField2.isVisible() && dasTextField2.getText().equals(""))
			return false;

		return true;
	}

	/**
	 * Setzt disable von {@link #derOkButton} mithilfe von
	 * {@link #istOkButtonFreigegeben()} und {@link #derResetButton} mithilfe von
	 * {@link #istBereit()}
	 */
	private void organisiereButtons() {
		this.derOkButton.setDisable(!istOkButtonFreigegeben());
		if (!resetButtonDeaktiviert)
			setzeResetButtonAktiv(istBereit());
	}

	/**
	 * Wird aufgerufen wenn {@link #dieComboBox} ver&auml;ndert wird. Passt die
	 * Sichtbarkeit, Text, usw. von {@link #dasLabel1}, {@link #dasLabel2},
	 * {@link #dasTextField1}, {@link #dasTextField2},
	 * {@link #dieBeschreibungsTextArea} und {@link #derOkButton} an.
	 */

	@FXML
	private void wennComboBoxAktuallisiert() {
		organisiereButtons();
		Funktionstyp funktionstyp = (Funktionstyp) this.dieComboBox.getSelectionModel().getSelectedItem();

		int dieLaenge = funktionstyp.gibParameter().length;

		if (dieLaenge == 0) {

			this.dasLabel1.setVisible(true);
			this.dasLabel1.setText("Eingabe erfolgt in einem seperaten Fenster");
			this.dasTextField1.setVisible(false);
			this.dasTextField2.setVisible(false);
			this.dasLabel2.setVisible(false);
			this.derOkButton.setText("Eingabe öffnen");

		} else if (dieLaenge == 1) {

			this.dasLabel1.setVisible(true);
			this.dasLabel1.setText(funktionstyp.gibParameter()[0]);
			this.dasTextField1.setVisible(true);
			this.dasTextField2.setVisible(false);
			this.dasLabel2.setVisible(false);
			this.derOkButton.setText("OK");

		} else if (dieLaenge == 2) {

			this.dasLabel1.setVisible(true);
			this.dasLabel1.setText(funktionstyp.gibParameter()[0]);
			this.dasTextField1.setVisible(true);
			this.dasTextField2.setVisible(true);
			this.dasLabel2.setVisible(true);
			this.dasLabel2.setText(funktionstyp.gibParameter()[1]);
			this.derOkButton.setText("OK");
		}
		this.dieBeschreibungsTextArea.setText(funktionstyp.gibBeschreibung());
	}

	/**
	 * Wird ausgef&uuml;hrt wenn {@link #derOkButton} gedr&uuml;ckt wurde. Erstellt
	 * dann abh&auml;ngig von dem durch {@link #dieComboBox} ausgew&auml;ten Item
	 * das passende Funktionsteil. <br>
	 * Dabei werden {@link #dasTextField1} und {@link #dasTextField2} eingelesen,
	 * oder Mit {@link EingabeLauncher} ein neues Fenster f&uuml;r die Eingabe
	 * ge&ouml;ffnet.
	 * 
	 */

	@FXML
	private void klickeOkButton() {
		new Thread(() -> {
			dasFunktionsteil = null;

			try {
				switch (dasSelectionModel.getSelectedItem()) {
				case Sin:
					dasFunktionsteil = new Sin(parseDouble(dasTextField1));
					break;
				case Cos:
					dasFunktionsteil = new Cos(parseDouble(dasTextField1));
					break;
				case Exponential:
					dasFunktionsteil = new ExponentialFunktion(parseDouble(dasTextField1), parseDouble(dasTextField2));
					break;
				case NatürlicheExponential:
					dasFunktionsteil = new NatuerlicheExponentialFunktion(parseDouble(dasTextField1));
					break;
				case Polynom:
					dasFunktionsteil = new PotenzFunktion(parseDouble(dasTextField1), parseDouble(dasTextField2));
					break;
				case Ln:
					dasFunktionsteil = new Ln(parseDouble(dasTextField1));
					break;
				case XExponential:
					dasFunktionsteil = new XExponentialFunktion(parseDouble(dasTextField1));
					break;
				case FaktorVerknuepfung:
				case Verkettung:
				case AddVerknuepfung:

					Platform.runLater(() -> {
						derEingabeLauncher = new EingabeLauncher();
						try {
							derEingabeLauncher.start(new Stage());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						derEingabeLauncher.setzeTitel("Eingabe " + dasSelectionModel.getSelectedItem());
						derMehrfachEingabeController = derEingabeLauncher.gibMehrfachEingabeController();
						derEingabeLauncher.öffne();
						derMehrfachEingabeController.setzeFunktionstyp(dasSelectionModel.getSelectedItem());
						derMehrfachEingabeController.ueberpruefeEnum();
					});

					try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					while (!derMehrfachEingabeController.istBereit()) {
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					dasFunktionsteil = derMehrfachEingabeController.gibFunktionsteil();

					break;
				}
			} catch (NumberFormatException e) {
				zeigeDialog("Eingabeproblem",
						"Bitte überprüfen sie ihre Eingabe auf unerlaubte Zeichen.\n Erlaubt sind: alle Zahlen, pi, e und Brüche mit \"/\"");
			}

			Platform.runLater(() -> {
				if (derHauptFensterController != null)
					derHauptFensterController.beendeBerechnungen();
				if (dieFunktionsTeile != null) {
					dieFunktionsTeile.add(dasFunktionsteil);
					bereit = true;
					if(derVaterController!=null) {
						derVaterController.leseEingabenEin();
					}
					if (derHauptFensterController != null) {
						derHauptFensterController.generiereFunktion();
						derHauptFensterController.aktualisiereKurvendisskusionController();
					} else {
						setzeFunktionsText(dasFunktionsteil.gibString(0, "x"));
					}
					if (!derResetButton.isVisible())
						setzteTextFieldsZurueck();
				}
			});

		}).start();

	}
	

	/**
	 * Setzt den Text von {@link #dasTextField1} und {@link #dasTextField2} auf "".
	 */
	private void setzteTextFieldsZurueck() {
		dasTextField1.setText("");
		dasTextField2.setText("");
	}

	/**
	 * Wird ausgef&uuml;hrt wenn {@link #derResetButton} gedr&uuml;ckt wurde. <br>
	 * Setzt den EingabeController in den Ursprungszustand zur&uuml;ck.
	 * 
	 * @see #reset()
	 */
	@FXML
	private void klickeResetButton() {
		reset();
	}

	/**
	 * Versucht den Fokus auf {@link #dieComboBox} zu legen.
	 */
	public void beantrageFokusFuerComboBox() {
		this.dieComboBox.requestFocus();

	}

	/**
	 * Setzt den EingabeController in den Ursprungszustand zur&uuml;ck.
	 */

	public void reset() {
		this.bereit = false;
		this.dasFunktionsteil = null;
		setzeFunktionsText("Keine Funktion gespeichert");
		setzteTextFieldsZurueck();
	}

	/**
	 * Getter f&uuml;r {@link #bereit}
	 * 
	 * @return {@link #bereit}
	 */
	public boolean istBereit() {
		return bereit;
	}

	/**
	 * Setzt den Text von {@link #dasFunktionsLabel}<br>
	 * Wird immer auf dem JavaFX Thread ausgef&uuml;hrt.
	 * 
	 * @param derFunktionsText
	 *            der neue Text f&uuml;r {@link #dasFunktionsLabel}
	 */
	private void setzeFunktionsText(String derFunktionsText) {
		Platform.runLater(() -> {
			dasFunktionsLabel.setText(derFunktionsText);
		});
	}

	/**
	 * Setzt {@link #dieFunktionsTeile}
	 * 
	 * @param dieFunktionsTeile
	 *            die neue {@link ArrayList}
	 */
	public void setzeFunktionsteileListe(ArrayList<Funktionsteil> dieFunktionsTeile) {
		this.dieFunktionsTeile = dieFunktionsTeile;
	}

	/**
	 * Setzt den {@link #derHauptFensterController}
	 * 
	 * @param derHauptFensterController
	 *            der neue {@link HauptFensterController}
	 */
	public void setzteHauptFenster(HauptFensterController derHauptFensterController) {
		this.derHauptFensterController = derHauptFensterController;
	}

	/**
	 * Aktiviert bzw. Deaktiviert den {@link #derResetButton} <br>
	 * Ordnet dabei die Butons passend an.
	 * 
	 * @param aktiv
	 *            entscheidet ob {@link #derResetButton} gezeigt werden soll.
	 */
	public void setzeResetButtonAktiv(boolean aktiv) {
		derResetButton.setVisible(aktiv);
		dasButtonGridPane.getChildren().clear();
		if (aktiv) {
			dasButtonGridPane.add(derOkButton, 1, 0);
			dasButtonGridPane.add(derResetButton, 2, 0);
		} else {
			dasButtonGridPane.add(derOkButton, 2, 0);
			dasButtonGridPane.add(derResetButton, 1, 0);
		}

	}

	/**
	 * Setzt disable von {@link #derOkButton}
	 * 
	 * @param disable
	 *            wenn true wird {@link #derOkButton} deaktiviert.
	 */

	public void setzeOkButtonDisabled(boolean disable) {
		this.derOkButton.setDisable(disable);
	}

	/**
	 * Setzt den Text von {@link #dasFunktionsLabel} mit dem Funktionsterm des
	 * Parameters dieFunktion.
	 * 
	 * @param dieFunktion
	 *            von der der Funktionsterm geholt werden soll.
	 * @see #setzeFunktionsText(String)
	 */
	public void setzeFunktionsText(Funktion dieFunktion) {
		if (dieFunktion != null) {
			setzeFunktionsText(dieFunktion.toString());
		} else {
			setzeFunktionsText("Keine Funktion erzeugt bisher");
		}
	}
	
	public void setzeVaterController(MehrfachEingabeController derVaterController) {
		this.derVaterController = derVaterController;
	}

}
