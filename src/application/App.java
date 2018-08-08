package application;

import static stringConverter.FunctionBuilder.stringToFunction;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.controlsfx.control.Notifications;

import functions.Function;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Creates the Application
 * 
 * @author Jonathan Brose
 *
 *
 */
public class App extends Application {
	
	private Stage primaryStage;
	private BorderPane rootLayout;
	private Function function;
	private BorderPane cDPane;
	private BorderPane oVPane;
	private OverviewController overviewController;
	private CurveDiscussionController curveDiscussionController;
	private PreferencesController preferencesController;
	private BorderPane preferencesPane;
	private File lastFilePath;


	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Function Application");
		this.primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("iconPi.jpg")));
		initCurveDiscussion();
		initOverview();
		initPreferences();
		initMainWindow();
		this.setFunction(new Function());
		
	}

	/**
	 *Loads MainWindow.fxml and initializes its controller 
	 * {@link MainWindowController}.
	 */
	private void initMainWindow() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(App.class.getResource("MainWindow.fxml"));
			this.rootLayout = (BorderPane) loader.load();

			MainWindowController mainWindowController = loader.getController();
			mainWindowController.setMainApp(this);
			mainWindowController.setCurveDiscussion(cDPane);
			mainWindowController.setOverview(oVPane);
			mainWindowController.setPreferences(preferencesPane);
			Scene scene = new Scene(this.rootLayout);
			scene.addEventFilter(MouseEvent.DRAG_DETECTED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					scene.startFullDrag();
				}
			});
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.widthProperty().addListener((p, oW, nW) -> refreshSize());
			primaryStage.heightProperty().addListener((p, oH, nH) -> refreshSize());
			primaryStage.maximizedProperty().addListener((b, o, n) -> refreshSize());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads Overview.fxml and initializes its controller.
	 */
	private void initOverview() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(App.class.getResource("Overview.fxml"));
			oVPane = (BorderPane) loader.load();
			overviewController = loader.getController();
			overviewController.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Loads Preferences.fxml and initializes its controller.
	 */
	private void initPreferences() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(App.class.getResource("Preferences.fxml"));
			preferencesPane = (BorderPane) loader.load();
			preferencesController = loader.getController();
			preferencesController.setMainApp(this);
			overviewController.loadColors(preferencesController);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void refreshColor() {
		overviewController.refreshCss();
		overviewController.draw();
	}

	/**
	 * Loads CurveDiscussion.fxml and initializes its controller.
	 */
	private void initCurveDiscussion() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(App.class.getResource("CurveDiscussion.fxml"));
			cDPane = (BorderPane) loader.load();
			curveDiscussionController = loader.getController();
			curveDiscussionController.setMainApp(this);
			curveDiscussionController.setFunction(function);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * creates a new Function from the given function term using 
	 * the stringToFunction method.
	 * @param funktionsterm the function term
	 */
	public void erstelleFunktion(String funktionsterm) {
		Function f = stringToFunction(funktionsterm);
		if(f != null)
			this.setFunction(f);
		else
			functions.Utilities.showDialog("Input not valid!","Check your input.");
	}

	public void setFunction(Function function) {
		this.function = function;
		this.overviewController.setFunktion(function);
		this.overviewController.draw();
		this.curveDiscussionController.stopCalculationThread();
		this.curveDiscussionController.setFunction(function);
		if (!curveDiscussionController.isRunning())
			this.overviewController.checkMainFunctionButtons();
	}

	/**
	 * Refreshes the size of the plot canvas in 
	 * {@link #overviewController}.
	 * 
	 */
	public void refreshSize() {
		overviewController.recalculateCanvas();;
	}

	/**
	 * Opens a File choose dialog and enables the user to save the {@link Function} to a ".func" file.
	 */
	public void save() {
		String filePath;

		filePath = openFileSaveDialog();
		if (filePath == null) {
			return;
		}
		this.function.save(filePath);
		showNotification("File sucessfully saved", filePath);
	}

	/**
	 * Opens a File choose dialog and enables the user to load a {@link Function} from a ".func" file.
	 */
	public void load() {
		String fileInput;
		fileInput = openLoadDialog();
		if (fileInput == null) {
			return;
		}
		Function function = Function.load(fileInput);
		if (function == null) {
			showNotification("Function couldn´t be loaded",
					"File may be corrupted, or it is not existing");
			return;
		}
		showNotification("Function sucessfully loaded", function.toString());
		this.setFunction(function);
	

	}

	/**
	 * deletes the function
	 */
	void deleteFunction() {
		showNotification("Function has been deleted", function.toString());
		stopCalculations();
		this.setFunction(new Function());
	}

	/**
	 * Shows a notification at the right bottom of the screen
	 * 
	 * @param title
	 *            title
	 * @param message
	 *            message
	 */
	private void showNotification(String title, String message) {

		Notifications notificationBuilder = Notifications.create().title(title).text(message)
				.hideAfter(new Duration(6000)).position(Pos.BOTTOM_RIGHT);
		notificationBuilder.darkStyle();
		notificationBuilder.show();

	}

	/**
	 * Open a dialog to load a .func file.
	 * 
	 * @return the file path
	 */
	public String openLoadDialog() {

		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Function file", "*.func"));
		if (lastFilePath == null) {

			fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		} else {
			fileChooser.setInitialDirectory(lastFilePath);
		}
		File file = fileChooser.showOpenDialog(null);
		if (file != null) {
			if (lastFilePath == null)
				lastFilePath = file.getParentFile();
			return file.getAbsolutePath();
		}
		return null;

	}

	/**
	 * Opens a dialog to save a .func file
	 * 
	 * @return the file path
	 */
	public String openFileSaveDialog() {

		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Function file", "*.func"));
		if (lastFilePath == null) {

			fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		} else {
			fileChooser.setInitialDirectory(lastFilePath);
		}
		File file = fileChooser.showSaveDialog(null);

		if (file != null) {
			lastFilePath = file.getParentFile();
			return file.getAbsolutePath();
		}

		return null;
	}

	/**
	 * Stops running calculations in {@link #curveDiscussionController}
	 * 
	 * @see application.CurveDiscussionController#stopCalculationThread()
	 */
	public void stopCalculations() {
		this.curveDiscussionController.stopCalculationThread();
	}


	public void resetCurveDiscussion() {
		curveDiscussionController.reset();
	}

	public OverviewController getOverviewController() {
		return this.overviewController;
	}
	
	public double getWidth() {
		return this.getPrimaryStage().getWidth();
	}

	public double getHeight() {
		return this.getPrimaryStage().getHeight();
	}

	public CurveDiscussionController getCurveDiscussionController() {
		return this.curveDiscussionController;
	}

	public Function getFunction() {
		return function;
	}

	@Override
	public void stop() {
		System.exit(0);
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}

	public BorderPane getOverviewPane() {
		return this.oVPane;
	}

}
