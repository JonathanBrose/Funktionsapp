package applikation;
import funktionen.Funktion;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class UebersichtController {
	
	
	@FXML
	private TextField dasStammfunktionTextField;
	@FXML
	private TextField dasFunktionsTextField;
	@FXML
	private TextField dasAbleitungTextField1;
	@FXML
	private TextField dasAbleitungTextField2;
	@FXML
	private Canvas dasCanvas;
	@FXML
	private TitledPane dasFunktionsLabelPane;
	@FXML
	private RadioButton derStammFunktionButton;
	@FXML
	private RadioButton derFunktionButton;
	@FXML
	private RadioButton derErsteAbleitungButton;
	@FXML
	private RadioButton derZweiteAbleitungButton;
	@FXML
	private Button derHauptFunktButton1;
	@FXML
	private Button derHauptFunktButton3;
	@FXML
	private Button derHauptFunktButton4;
	@FXML
	private ScrollPane dasScrollPane;
	@FXML 
	private GridPane dasFunktionenGridPane;
	/**
	 * Speichert die letzte Position der Maus um bei einer Bewegung die differenz dem Plotter als deltaX zu &uuml;bergeben.
	 */
	private double letzteMausXpos = 0, letzteMausYpos = 0;
	/**
	 * Der Plotter verwaltet {@link #dasCanvas} und zeichnet auf diesem.
	 */
	private Plotter derPlotter;
	/**
	 * Referenz von der {@link Funktion} aus {@link #derHauptFensterController}. Diese wird mit Stammfunktion, erster und zweiter Ableitung gespeichert.
	 */
	private Funktion dieStammfunktion, dieFunktion, dieErsteAbleitung, dieZweiteAbleitung;
	
	private Steuerung dieSteuerung;
	/**
	 * Initialisiert die Funktions&uuml;bersicht und {@link #derPlotter}.
	 * Wird direkt nach dem Konstruktor aufgerufen.
	 */
	@FXML
	public void initialize() {
	
		derPlotter = new Plotter(dasCanvas);
		dasFunktionsLabelPane.setExpanded(true);
		zeichne();
		dasFunktionsLabelPane.expandedProperty().addListener((obs, oldB, newB) -> this.klappeFunktionsTitledPaneAus(oldB, newB));
		dasFunktionsLabelPane.heightProperty().addListener((obs, oldB, newB) -> this.aktualisiereCanvas());
		dasCanvas.setOnScroll(this::canvasScroll);
		dasCanvas.setOnMouseDragOver(this::canvasVerschiebung);
		dasCanvas.setOnMouseDragEntered(this::canvasVerschiebungStart);
		dasCanvas.setOnZoom(this::canvasZoom);
		initialisiereHauptFunktButtons();
		ueberpruefeHauptFunktionsButtons();
		Runnable r = ()->{
			try {
				Thread.sleep(15*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			loescheSTRGInfo();
		};
		new Thread(r).start();
		setzeTextFieldListener();
	}
	private void setzeTextFieldListener() {
		aktiviereTextFieldAutomatischesWachsen(dasFunktionsTextField);
		aktiviereTextFieldAutomatischesWachsen(dasAbleitungTextField1);
		aktiviereTextFieldAutomatischesWachsen(dasAbleitungTextField2);
		aktiviereTextFieldAutomatischesWachsen(dasStammfunktionTextField);
	}
	private void aktiviereTextFieldAutomatischesWachsen(TextField dasTextField) {
		dasTextField.textProperty().addListener(
				(ObservableValue<? extends String> arg0, String arg1, String arg2)-> {
					dasTextField.setPrefWidth(dasTextField.getText().length() * 7); 
				
		});
	}
	/**
	 * Entfernt ein Label mit der Info, dass mit STRG + Mausrad gezoomt werden kann.
	 */
	private void loescheSTRGInfo() {
		Runnable r1 = () -> {
			BorderPane dasBorderPane = (BorderPane) dasCanvas.getParent();
			dasBorderPane.setTop(null);
			aktualisiereCanvas();
		};
		Platform.runLater(r1);
	}
	
	/**
	 * Gibt die H&ouml;he des Info Labels &uuml;ber dem Canvas zur&uuml;ck.
	 * @return die H&ouml;he.
	 */
	private double gibSTRGHoehe() {
		BorderPane dasBorderPane = (BorderPane) dasCanvas.getParent();
		if(dasBorderPane.getTop()==null) return 0;
		
		Label dasStrgInfoLabel= (Label) dasBorderPane.getTop();
		return dasStrgInfoLabel.getHeight();
	}
	
	/**
	 * Setzt ein {@link Tooltip} f&uuml;r die HauptFunktButtons.
	 */
	private void initialisiereHauptFunktButtons() {
		Tooltip dasTooltip = new Tooltip();
		dasTooltip.setText("�berschreibt die Funktion des Programms mit der Funktion links von diesem Knopf.");
		derHauptFunktButton1.setTooltip(dasTooltip);
		derHauptFunktButton3.setTooltip(dasTooltip);
		derHauptFunktButton4.setTooltip(dasTooltip);
	}
	/**
	 * Initialisiert den Text der Labels. Dabei werden diese mit den Funktionstermen von {@link #dieStammfunktion}, {@link #dieFunktion}
	 * , {@link #dieErsteAbleitung} und {@link #dieZweiteAbleitung} best&uuml;ckt.
	 */
	public void initialisiereLabels() {
		if (dieFunktion != null) {
			String derFehlerText = "Stammfunktion kann nicht bestimmt werden";
			dasStammfunktionTextField.setText(dieStammfunktion == null ? derFehlerText : dieStammfunktion.toString());
			dasFunktionsTextField.setText(dieFunktion.toString());
			dasAbleitungTextField1.setText(dieErsteAbleitung.toString());
			dasAbleitungTextField2.setText(dieZweiteAbleitung.toString());
		} else {
			dasStammfunktionTextField.setText("F(x)=");
			dasFunktionsTextField.setText("f(x)=");
			dasAbleitungTextField1.setText("f'(x)=");
			dasAbleitungTextField2.setText("f''(x)=");
		}
	
	}
	/**
	 * Sorgt daf&uuml;r, dass {@link #dasCanvas} sich an die Gr&ouml;&szlig;e des Fensters und seiner Umgebung anpasst, 
	 * da ein {@link Canvas} eine feste Gr&ouml;&szlig;e hat.
	 */
	public void aktualisiereCanvas() {
		dasCanvas.setHeight(0);
		dasCanvas.setWidth(0);
		Runnable r = () -> {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Runnable r2 = () -> {
				BorderPane dasBorderPane = ((BorderPane) dasCanvas.getParent());
				dasCanvas.setWidth(dasBorderPane.getWidth());
				dasCanvas.setHeight(dasBorderPane.getHeight() - dasFunktionsLabelPane.getHeight()- gibSTRGHoehe());
				zeichne();
			};
			Platform.runLater(r2);
		};
		new Thread(r).start();
	
	}
	/**
	 * Zeichnet das Koordinatensystem, die Punkte, die Funktionen usw... 
	 */
	public void zeichne() {
		if(dasCanvas.getWidth() == 0 || dasCanvas.getHeight() == 0)
			return;
		derPlotter.loescheZeichnung();
		derPlotter.plotteGitter();
		derPlotter.plotteAchsen();
		if (derStammFunktionButton.isSelected())
			derPlotter.plotteFunktion(dieStammfunktion, Color.DARKBLUE);
		if (derFunktionButton.isSelected()) {
			derPlotter.plotteFunktion(dieFunktion, Color.ORANGERED);
			if (dieSteuerung != null)
				derPlotter.plotteBesonderePunkte(dieSteuerung.gibKurvendisskusionController(),
						Color.ORANGERED);
		}
		if (derErsteAbleitungButton.isSelected())
			derPlotter.plotteFunktion(dieErsteAbleitung, Color.GREEN);
		if (derZweiteAbleitungButton.isSelected())
			derPlotter.plotteFunktion(dieZweiteAbleitung, Color.BLUEVIOLET);
	}
	/**
	 * Sorgt daf&uuml;r, dass das {@link #dasFunktionsLabelPane} sich asuklappen kann, in dem {@link #dasCanvas}
	 * kurzzeitig verschwindet.
	 * @param alterWert der vorherige Status, ob {@link #dasFunktionsLabelPane} ausgeklappt ist.
	 * @param neuerWert der jetzige Status, ob {@link #dasFunktionsLabelPane} ausgeklappt ist.
	 */
	public void klappeFunktionsTitledPaneAus(boolean alterWert, boolean neuerWert) {
		if (!alterWert && neuerWert) {
			dasCanvas.setHeight(0);
			dasCanvas.setWidth(0);
		}
	}
	@FXML
	private void leseFunktionEin(KeyEvent dasKeyEvent) {
		if(dasKeyEvent.getCode() == KeyCode.ENTER)
			dieSteuerung.erstelleFunktion(dasFunktionsTextField.getText());
	}

	/**
	 * &Uuml;berpr&uuml;ft welche HauptFunktButtons aktiv sein d&uuml;rfen, und welche nicht.
	 */
	public void ueberpruefeHauptFunktionsButtons() {
		if (this.dieFunktion == null) {
			derHauptFunktButton1.setDisable(true);
			derHauptFunktButton3.setDisable(true);
			derHauptFunktButton4.setDisable(true);
		} else if (this.dieFunktion.entsprichtXAchse()) {
			derHauptFunktButton1.setDisable(true);
			derHauptFunktButton3.setDisable(true);
			derHauptFunktButton4.setDisable(true);
		} else if (this.dieStammfunktion == null) {
			derStammFunktionButton.setDisable(true);
			derStammFunktionButton.setSelected(false);
			derHauptFunktButton1.setDisable(true);
			derHauptFunktButton3.setDisable(false);
			derHauptFunktButton4.setDisable(false);
		} else {
			derStammFunktionButton.setDisable(false);
			derHauptFunktButton1.setDisable(false);
			derHauptFunktButton3.setDisable(false);
			derHauptFunktButton4.setDisable(false);
		}
	
	}
	/**
	 * Setzt den Aktiv Status der HauptFunktButtons, oder aktiviert bzw. deaktiviert diese. 
	 * @param aktiv Status ob die HauptFunktButtons aktiv sein sollen.
	 */
	public void zuHauptFunktionsButtonsAktiv(boolean aktiv) {
		derHauptFunktButton1.setDisable(!aktiv);
		derHauptFunktButton3.setDisable(!aktiv);
		derHauptFunktButton4.setDisable(!aktiv);
		ueberpruefeHauptFunktionsButtons();
	}
	/**
	 * Ruft {@link #speichereAlsHauptfunktion(int)} mit dem Parameter 1 auf.
	 */
	@FXML
	public void speicherAlsHaupt1() {
		speichereAlsHauptfunktion(1);
	}
	/**
	 * Ruft {@link #speichereAlsHauptfunktion(int)} mit dem Parameter 3 auf.
	 */
	@FXML
	public void speicherAlsHaupt3() {
		speichereAlsHauptfunktion(3);
	}
	/**
	 * Ruft {@link #speichereAlsHauptfunktion(int)} mit dem Parameter 4 auf.
	 */
	@FXML
	public void speicherAlsHaupt4() {
		speichereAlsHauptfunktion(4);
	}
	/**
	 * Speichert die &uuml;ber den index ausgew&auml;hlte Funktion als HauptFunktion.
	 * @param index index der Funktion die &uuml;bernommen werden soll.
	 */
	public void speichereAlsHauptfunktion(int index) {
		Funktion dieFunktion;
		switch (index) {
		case 1:
			dieFunktion = dieSteuerung.gibFunktion().gibStammfunktion();
			if (dieFunktion == null)
				return;
			break;
		case 3:
			dieFunktion = dieSteuerung.gibFunktion().gibAbleitung();
			break;
		case 4:
			dieFunktion = dieSteuerung.gibFunktion().gibAbleitung(2);
			break;
		default:
			dieFunktion = dieSteuerung.gibFunktion();
		}
		dieSteuerung.beendeBerechnungen();
		dieSteuerung.setzeFunktion(dieFunktion);
		
	}
	/**
	 * Wird vom ZoomListener aufgerufen. Kontrolliert die Skalierung von {@link #derPlotter}
	 * @param dasZoomEvent das ZoomEvent mit den Informationen wie stark gezoomt wird.
	 */
	private void canvasZoom(ZoomEvent dasZoomEvent) {
		derPlotter.setzeSkalierung(derPlotter.gibSkalierung() / dasZoomEvent.getZoomFactor());
	}
	/**
	 * Wird aufgerufen wenn die Maus bei gedr&uuml;ckter Maustaste verschoben wird. Verschiebt das Bild vom {@link #derPlotter} passend.
	 * @param dasMausEvent MausEvent mit den Informationen wie weit die Maus verschoben wurde.
	 */
	private void canvasVerschiebungStart(MouseDragEvent dasMausEvent) {
		if (dasMausEvent.isSynthesized())
			return;
		if (dasMausEvent.getButton() != MouseButton.PRIMARY)
			return;
		letzteMausXpos = dasMausEvent.getX();
		letzteMausYpos = dasMausEvent.getY();

	}
	/**
	 * Wird aufgerufen wenn die Maus bei gedr&uuml;ckter Maustaste verschoben wird. Verschiebt das Bild vom {@link #derPlotter} passend.
	 * @param dasMausEvent MausEvent mit den Informationen wie weit die Maus verschoben wurde.
	 */
	private void canvasVerschiebung(MouseDragEvent dasMausEvent) {
		if (dasMausEvent.isSynthesized())
			return;
		if (dasMausEvent.getButton() != MouseButton.PRIMARY)
			return;

		double xNeu = dasMausEvent.getX(), yNeu = dasMausEvent.getY();

		derPlotter.addiereZuDeltaX(((xNeu - letzteMausXpos) / 60) * derPlotter.gibSkalierung());
		derPlotter.addiereZuDeltaY(((letzteMausYpos - yNeu) / 60) * derPlotter.gibSkalierung());
		letzteMausXpos = xNeu;
		letzteMausYpos = yNeu;
		zeichne();
	}
	/**
	 *Bei einem Touchscreen wird durch die Scrollgeste das Bild von {@link #derPlotter} verschoben.
	 * Beim Scrollen mit der Maus und dr&uuml;cken von strg wird gezoomt.
	 * @param dasScrollEvent {@link ScrollEvent} mit den Informationen f&uuml;r die Verschiebung.
	 */
	private void canvasScroll(ScrollEvent dasScrollEvent) {

		if (dasScrollEvent.isDirect() || !dasScrollEvent.isControlDown()) {
			derPlotter.addiereZuDeltaX(derPlotter.gibSkalierung() * dasScrollEvent.getDeltaX() / 50);
			derPlotter.addiereZuDeltaY(derPlotter.gibSkalierung() * -dasScrollEvent.getDeltaY() / 50);
		} else {
			derPlotter.addiereZuSkalierung(-(dasScrollEvent.getDeltaY() * 3));
			loescheSTRGInfo();
		}
		zeichne();

	}
	/**
	 * Setzt {@link #dieFunktion}
	 * @param dieFunktion neue {@link Funktion}
	 */
	public void setzeFunktion(Funktion dieFunktion) {
		this.dieFunktion = dieFunktion;
		this.dieStammfunktion = dieFunktion.gibStammfunktion();
		this.dieErsteAbleitung = dieFunktion.gibAbleitung();
		this.dieZweiteAbleitung = dieErsteAbleitung.gibAbleitung();
		initialisiereLabels();
	}
	/**
	 * Setter f&uuml;r {@link #derHauptFensterController}
	 * @param dieSteuerung neue Instanz f&uuml;r {@link #derHauptFensterController}
	 */
	public void setzeSteuerung(Steuerung dieSteuerung) {
		this.dieSteuerung = dieSteuerung;
	
	}
	/**
	 * Getter f&uuml;r {@link #dasCanvas}
	 * @return {@link #dasCanvas}
	 */
	public Canvas gibCanvas() {
		return dasCanvas;
	}
	/**
	 * Gibt die H&ouml;he von {@link #dasFunktionsLabelPane}
	 * @return H&ouml;he von {@link #dasFunktionsLabelPane}
	 */
	public double gibTitledPaneHoehe() {
		return this.dasFunktionsLabelPane.getHeight();
	}
	/**
	 * Gibt die Breite von {@link #dasFunktionsLabelPane}
	 * @return Breite von {@link #dasFunktionsLabelPane}
	 */
	public TitledPane gibTitledPane() {
		return this.dasFunktionsLabelPane;
	}
	public void setzeFunktionEingabeAktiv(boolean aktiv) {
		this.dasFunktionsTextField.setEditable(aktiv);
	}

}