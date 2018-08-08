package application;

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
 * Controller for preferences.fxml
 * @author Jonathan Brose
 *
 */
public class PreferencesController {
	
	@FXML 
	private VBox vBox;
	
	private App mainApp;
	
	private Runnable refreshColor;
	
	private SimpleBooleanProperty touchPadScroll;

	@FXML
	private void initialize() {
		refreshColor = () ->{
			mainApp.refreshColor();
		};
		
	}
	public void setColors(ColorWrapper sf, ColorWrapper f, ColorWrapper ab, ColorWrapper ab2, 
			ColorWrapper p, SimpleBooleanProperty touchPadScroll) {
		this.touchPadScroll = touchPadScroll;
		ObservableList<Node> vBoxItems = vBox.getChildren();
		Font font = new  Font(18);
		Label label =new Label("Farben");
		label.setFont(font);
		label.setUnderline(true);
		vBoxItems.add(label);
		vBoxItems.add(new Separator(Orientation.HORIZONTAL));
		vBoxItems.add(new ColorEditor(sf, "antiderivative color", refreshColor));
		vBoxItems.add(new Separator(Orientation.HORIZONTAL));
		vBoxItems.add(new ColorEditor(f, "function color", refreshColor));
		vBoxItems.add(new Separator(Orientation.HORIZONTAL));
		vBoxItems.add(new ColorEditor(ab, "first derivative color", refreshColor));
		vBoxItems.add(new Separator(Orientation.HORIZONTAL));
		vBoxItems.add(new ColorEditor(ab2, "second derivative color", refreshColor));
		vBoxItems.add(new Separator(Orientation.HORIZONTAL));
		vBoxItems.add(new ColorEditor(p, "special points color", refreshColor));
		vBoxItems.add(new Separator(Orientation.HORIZONTAL));
		vBoxItems.add(createTouchPadScroll());
		vBoxItems.add(new Separator(Orientation.HORIZONTAL));
	}
	private BorderPane createTouchPadScroll() {
		BorderPane borderPane = new BorderPane();
		borderPane.setCenter(new Label("defines the scroll mode"));
		CheckBox checkBox = new CheckBox("touchpad");
		checkBox.setSelected(touchPadScroll.get());
		checkBox.setMinWidth(80);
		borderPane.setLeft(checkBox);
		borderPane.setPadding(new Insets(5, 0, 5, 0));
		checkBox.setOnAction((e)->{
			touchPadScroll.set(checkBox.selectedProperty().get());
			if(checkBox.selectedProperty().get()) {
				checkBox.setText("touchpad");
			}else
				checkBox.setText("mouse");
		});
		return borderPane;
	}
	public void setMainApp(App mainApp) {
		this.mainApp = mainApp;
	}
}
