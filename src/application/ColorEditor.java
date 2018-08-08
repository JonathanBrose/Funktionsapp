package application;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class ColorEditor extends BorderPane{
	ColorWrapper colorWrapper;
	Color originalColor;
	Label label;
	Button button;
	ColorPicker colorPicker;
	Runnable runnable;
	
	public ColorEditor(ColorWrapper color, String text, Runnable r) {
		runnable = r;
		this.colorWrapper = color;
		this.originalColor = color.getColor();
		colorPicker = new ColorPicker(color.getColor());
		colorPicker.setOnAction((ActionEvent e)->colorSelected());
		label = new Label();
		label.setAlignment(Pos.CENTER_LEFT);
		label.setPadding(new Insets(5, 0, 5, 10));
		label.setText(text);
		button = new Button("Reset");
		button.setOnAction((e)-> buttonAction());
		setCenter(label);
		setRight(button);
		setLeft(colorPicker);
	}
	private void buttonAction() {
		colorWrapper.setColor(originalColor);
		colorPicker.setValue(originalColor);
		colorSelected();
	}
	private void colorSelected() {
		colorWrapper.setColor(colorPicker.getValue());
		Platform.runLater(runnable);
	}
	public Color getColor() {
		return colorWrapper.getColor();
	}
}
