package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import functions.Function;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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

public class OverviewController {

	@FXML
	private TextField antiderivativeTextField;
	@FXML
	private TextField functionTextField;
	@FXML
	private TextField derivativeTextField;
	@FXML
	private TextField derivate2TextField;
	@FXML
	private Canvas canvas;
	@FXML
	private TitledPane titledPane;
	@FXML
	private RadioButton antiderivativeRadioButton;
	@FXML
	private RadioButton functionRadioButton;
	@FXML
	private RadioButton derivativeRadioButton;
	@FXML
	private RadioButton derivative2RadioButton;
	@FXML
	private Button mainFunctionButton1;
	@FXML
	private Button mainFunctionButton3;
	@FXML
	private Button mainFunctionButton4;
	@FXML
	private ScrollPane scrollPane;
	@FXML
	private GridPane gridPane;
	
	private double lastMouseXPosition = 0, lastMouseYPosition = 0;
	
	private Plotter plotter;
	
	private Function antiderivative, function, derivative, secondDerivative;

	private App mainApp;
	private SimpleBooleanProperty touchPadScroll;
	
	private ColorWrapper antiderivativeColor, functionColor, derivativeColor, secondDerivativeColor,
			pointColor;
	@FXML
	public void initialize() {

		touchPadScroll = new SimpleBooleanProperty(true);
		plotter = new Plotter(canvas);
		titledPane.setExpanded(true);
		titledPane.expandedProperty()
				.addListener((obs, oldB, newB) -> this.resetCanvasSize(oldB, newB));
		titledPane.heightProperty().addListener((obs, oldB, newB) -> this.recalculateCanvas());
		canvas.setOnScroll(this::canvasScroll);
		canvas.setOnMouseDragOver(this::canvasMove);
		canvas.setOnMouseDragEntered(this::canvasStartMove);
		canvas.setOnZoom(this::canvasZoom);
		initColors();
		refreshCss();
		draw();
		initTooltips();
		checkMainFunctionButtons();
		setTextFieldListener();
		deleteSTRGInfo();
	}

	private void initColors() {
		antiderivativeColor = new ColorWrapper(Color.DARKBLUE);
		functionColor = new ColorWrapper(Color.ORANGERED);
		derivativeColor = new ColorWrapper(Color.GREEN);
		secondDerivativeColor = new ColorWrapper(Color.BLUEVIOLET);
		pointColor = new ColorWrapper(Color.RED);
	}

