package applikation;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


/**
 * Controller f&uuml;r Einstellungen.fxml<br>
 * Verwaltet diesen Tab.
 * @author Jonathan Brose
 *
 */
public class EinstellungenController {
	
	@FXML 
	private VBox dieFarbenVBox;
	/**
	 * Referenz zur Steuerung der App.
	 */
	private Steuerung dieSteuerung;
	/**
	 * {@link Runnable}, die ausgeführt wird wenn sich ein Farbe ändert.
	 */
	private Runnable aktualisiereFarbe;
	/**
	 *
	*/
	private SimpleBooleanProperty touchPadScroll;

	@FXML
	private void initialize() {
		aktualisiereFarbe = () ->{
			dieSteuerung.aktuallisiereFarbe();
		};
		
	}
	public void setzeFarben(ColorWrapper sf, ColorWrapper f, ColorWrapper ab, ColorWrapper ab2, 
			ColorWrapper p, SimpleBooleanProperty touchPadScroll) {
		this.touchPadScroll = touchPadScroll;
		ObservableList<Node> dieVBoxItems = dieFarbenVBox.getChildren();
		Font dieSchrift = new  Font(18);
		Label dieUeberschrift =new Label("Farben");
		dieUeberschrift.setFont(dieSchrift);
		dieUeberschrift.setUnderline(true);
		dieVBoxItems.add(dieUeberschrift);
		dieVBoxItems.add(new Separator(Orientation.HORIZONTAL));
		dieVBoxItems.add(new FarbenItem(sf, "Farbe der Stammfunktion", aktualisiereFarbe));
		dieVBoxItems.add(new Separator(Orientation.HORIZONTAL));
		dieVBoxItems.add(new FarbenItem(f, "Farbe der Funktion", aktualisiereFarbe));
		dieVBoxItems.add(new Separator(Orientation.HORIZONTAL));
		dieVBoxItems.add(new FarbenItem(ab, "Farbe der ersten Ableitung", aktualisiereFarbe));
		dieVBoxItems.add(new Separator(Orientation.HORIZONTAL));
		dieVBoxItems.add(new FarbenItem(ab2, "Farbe der zweiten Ableitung", aktualisiereFarbe));
		dieVBoxItems.add(new Separator(Orientation.HORIZONTAL));
		dieVBoxItems.add(new FarbenItem(p, "Farbe der besonderen Punkte", aktualisiereFarbe));
		dieVBoxItems.add(new Separator(Orientation.HORIZONTAL));
		dieVBoxItems.add(erstelleTouchPadScroll());
		dieVBoxItems.add(new Separator(Orientation.HORIZONTAL));
	}
	private BorderPane erstelleTouchPadScroll() {
		BorderPane dasBorderPane = new BorderPane();
		dasBorderPane.setCenter(new Label("bestimmt den Scrollmodus"));
		CheckBox dieCheckBox = new CheckBox("Touchpad");
		dieCheckBox.setSelected(touchPadScroll.get());
		dieCheckBox.setMinWidth(80);
		dasBorderPane.setLeft(dieCheckBox);
		dasBorderPane.setPadding(new Insets(5, 0, 5, 0));
		dieCheckBox.setOnAction((e)->{
			touchPadScroll.set(dieCheckBox.selectedProperty().get());
			if(dieCheckBox.selectedProperty().get()) {
				dieCheckBox.setText("Touchpad");
			}else
				dieCheckBox.setText("Maus");
		});
		return dasBorderPane;
	}
	public void setzeSteuerung(Steuerung steuerung) {
		this.dieSteuerung = steuerung;
	}
}
