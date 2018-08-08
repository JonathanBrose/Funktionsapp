package application;

import java.util.ArrayList;
import static functions.Utilities.parseDouble;
import static functions.Utilities.checkDouble;
import functions.Function;
import functions.Interval;
import functions.Point;
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
 * Controller for the CurveDiskussion tab.
 * 
 * @author Jonathan
 *
 */
public class CurveDiscussionController {

	@FXML
	private TextField textField1;
	@FXML
	private TextField textField2;
	@FXML
	private TitledPane zeroTitledPane;
	@FXML
	private TitledPane extremePointsTitledPane;
	@FXML
	private TitledPane turningPointsTitledPane;
	@FXML
	private TitledPane saddlePointsTitledPane;
	@FXML
	private TitledPane integralTitledPane;
	@FXML
	private TitledPane surfaceTitledPane;
	@FXML
	private TitledPane arcLengthTitledPane;
	@FXML
	private TitledPane meanTitledPane;
	@FXML
	private TitledPane rotationVolumeTitledPane;
	@FXML
	private Button selectionButton;
	@FXML
	private ProgressIndicator progressIndicator;
	@FXML
	private CheckBox showPointsCheckBox;
	@FXML
	private Button stopButton;
	@FXML
	private Button defaultButton;

	private Thread calculationThread;

	private Runnable runnable;

	private volatile boolean running;

	private Function function;

	private Interval interval;

	private ArrayList<Point> zeros;

	private ArrayList<Point> extremePoints;

	private ArrayList<Point> turningPoints;

	private ArrayList<Point> saddlePoints;

	private App mainApp;

	@FXML
	private void initialize() {
		zeros = new ArrayList<Point>();
		extremePoints = new ArrayList<Point>();
		turningPoints = new ArrayList<Point>();
		saddlePoints = new ArrayList<Point>();
		selectionButton.setTooltip(new Tooltip("Calculates the selected pane"));
		defaultButton.setTooltip(new Tooltip("Resets th interval to [-50:50]"));
		stopButton.setTooltip(new Tooltip("Stops all running calculations and deletes the results."));
		showPointsCheckBox.setTooltip(new Tooltip("Shows the calculated points in the overview"));
		showPointsCheckBox.selectedProperty().addListener((p, o, n) -> mainApp.refreshSize());
		resetInterval();

	}

	/**
	 * sets the interval and its textFields to -50;50
	 */
	@FXML
	private void resetInterval() {
		interval = new Interval(-50, 50);
		refreshIntervalDisplay();
	}

	/**
	 * Sets the textField textField1 and textField2 with the values from the
	 * interval.
	 */
	private void refreshIntervalDisplay() {
		textField1.setText(interval.getLeft() + "");
		textField2.setText(interval.getRight() + "");
	}

	/**
	 * Sets the function
	 * 
	 * @param function new function
	 * @see #reset()
	 */
	public void setFunction(Function function) {
		this.function = function;
		reset();
	}

	/**
	 * Gets called as {@link Accordion} gets clicked or changes. <br>
	 * Sets the text of the buttons and the boolean disable relying on the current
	 * state.
	 */
	@FXML
	public void checkAccordion() {
		if (zeroTitledPane.isExpanded()) {
			selectionButton.setText("calculate zeros");
			selectionButton.setDisable(false);
		} else if (extremePointsTitledPane.isExpanded()) {
			selectionButton.setText("calculate extrem points");
			selectionButton.setDisable(false);
		} else if (turningPointsTitledPane.isExpanded()) {
			selectionButton.setText("calculate turning points");
			selectionButton.setDisable(false);
		} else if (saddlePointsTitledPane.isExpanded()) {
			selectionButton.setText("calculate saddle points");
			selectionButton.setDisable(false);
		} else if (integralTitledPane.isExpanded()) {
			selectionButton.setText("calculate integral");
			selectionButton.setDisable(false);
		} else if (surfaceTitledPane.isExpanded()) {
			selectionButton.setText("calculate surface");
			selectionButton.setDisable(false);
		} else if (arcLengthTitledPane.isExpanded()) {
			selectionButton.setText("calculate arc length");
			selectionButton.setDisable(false);
		} else if (meanTitledPane.isExpanded()) {
			selectionButton.setText("calculate mean");
			selectionButton.setDisable(false);
		} else if (rotationVolumeTitledPane.isExpanded()) {
			selectionButton.setText("calculate rotation volume");
			selectionButton.setDisable(false);
		} else {
			selectionButton.setText("calculate selection");
			selectionButton.setDisable(true);
		}
	}

