package src;

import java.math.BigDecimal;
import java.math.RoundingMode;
import com.google.common.math.DoubleMath;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

/**
 * Statische Klasse die einige statischen Methoden beinhaltet, die in den anderen Klassen benoetigt werden.
 * @author Jonathan Brose
 */
public class Hilfsmethoden {
	
	private Hilfsmethoden() {
		
	}
	
	/**
	 * 
	 * Schaut ob d ein Vielfaches von PI oder e ist und gibt dann z.B 2pi anstatt 6,2...
	 * zurück.
	 * 
	 * @param d
	 *            double der als String mit Angepasster Darstellung returnt werden
	 *            soll.
	 * @return Überprüfter String mit der Angepassten Darstellung.
	 */

	public static String ueberpruefeDouble(double d) {
		double piFaktor = d / Math.PI;
		piFaktor = runde(piFaktor, 4);

		if (DoubleMath.fuzzyEquals(piFaktor, (int) piFaktor, 0.001)) {
			if (piFaktor == 0)
				return "0";

			return (piFaktor == 1 ? "" : (int) piFaktor + "") + "\u03C0";
		} else {
			piFaktor = d / Math.E;
			piFaktor = runde(piFaktor, 4);
			if (DoubleMath.fuzzyEquals(piFaktor, (int) piFaktor, 0.001)) {
				if (piFaktor == 0)
					return "0";
				return (piFaktor == 1 ? "" : (int) piFaktor + "") + "e";
			}
		}
		return d + "";
	}
	/**
	 * 
	 * Liest ein double aus einem {@link TextField}. Dabei wird ein "," zu ".", also wird z.B: 1,2 keinen Fehler geben. 
	 * Brueche werden ausgerechnet und als Dezimalzahl gespeichert.
	 * 
	 * @param textField das TextFeld aus dem gelesen werden soll.
	 * @return der eingelesene double
	 * @throws NumberFormatException wenn der in textField enthaltene String f&uuml;r Double.parseDouble() 
	 * unerlaubte Zeichen enth&auml;t, oder der String nicht interpretierbar ist.
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
	 * Wandelt einen String zum double um. Dabei wird "Pi" oder "e" durch die jeweilige passende Zahl ersetzt.
	 * 
	 * @param text der zum double gewandelt werden soll.
	 * @return der ueberpruefte text.
	 * @throws NumberFormatException wenn text f&uuml;r Double.parseDouble() 
	 * unerlaubte Zeichen enth&auml;t, oder der String nicht interpretierbar ist.
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
	 * Zeigt einen Dialog mit der uebergebenen Nachricht.
	 * @param titel des Dialoges
	 * @param nachricht des Dialoges
	 */

	public static void zeigeDialog(String titel, String nachricht) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(titel);
				alert.setHeaderText(null);
				alert.setContentText(nachricht);
				alert.showAndWait();
			}
		});

	}

	/**
	 * Rundet einen gegebenen double auf n Nachkommastellen, und gibt den gerundeten
	 * Wert zur�ck.
	 * 
	 * @param wert
	 *            Der Wert, der gerundet werden soll.
	 * @param nachkommaStellen
	 *            Anzahl der Nachkommastellen auf die gerundet werden soll.
	 * @return Der gerundete Wert
	 */
	public static double runde(double wert, int nachkommaStellen) {
		if (nachkommaStellen < 0)
			throw new IllegalArgumentException();
		Double derWert = wert;
		if (!derWert.isInfinite() && !derWert.isNaN()) {
			BigDecimal derBigDecimal = new BigDecimal(wert);
			derBigDecimal = derBigDecimal.setScale(nachkommaStellen, RoundingMode.HALF_UP);
			return derBigDecimal.doubleValue();
		}
		return wert;
	}
}
