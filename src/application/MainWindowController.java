package application;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
/**
 * Controls the MainWindow, which contains an {@link OverviewController} and a{@link CurveDiscussionController} 
 * @author Jonathan Brose
 *
 */
public class MainWindowController {

	@FXML
	private TabPane tabPane;
	@FXML
	private MenuItem saveMenuItem;
	@FXML
	private MenuItem loadMenuItem;
	@FXML
	private MenuItem deleteMenuItem;
	
	private ObservableList<Tab> tabs;
	
	private App mainApp;
	
	@FXML
	private void initialize() {
		tabs = tabPane.getTabs();
		deleteMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN));
		saveMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
		loadMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
	}
	
	@FXML
	private void checkAccordion() {
		mainApp.getCurveDiscussionController().checkAccordion();
	}
	@FXML
	private void load() {
		mainApp.load();
	}
	@FXML
	private void save() {
		mainApp.save();
	}
	@FXML
	private void delete() {
		mainApp.deleteFunction();
	}
	
	
	public void setMainApp(App mainApp) {
		this.mainApp = mainApp;
	}
	public void setCurveDiscussion(BorderPane cDPane) {
		this.tabs.get(1).setContent(cDPane);
	}
	public void setOverview(BorderPane oVPane) {
		this.tabs.get(0).setContent(oVPane);
	}
	public void setPreferences(BorderPane prefPane) {
		this.tabs.get(2).setContent(prefPane);
	}

}