	/**
	 * sets the progress of the {@link #progressIndicator}.<br>
	 * 
	 * @param progress progress from 0 - 1.0
	 */
	private void refreshProgress(double progress) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				progressIndicator.setProgress(progress);
			}
		});
	}

	/**
	 * Stops all calculations, and resets the whole controller but with exclusion of
	 * {@link #function}
	 */
	@FXML
	public void reset() {
		stopCalculationThread();
		setText("", zeroTitledPane);
		setText("", extremePointsTitledPane);
		setText("", turningPointsTitledPane);
		setText("", saddlePointsTitledPane);
		setText("", integralTitledPane);
		setText("", surfaceTitledPane);
		setText("", arcLengthTitledPane);
		setText("", meanTitledPane);
		setText("", rotationVolumeTitledPane);
		resetInterval();
		this.zeros.clear();
		this.extremePoints.clear();
		this.turningPoints.clear();
		this.saddlePoints.clear();
	}

	/**
	 * Calculates everything
	 */
	@FXML
	public void calculateAll() {
		if (!running) {
			readInInterval();
			runnable = new Runnable() {

				@Override
				public void run() {
					setRunning(true);
					Runnable r = () -> mainApp.getOverviewController().setActiveButtons(false);
					Platform.runLater(r);
					refreshProgress(0.1);
					calcZeros(interval);
					refreshProgress(0.2);
					calcExtremePoints(interval);
					refreshProgress(0.3);
					calcTurningPoints(interval);
					refreshProgress(0.4);
					calcSaddlePoints(interval);
					refreshProgress(0.5);
					Runnable task2 = (() -> refreshPlot());
					Platform.runLater(task2);
					calculateIntegral(interval);
					if (checkInterrupted())
						return;
					refreshProgress(0.6);
					berechneFlaeche(interval);
					if (checkInterrupted())
						return;
					refreshProgress(0.7);
					calcRotationVolume(interval);
					if (checkInterrupted())
						return;
					refreshProgress(0.8);
					calcMean(interval);
					if (checkInterrupted())
						return;
					refreshProgress(0.9);
					calcArcLength(interval);
					if (checkInterrupted())
						return;
					refreshProgress(1);
					setRunning(false);
					r = () -> mainApp.getOverviewController().setActiveButtons(true);
					Platform.runLater(r);

				}
			};
			calculationThread = new Thread(runnable);
			calculationThread.start();
		}

	}

	/**
	 * Checks if the {@link #calculationThread} has been interrupted and resets some objects then.
	 * 
	 * @return return whether the {@link #calculationThread} was interrupted or not.
	 */
	private boolean checkInterrupted() {
		boolean interrupted = Thread.interrupted();
		if (interrupted) {
			setRunning(false);
			refreshProgress(0);
			Runnable r = () -> mainApp.getOverviewController().setActiveButtons(true);
			Platform.runLater(r);
		}
		return interrupted;
	}


	private void refreshPlot() {
		mainApp.refreshSize();
	}

	/**
	 * Stops the {@link #calculationThread}, or the be precise interrupts it
	 */
	public void stopCalculationThread() {
		if (this.calculationThread != null)
			this.calculationThread.interrupt();

	}

	/**
	 * Setter for {@link #running}
	 * 
	 * @param running indicates whether {@link #calculationThread} is active or not.
	 */
	private void setRunning(boolean running) {
		this.running = running;
		this.mainApp.getOverviewController().setInputActiv(!running);
		this.stopButton.setDisable(!running);
	}

	/**
	 * Calculates the selected information
	 */
	@FXML
	private void calculateSelection() {

		if (!running) {
			readInInterval();
			mainApp.getOverviewController().setActiveButtons(false);
			runnable = new Runnable() {

				@Override
				public void run() {
					setRunning(true);
					refreshProgress(0.1);
					if (checkInterrupted())
						return;
					if (zeroTitledPane.isExpanded())
						calcZeros(interval);
					else if (extremePointsTitledPane.isExpanded())
						calcExtremePoints(interval);
					else if (turningPointsTitledPane.isExpanded())
						calcTurningPoints(interval);
					else if (saddlePointsTitledPane.isExpanded())
						calcSaddlePoints(interval);
					else if (integralTitledPane.isExpanded())
						calculateIntegral(interval);
					else if (surfaceTitledPane.isExpanded())
						berechneFlaeche(interval);
					else if (arcLengthTitledPane.isExpanded())
						calcArcLength(interval);
					else if (meanTitledPane.isExpanded())
						calcMean(interval);
					else if (rotationVolumeTitledPane.isExpanded())
						calcRotationVolume(interval);

					setRunning(false);
					if (checkInterrupted())
						return;
					refreshProgress(1);
					Runnable r = () -> mainApp.getOverviewController().setActiveButtons(true);
					Platform.runLater(r);
				}
			};
			calculationThread = new Thread(runnable);
			calculationThread.start();
		}

	}

	/**
	 * Reads in {@link #textField1} and {@link #textField2} and creates a new {@link Interval} from that.
	 * 
	 */
	private void readInInterval() {
		this.interval = new Interval(parseDouble(textField1), parseDouble(textField2));
	}

	
	private void calcRotationVolume(Interval interval) {
		setText("calculating...", rotationVolumeTitledPane);
		Double rotationVolume = this.function.calculateRotationVolume(interval);
		String text = "roation volume in" + interval.toString() + ": " + checkDouble(rotationVolume);
		setText(text, rotationVolumeTitledPane);
	}

	
	private void calcZeros(Interval interval) {
		setText("calculating...", zeroTitledPane);
		ArrayList<Point> zeros = this.function.calculateZeros(interval);
		this.zeros.clear();
		this.zeros.addAll(zeros);
		String text = this.ArrayListToString(zeros);
		setText(text, zeroTitledPane);
	}

	
	private void calcExtremePoints(Interval interval) {
		setText("calculating...",  extremePointsTitledPane);
		ArrayList<Point> extremePoints = this.function.calculateExtremePoints(interval);
		this.extremePoints.clear();
		this.extremePoints.addAll(extremePoints);
		String text = this.ArrayListToString(extremePoints);
		setText(text, extremePointsTitledPane);
	}

	
	private void calcTurningPoints(Interval interval) {
		setText("calculating...",  turningPointsTitledPane);
		ArrayList<Point> turningPoints = this.function.calculateTurningPoints(interval);
		this.turningPoints.clear();
		this.turningPoints.addAll(turningPoints);
		String text = this.ArrayListToString(turningPoints);
		setText(text, turningPointsTitledPane);
	}

	
	private void calcSaddlePoints(Interval interval) {
		setText("calculating...",  saddlePointsTitledPane);
		ArrayList<Point> saddlePoints = this.function.calculateSaddlePoints(interval);
		this.saddlePoints.clear();
		this.saddlePoints.addAll(saddlePoints);
		String text = this.ArrayListToString(saddlePoints);
		setText(text, saddlePointsTitledPane);
	}

	
	public void calculateIntegral(Interval interval) {
		setText("calculating...",  integralTitledPane);
		double integral = this.function.calculateIntegral(interval);
		setText("integral in " + interval.toString() + ": " + checkDouble(integral), integralTitledPane);
	}

	
	public void berechneFlaeche(Interval interval) {
		setText("calculating...",  surfaceTitledPane);
		double surface = this.function.calculateSurface(interval);
		setText("surface in " + interval.toString() + ": " + checkDouble(surface), surfaceTitledPane);
	}

	public void calcArcLength(Interval interval) {
		setText("calculating...",  arcLengthTitledPane);
		double arcLength = this.function.calculateArcLength(interval);
		setText("arc length in " + interval.toString() + ": " + checkDouble(arcLength),
				arcLengthTitledPane);
	}

	
	public void calcMean(Interval interval) {
		setText("calculating...",  meanTitledPane);
		double mean = this.function.calculateIntegralMean(interval);
		setText("Mittelwert in " + interval.toString() + ": " + checkDouble(mean), meanTitledPane);
	}

	
	private String ArrayListToString(ArrayList<Point> points) {

		if (points.isEmpty())
			return "nothing found";

		String string = "";

		for (Point point : points) {
			string += point.toString() + System.lineSeparator();
		}

		return string;
	}

	/**
	 *Sets the text to the param titledPane<br>
	 * 
	 * @param string     new Text.
	 * @param titledPane titledPane to be edited.
	 */
	private void setText(String string, TitledPane titledPane) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				ScrollPane scrollPane = (ScrollPane) titledPane.getContent();
				TextFlow textFlow = (TextFlow) scrollPane.getContent();
				Text text = new Text(string);
				text.setFont(new Font(16));
				textFlow.getChildren().clear();
				textFlow.getChildren().add(text);

			}
		});

	}

	
	public ArrayList<Point> getZeros() {
		return zeros;
	}
	
	public ArrayList<Point> getExtremePoints() {
		return extremePoints;
	}

	public ArrayList<Point> getTurningPoints() {
		return turningPoints;
	}

	public ArrayList<Point> getSaddlePoints() {
		return saddlePoints;
	}

	public boolean pointsShown() {
		return showPointsCheckBox.isSelected();
	}

	public void setMainApp(App mainApp) {
		this.mainApp = mainApp;
	}
	public boolean isRunning() {
		return this.running;
	}

}
