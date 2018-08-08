package functions;
 
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.google.common.math.DoubleMath;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

/**
 * Static class, with some helper methods used in a lot of other classes in the functions package
 * @author Jonathan Brose
 */
public class Utilities {
	
	private Utilities() {
		
	}
	
	/**
	 * 
	 * Checks whether the param d is a  int factor of PI,  for example: returns 2pi instead 6,2...
	 * 
	 * @param d
	 *           double to be checked and converted to String
	 * @return converted value 
	 */

	public static String checkDouble(double d) {
		double piFactor = d / Math.PI;
		piFactor = round(piFactor, 4);

		if (DoubleMath.fuzzyEquals(piFactor, (int) piFactor, 0.001)) {
			if (piFactor == 0)
				return "0";

			return (piFactor == 1 ? "" : (int) piFactor + "") + "\u03C0";
		} else {
			piFactor = d / Math.E;
			piFactor = round(piFactor, 4);
			if (DoubleMath.fuzzyEquals(piFactor, (int) piFactor, 0.001)) {
				if (piFactor == 0)
					return "0";
				return (piFactor == 1 ? "" : (int) piFactor + "") + "e";
			}
		}
		return d + "";
	}
	/**
	 * 
	 * Reads a double out of a {@link TextField}.  "," becomes ".", so 1,2 can be a valid input. 
	 * Fractions are getting calculated and stored in the output.
	 * 
	 * @param textField input TextField
	 * @return the parsed double.
	 * @throws NumberFormatException if the String is not valid for {@link #parseDouble(String)}.
	 */

	public static double parseDouble(TextField textField) throws NumberFormatException {
		String text = textField.getText();
		text = text.replaceAll(",", ".");
		if (text.contains("/")) {
			String[] dieBruchteile = text.split("/");
			double a = parseDouble(dieBruchteile[0]);
			for (int i = 1; i < dieBruchteile.length; i++) {
				a /= parseDouble(dieBruchteile[i]);
			}
			return a;
		}
		return parseDouble(text);
	}
	/**
	 * Parses a String to a double. "Pi" or "e" will be replaced by the matching values from the Math class.
	 * 
	 * @param text to be parsed.
	 * @return parsed double
	 * @throws NumberFormatException if the String is not valid for {@link #parseDouble(String)}.
	 */
	public static double parseDouble(String text) throws NumberFormatException {
		double d = 0;
		if (text.toLowerCase().contains("pi")) {
			text = text.toLowerCase().replace("pi", "");
			d = Math.PI;
			if (text.length() > 0) {
				d *= Double.parseDouble(text);
			}

		} else if (text.contains("e")) {
			text = text.replace("e", "");
			d = Math.E;
			if (text.length() > 0) {
				d *= Double.parseDouble(text);
			}
		} else {
			d = Double.parseDouble(text);
		}
		return d;
	}
	/**
	 * Shows a dialog with the given title and message.
	 * @param title title
	 * @param message message
	 */

	public static void showDialog(String title, String message) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(title);
				alert.setHeaderText(null);
				alert.setContentText(message);
				alert.showAndWait();
			}
		});

	}

	/**
	 * Rounds the value to the given decimals.
	 * 
	 * @param value
	 *            value to be rounded.
	 * @param decimals
	 *            count to how many decimals the method should round
	 * @return rounded value
	 */
	public static double round(double value, int decimals) {
		if (decimals < 0)
			throw new IllegalArgumentException();
		Double theValue = value;
		if (!theValue.isInfinite() && !theValue.isNaN()) {
			BigDecimal bigDecimal = new BigDecimal(value);
			bigDecimal = bigDecimal.setScale(decimals, RoundingMode.HALF_UP);
			return bigDecimal.doubleValue();
		}
		return value;
	}
}