	public void refreshCss() {
		String style = ".radio-button .radio {\r\n" + "    -fx-border-width: 1px;\r\n"
				+ "    -fx-border-color: #000;\r\n" + "    -fx-background-color: lightgray;\r\n"
				+ "    -fx-background-image: null;\r\n" + "    -fx-border-radius: 9px;\r\n"
				+ "    -fx-padding: 0px;\r\n" + "    \r\n" + "}\r\n" + ".radio-button:selected .dot{\r\n"
				+ "	 -fx-background-insets: 0px;\r\n" + "}\r\n" + ".radio-button .dot {\r\n"
				+ "    -fx-background-radius: 12px;\r\n" + "    -fx-padding: 9px;\r\n" + "}";

		style += ".stammfunktion:selected  .dot {" + " -fx-background-color: #" + toHexColor(antiderivativeColor)
				+ ";" + "}\n";

		style += ".funktion:selected  .dot {" + " -fx-background-color: #" + toHexColor(functionColor) + ";"
				+ "}\n";

		style += ".ableitung1:selected  .dot {" + "-fx-background-color: #" + toHexColor(derivativeColor) + ";"
				+ "}\n";

		style += ".ableitung2:selected  .dot {" + "-fx-background-color: #" + toHexColor(secondDerivativeColor) + ";"
				+ "}";

		File radioButtonFile = new File(".radioButtons.css");
		try {
			FileWriter fileWriter = new FileWriter(radioButtonFile);
			fileWriter.write(style);
			fileWriter.close();
			gridPane.getStylesheets().clear();
			gridPane.getStylesheets()
					.add("file:/" + radioButtonFile.getAbsolutePath().replace("\\", "/"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String toHexColor(ColorWrapper color) {
		String s = color.toString();
		s = s.substring(2, s.length());
		return s.substring(0, s.length() - 2);
	}

	private void setTextFieldListener() {
		activateAutomaticGrow(functionTextField);
		activateAutomaticGrow(derivativeTextField);
		activateAutomaticGrow(derivate2TextField);
		activateAutomaticGrow(antiderivativeTextField);
	}

	private void activateAutomaticGrow(TextField dasTextField) {
		dasTextField.textProperty().addListener((ObservableValue<? extends String> arg0, String arg1, String arg2) -> {
			dasTextField.setPrefWidth(dasTextField.getText().length() * 7);

		});
	}

	private void deleteSTRGInfo() {
		Runnable r1 = () -> {
			BorderPane borderPane = (BorderPane) canvas.getParent();
			borderPane.setTop(null);
			recalculateCanvas();
		};
		Platform.runLater(r1);
	}

	
	private double getSTRGHeight() {
		BorderPane borderPane = (BorderPane) canvas.getParent();
		if (borderPane.getTop() == null)
			return 0;

		Label infoLabel = (Label) borderPane.getTop();
		return infoLabel.getHeight();
	}

	private void initTooltips() {
		Tooltip tooltip = new Tooltip();
		tooltip.setText("Overides the main function with the function left from this button");
		mainFunctionButton1.setTooltip(tooltip);
		mainFunctionButton3.setTooltip(tooltip);
		mainFunctionButton4.setTooltip(tooltip);
		Tooltip tooltip1 = new Tooltip("The antiderivative");
		antiderivativeTextField.setTooltip(tooltip1);
		Tooltip tooltip2 = new Tooltip("Insert the function here and press enter");
		functionTextField.setTooltip(tooltip2);
		Tooltip tooltip3 = new Tooltip("The first derivative");
		derivativeTextField.setTooltip(tooltip3);
		Tooltip tooltip4 = new Tooltip("The second derivative");
		derivate2TextField.setTooltip(tooltip4);
	}

	
	public void initLabels() {
		if (function != null) {
			String errorText = "antiderivate couldn�t get determined";
			antiderivativeTextField.setText(antiderivative == null ? errorText : antiderivative.toString());
			functionTextField.setText(function.toString());
			derivativeTextField.setText(derivative.toString());
			derivate2TextField.setText(secondDerivative.toString());
		} else {
			antiderivativeTextField.setText("F(x)=");
			functionTextField.setText("f(x)=");
			derivativeTextField.setText("f'(x)=");
			derivate2TextField.setText("f''(x)=");
		}

	}

	/**
	 * Makes sure the size of the canvas fits to the window containing it.
	 */
	public void recalculateCanvas() {
		canvas.setHeight(0);
		canvas.setWidth(0);
		Runnable r = () -> {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Runnable r2 = () -> {
				BorderPane borderPane = ((BorderPane) canvas.getParent());
				canvas.setWidth(borderPane.getWidth());
				canvas.setHeight(borderPane.getHeight() - titledPane.getHeight() - getSTRGHeight());
				draw();
			};
			Platform.runLater(r2);
		};
		new Thread(r).start();

	}

	/**
	 * Draws the coordinate system, the function and so on...
	 */
	public void draw() {
		if (canvas.getWidth() == 0 || canvas.getHeight() == 0)
			return;
		plotter.deleteImage();
		plotter.plotGrid();
		plotter.plotAxes();
		if (antiderivativeRadioButton.isSelected())
			plotter.plotFunction(antiderivative, antiderivativeColor.getColor());
		if (functionRadioButton.isSelected()) {
			plotter.plotFunction(function, functionColor.getColor());
			if (mainApp != null)
				plotter.plotSpecialPoints(mainApp.getCurveDiscussionController(),
						pointColor.getColor());
		}
		if (derivativeRadioButton.isSelected())
			plotter.plotFunction(derivative, derivativeColor.getColor());
		if (derivative2RadioButton.isSelected())
			plotter.plotFunction(secondDerivative, secondDerivativeColor.getColor());
	}

	
	public void resetCanvasSize(boolean oldValue, boolean newValue) {
		if (!oldValue && newValue) {
			canvas.setHeight(0);
			canvas.setWidth(0);
		}
	}

	@FXML
	private void onTextFieldClick() {
		if (!functionTextField.isEditable()) {
			String message = "Function can�t be edited, becaus calculations are running in the curve discussion tab. Do you want to stop them?";
			Alert alert = new Alert(AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.CANCEL);
			alert.setTitle("Function can�t be edited");
			alert.setHeaderText(null);
			Optional<ButtonType> buttonType = alert.showAndWait();
			if (buttonType.isPresent() && buttonType.get().equals(ButtonType.YES)) {
				mainApp.stopCalculations();
			}
		}
	}

	@FXML
	private void generateFunction(KeyEvent keyEvent) {
		if (keyEvent.getCode() == KeyCode.ENTER) {
			mainApp.erstelleFunktion(functionTextField.getText());
			functionTextField.end();
		}
	}

	/**
	 * Checks which button should be active, and which should not.
	 */
	public void checkMainFunctionButtons() {
		if (this.function == null) {
			mainFunctionButton1.setDisable(true);
			mainFunctionButton3.setDisable(true);
			mainFunctionButton4.setDisable(true);
		} else if (this.function.isXAxis()) {
			mainFunctionButton1.setDisable(true);
			mainFunctionButton3.setDisable(true);
			mainFunctionButton4.setDisable(true);
		} else if (this.antiderivative == null) {
			antiderivativeRadioButton.setDisable(true);
			antiderivativeRadioButton.setSelected(false);
			mainFunctionButton1.setDisable(true);
			mainFunctionButton3.setDisable(false);
			mainFunctionButton4.setDisable(false);
		} else {
			antiderivativeRadioButton.setDisable(false);
			mainFunctionButton1.setDisable(false);
			mainFunctionButton3.setDisable(false);
			mainFunctionButton4.setDisable(false);
		}

	}

	/**
	 * Checks which button should be active, and which should not.
	 * @param activ indicates the active state
	 */
	public void setActiveButtons(boolean activ) {
		mainFunctionButton1.setDisable(!activ);
		mainFunctionButton3.setDisable(!activ);
		mainFunctionButton4.setDisable(!activ);
		checkMainFunctionButtons();
	}

	@FXML
	public void saveAsMain1() {
		saveAsMain(1);
	}

	@FXML
	public void saveAsMain3() {
		saveAsMain(3);
	}

	@FXML
	public void saveAsMain4() {
		saveAsMain(4);
	}

	/**
	 * saves the with the index selected function as main function
	 * 
	 * @param index index of the function
	 */
	public void saveAsMain(int index) {
		Function function;
		switch (index) {
		case 1:
			function = mainApp.getFunction().getAntiderivative();
			if (function == null)
				return;
			break;
		case 3:
			function = mainApp.getFunction().getDerivative();
			break;
		case 4:
			function = mainApp.getFunction().getDerivative(2);
			break;
		default:
			function = mainApp.getFunction();
		}
		mainApp.stopCalculations();
		mainApp.setFunction(function);

	}

	/**
	 * scales the image of canvas
	 * 
	 * @param zoomEvent the zoom event
	 */
	private void canvasZoom(ZoomEvent zoomEvent) {
		plotter.setScale(plotter.getScale() / zoomEvent.getZoomFactor());
	}

	/**
	 * moves the image of the canvas.
	 * 
	 * @param mouseEvent the mouse Event
	 */
	private void canvasStartMove(MouseDragEvent mouseEvent) {
		if (mouseEvent.isSynthesized())
			return;
		if (mouseEvent.getButton() != MouseButton.PRIMARY)
			return;
		lastMouseXPosition = mouseEvent.getX();
		lastMouseYPosition = mouseEvent.getY();

	}

	/**
	 * moves the image of the canvas.
	 * 
	 * @param mouseEvent the mouse Event
	 */
	private void canvasMove(MouseDragEvent mouseEvent) {
		if (mouseEvent.isSynthesized())
			return;
		if (mouseEvent.getButton() != MouseButton.PRIMARY)
			return;

		double newX = mouseEvent.getX(), newY = mouseEvent.getY();

		plotter.addToDX(((newX - lastMouseXPosition) / 60) * plotter.getScale());
		plotter.addToDY(((lastMouseYPosition - newY) / 60) * plotter.getScale());
		lastMouseXPosition = newX;
		lastMouseYPosition = newY;
		draw();
	}

	
	private void canvasScroll(ScrollEvent scrollEvent) {

		if (scrollEvent.isDirect() || !scrollEvent.isControlDown() && touchPadScroll.get()) {
			plotter.addToDX(plotter.getScale() * scrollEvent.getDeltaX() / 50);
			plotter.addToDY(plotter.getScale() * -scrollEvent.getDeltaY() / 50);
		} else if (scrollEvent.isControlDown() || !touchPadScroll.get()) {

			plotter.addToScale(-(scrollEvent.getDeltaY() * 3));
			deleteSTRGInfo();
		}
		draw();

	}

	
	public void setFunktion(Function function) {
		this.function = function;
		this.antiderivative = function.getAntiderivative();
		this.derivative = function.getDerivative();
		this.secondDerivative = derivative.getDerivative();
		initLabels();
	}

	public void setMainApp(App mainApp) {
		this.mainApp = mainApp;

	}
	public Canvas getCanvas() {
		return canvas;
	}

	public double getTitledPaneHeight() {
		return this.titledPane.getHeight();
	}

	public TitledPane getTitledPane() {
		return this.titledPane;
	}

	public void setInputActiv(boolean activ) {
		this.functionTextField.setEditable(activ);
	}

	public void loadColors(PreferencesController preferencesController) {
		preferencesController.setColors(antiderivativeColor, functionColor, derivativeColor,
				secondDerivativeColor, pointColor, touchPadScroll);
	}
}
