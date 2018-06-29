package applikation;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class FarbenItem extends BorderPane{
	ColorWrapper dieFarbe;
	Color dieOriginalFarbe;
	Label dasLabel;
	Button derButton;
	ColorPicker derColorPicker;
	Runnable dieRunnable;
	
	public FarbenItem(ColorWrapper dieFarbe, String text, Runnable r) {
		dieRunnable = r;
		this.dieFarbe = dieFarbe;
		this.dieOriginalFarbe = dieFarbe.gibFarbe();
		derColorPicker = new ColorPicker(dieFarbe.gibFarbe());
		derColorPicker.setOnAction((ActionEvent e)->farbeAusgewaehlt());
		dasLabel = new Label();
		dasLabel.setAlignment(Pos.CENTER_LEFT);
		dasLabel.setPadding(new Insets(5, 0, 5, 10));
		dasLabel.setText(text);
		derButton = new Button("Zurücksetzen");
		derButton.setOnAction((e)-> buttonAktion());
		setCenter(dasLabel);
		setRight(derButton);
		setLeft(derColorPicker);
	}
	private void buttonAktion() {
		dieFarbe.setzeFarbe(dieOriginalFarbe);
		derColorPicker.setValue(dieOriginalFarbe);
		farbeAusgewaehlt();
	}
	private void farbeAusgewaehlt() {
		dieFarbe.setzeFarbe(derColorPicker.getValue());
		Platform.runLater(dieRunnable);
	}
	public Color gibFarbe() {
		return dieFarbe.gibFarbe();
	}
}
